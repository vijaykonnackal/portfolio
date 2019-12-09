package com.konnackal.aptos.portfolio.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Currency;

class MoneyTest {

    @Test
    void moneyAdd() {
        Money m1 = Support.USD10();
        Money m2 = Support.USD(20.5);
        Money sum = m1.add(m2);
        Assertions.assertEquals(30.5, sum.getAmount());
        Assertions.assertEquals(Currency.getInstance("USD"), sum.getCurrency());
    }

    @Test
    void moneyAddResultsInCurrencyMismatch() {
        Money m1 = Support.USD10();
        Money m2 = new Money(20.5, Currency.getInstance("INR"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> m1.add(m2));
    }

    @Test
    void moneyTimesTest() {
        Money m1 = Support.USD(25.6);
        Money m2 = m1.times(20.5);
        Assertions.assertEquals(25.6 * 20.5, m2.getAmount());
    }
}
