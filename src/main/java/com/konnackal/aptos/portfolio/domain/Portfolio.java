package com.konnackal.aptos.portfolio.domain;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Models a Porfolio of stocks.
 * <p>
 * Sell and Buy are done by calling the sell and buy methods respectively. Use the factory method buildPortfolio
 * to construct an instance.
 * <p>
 * The implementation keeps track of the cash after a sell or buy is executed. Buy is not allowed if the cash balance
 * is not sufficient ot execute the transaction.
 */
public class Portfolio {

    private Map<Symbol, Holding> stocks = new HashMap<>();
    private TransactionHistory history = new TransactionHistory();
    private Money cash;

    private Portfolio() {
    }

    public void sell(Holding holding) {
        removeHolding(holding);
        cash = holding.getValue().add(cash);
        history.sold(holding); //business event
    }

    public void buy(Holding holding) {
        addHolding(holding);
        if (this.cash == null || cash.lessThan(holding.getValue())) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        cash = cash.minus(holding.getValue());
        history.bought(holding); //business event
    }

    public Set<Symbol> getSymbols() {
        return new HashSet<>(stocks.keySet());
    }

    public Holding getHolding(Symbol symbol) {
        return stocks.get(symbol);
    }

    public List<Holding> allHoldings() {
        return new LinkedList<>(stocks.values());
    }

    public boolean hasHolding(Symbol symbol) {
        return stocks.containsKey(symbol);
    }

    public Money getCash() {
        return cash;
    }

    public List<Transaction> getTransactionSummary() {
        return history.getSummary();
    }

    void addHolding(Holding holding) {
        stocks.compute(holding.getSymbol(), (symbol, existing) -> {
            if (null == existing) return holding;
            return Holding.from(holding.getStock(),
                    holding.getQuantity() + existing.getQuantity());
        });
    }

    void removeHolding(Holding holding) {
        stocks.compute(holding.getSymbol(), (symbol, existing) -> {
            if (null == existing || existing.getQuantity() < holding.getQuantity()) {
                // cannot sell more than what is available.
                throw new IllegalArgumentException("Insufficient quantity in Portfolio");
            }
            Holding newHolding = Holding.from(holding.getStock(),
                    existing.getQuantity() - holding.getQuantity());

            //if newQuantity is 0, return null which gets removed from stocks map
            return newHolding.getQuantity() == 0 ? null : newHolding;
        });
    }

    /**
     * Update the internal state with given stock objects. The quantity remains the same
     */
    public void updatePrices(Map<Symbol, Stock> prices) {
        Set<Symbol> symbols = new HashSet<>(stocks.keySet());
        symbols.forEach(symbol ->
                stocks.computeIfPresent(symbol,
                        (symbol1, holding) -> Holding.from(prices.get(symbol1), holding.getQuantity())));
    }

    /**
     * Gets the total value of the Portfolio using the last known price including the cash balance.
     * Call the updatePrices method to update the prices if required prior to calling this method
     */
    public Money getTotalValue() {
        Money totalValue = stocks.values()
                .stream()
                .map(Holding::getValue)
                .reduce(Money::add)
                .orElse(null);

        return null == totalValue ? cash : totalValue.add(cash);
    }

    /**
     * Gets the allocation percentage of the stocks
     */
    public List<Allocation> getAllocation() {
        Money total = getTotalValue();

        return stocks.values()
                .stream()
                .map(holding -> {
                    double p = holding.getValue().getAmount() / total.getAmount();
                    return new Allocation(holding.getSymbol(), p * 100);
                })
                .collect(Collectors.toList());
    }

    public static Portfolio buildPortfolio(List<Holding> holdings) {
        Portfolio portfolio = new Portfolio();
        holdings.forEach(portfolio::addHolding);
        return portfolio;
    }
}
