package com.konnackal.aptos.portfolio.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

class PortfolioTest {

    @Test
    void testAddRemoveHolding() {
        Portfolio portfolio = Portfolio.buildPortfolio(Collections.emptyList());

        portfolio.addHolding(Support.appleHolding( 10, 10));
        portfolio.addHolding(Support.appleHolding(15, 15));

        Assertions.assertEquals(25, portfolio.getHolding(Support.AAPL).getQuantity());

        portfolio.addHolding(Support.googHolding(50, 10));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> portfolio.removeHolding(Support.googHolding(50, 11)));

        portfolio.removeHolding(Support.appleHolding(15, 15));
        Assertions.assertEquals(10, portfolio.getHolding(Support.AAPL).getQuantity());

        Set<Symbol> symbols = portfolio.getSymbols();
        Assertions.assertEquals(2, symbols.size());
    }

    @Test
    void buyAndSell() {
        Portfolio portfolio = Portfolio.buildPortfolio(Collections.emptyList());
        portfolio.addHolding(Support.appleHolding(10, 10));
        portfolio.sell(Support.appleHolding(10, 5));
        Assertions.assertEquals(5, portfolio.getHolding(Support.AAPL).getQuantity());
        Assertions.assertEquals(50, portfolio.getCash().getAmount());

        portfolio.buy(Support.appleHolding(20, 2));
        Assertions.assertEquals(7, portfolio.getHolding(Support.AAPL).getQuantity());
        Assertions.assertEquals(10, portfolio.getCash().getAmount());
    }
}
