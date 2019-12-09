package com.konnackal.aptos.portfolio;

import com.konnackal.aptos.portfolio.domain.*;
import com.konnackal.aptos.portfolio.infrastructure.StockPriceService;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PortfolioService {

    private StockPriceService stockPriceService;

    public PortfolioService(StockPriceService stockPriceService) {
        this.stockPriceService = stockPriceService;
    }

    public Portfolio buildPortfolio(List<Position> positions) throws Exception {
        List<Holding> holdings = createStockHoldings(positions);
        return Portfolio.buildPortfolio(holdings);
    }

    public void rebalance(Portfolio portfolio, List<Allocation> allocations) throws Exception {
        Map<Symbol, Allocation> target = allocations.stream()
                .collect(Collectors.toMap(Allocation::getSymbol, Function.identity()));

        // collect all symbols - existing + target state. need price of all
        Set<Symbol> symbols = portfolio.getSymbols();
        symbols.addAll(target.keySet());

        Map<Symbol, Stock> prices = stockPriceService.getPrice(symbols);

        //update portfolio with latest price
        portfolio.updatePrices(prices);

        Money totalValue = portfolio.getTotalValue();

        List<Holding> buys = new LinkedList<>();
        List<Holding> sells = new LinkedList<>();

        for (Holding holding : portfolio.allHoldings()) {
            Symbol symbol = holding.getSymbol();
            Stock stock = prices.get(symbol);
            Allocation allocation = target.get(symbol);
            if (null == allocation) {
                // not present in target allocation. Sell all
                sells.add(holding);
            } else {
                // truncate to nearest integer as stock can bought/sold only in whole numbers
                int newQuantity = (int) ((totalValue.getAmount() * allocation.getPercentage()) / (100 * stock.getPrice().getAmount()));
                int diff = newQuantity - holding.getQuantity();
                if (diff < 0) {
                    sells.add(Holding.from(stock, -diff));
                } else {
                    buys.add(Holding.from(stock, diff));
                }
            }
        }

        for (Allocation allocation : allocations) {
            Symbol symbol = allocation.getSymbol();
            if (!portfolio.hasHolding(symbol)) {
                // new stock in portfolio. Buy as required
                Stock stock = prices.get(symbol);
                int diff = (int) ((totalValue.getAmount() * allocation.getPercentage()) / (100 * stock.getPrice().getAmount()));
                Holding newHolding = Holding.from(stock, diff);
                buys.add(newHolding);
            }
        }

        sells.forEach(portfolio::sell);
        buys.forEach(portfolio::buy);
    }

    private List<Holding> createStockHoldings(List<Position> positions) throws Exception {
        Set<Symbol> symbols = positions
                .stream()
                .map(Position::getSymbol)
                .collect(Collectors.toSet());

        Map<Symbol, Stock> prices = stockPriceService.getPrice(symbols);

        return positions.stream()
                .map(position -> Holding.from(prices.get(position.getSymbol()), position.getQuantity()))
                .collect(Collectors.toList());
    }
}
