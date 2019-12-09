package com.konnackal.aptos.portfolio;

import com.konnackal.aptos.portfolio.domain.*;
import com.konnackal.aptos.portfolio.infrastructure.StockPriceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

class PortfolioServiceTest {

    @Test
    void buildPortfolio() throws Exception {
        PortfolioService portfolioService = new PortfolioService(new MockStockPriceService());

        Portfolio portfolio = portfolioService.buildPortfolio(Arrays.asList(
                Support.applePosition(20),
                Support.googlePosition(30),
                Support.msftPosition(20)));

        Assertions.assertNull(portfolio.getCash());
        Assertions.assertEquals(3, portfolio.getSymbols().size());
    }

    @Test
    void rebalanceMock() throws Exception {
        PortfolioService portfolioService = new PortfolioService(new MockStockPriceService());
        List<Position> initialPosition = Arrays.asList(
                Support.applePosition(1000));

        Portfolio portfolio = portfolioService.buildPortfolio(initialPosition);

        List<Allocation> newAllocation = Arrays.asList(
                new Allocation(Support.GOOG, 50),
                new Allocation(Support.AAPL, 50));

        portfolioService.rebalance(portfolio, newAllocation);

        Assertions.assertEquals(500, portfolio.getHolding(Support.AAPL).getQuantity());
        Assertions.assertEquals(250, portfolio.getHolding(Support.GOOG).getQuantity());
        Assertions.assertEquals(0, portfolio.getCash().getAmount());

        newAllocation = Arrays.asList(
                new Allocation(Support.MSFT, 100));
        portfolioService.rebalance(portfolio, newAllocation);

        Assertions.assertEquals(1, portfolio.allHoldings().size());
        Assertions.assertEquals(10, portfolio.getCash().getAmount());

        List<Allocation> result = portfolio.getAllocation();
        result.forEach(System.out::println);
    }

    @Main
    @Slow
    @Test
    void rebalance() throws Exception {
        PortfolioService portfolioService = new PortfolioService(new StockPriceService());

        List<Position> positions = Arrays.asList(
                Support.applePosition(50),
                Support.googlePosition(200),
                Position.from(Symbol.of("CYBR"), 150),
                Position.from(Symbol.of("ABB"), 900));

        Portfolio portfolio = portfolioService.buildPortfolio(positions);

        List<Allocation> newAllocation = Arrays.asList(
                new Allocation(Support.AAPL, 22),
                new Allocation(Support.GOOG, 38),
                new Allocation(Symbol.of("GFN"), 25),
                new Allocation(Symbol.of("ACAD"), 15)
        );

        portfolioService.rebalance(portfolio, newAllocation);

        List<Allocation> result = portfolio.getAllocation();
        Assertions.assertEquals(4, result.size());

        Set<Symbol> currentSymbols = result.stream().map(Allocation::getSymbol).collect(Collectors.toSet());
        Assertions.assertTrue(currentSymbols.contains(Support.AAPL));
        Assertions.assertTrue(currentSymbols.contains(Support.GOOG));
        Assertions.assertTrue(currentSymbols.contains(Symbol.of("GFN")));
        Assertions.assertTrue(currentSymbols.contains(Symbol.of("ACAD")));

        Assertions.assertFalse(currentSymbols.contains(Symbol.of("CYBR")));
        Assertions.assertFalse(currentSymbols.contains(Symbol.of("ABB")));

        System.out.println("\n===================Allocation==================");
        result.forEach(System.out::println);
        System.out.println("===================Transactions================");
        portfolio.getTransactionSummary().forEach(System.out::println);
        System.out.println("===================Cash========================");
        System.out.println("Cash : " + portfolio.getCash());
        System.out.println("===============================================");
        System.out.println("Done");
    }

    static class MockStockPriceService extends StockPriceService {
        @Override
        public Map<Symbol, Stock> getPrice(Collection<Symbol> stockSymbols) throws Exception {
            Map<Symbol, Stock> prices = new HashMap<>();
            prices.put(Support.AAPL, Support.appleStock(10.0));
            prices.put(Support.GOOG, Support.googStock(20.0));
            prices.put(Support.MSFT, Support.msftStock(30.0));
            return prices;
        }
    }
}
