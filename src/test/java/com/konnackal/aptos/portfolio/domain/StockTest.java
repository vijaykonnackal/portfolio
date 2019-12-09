package com.konnackal.aptos.portfolio.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StockTest {

    @Test
    void testEq() {
        Stock stock1 = Support.googStock(10.0);
        Stock stock2 = Support.googStock(10.0);
        Assertions.assertEquals(stock1, stock2);

        Stock stock3 = Support.googStock(10.1);
        Assertions.assertNotEquals(stock1, stock3);

        Stock stock4 = Support.appleStock(10.0);
        Assertions.assertNotEquals(stock1, stock4);
    }

    @Test
    void testConstruction() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Stock(null, "A Stock", Support.USD10()));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Stock(Symbol.of("GOOG"), "google", null));
    }
}
