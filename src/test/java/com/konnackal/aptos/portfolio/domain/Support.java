package com.konnackal.aptos.portfolio.domain;

import java.util.Currency;

public class Support {

    private static final Currency USD = Currency.getInstance("USD");

    public static final Symbol AAPL = Symbol.of("AAPL");

    public static final Symbol GOOG = Symbol.of("GOOG");

    public static final Symbol MSFT = Symbol.of("MSFT");

    public static Money USD10() {
        return new Money(10.0, USD);
    }

    public static Money USD(double value) {
        return new Money(value, USD);
    }

    public static Position applePosition(int quantity) {
        return Position.from(AAPL, quantity);
    }

    public static Position googlePosition(int quantity) {
        return Position.from(GOOG, quantity);
    }

    public static Position msftPosition(int quantity) {
        return Position.from(MSFT, quantity);
    }

    public static Position position(String symbol, int quantity) {
        return Position.from(Symbol.of(symbol), quantity);
    }

    public static Stock appleStock(double price) {
        return new Stock(AAPL, "Apple", USD(price));
    }

    public static Stock googStock(double price) {
        return new Stock(GOOG, "Google", USD(price));
    }

    public static Stock msftStock(double price) {
        return new Stock(MSFT, "Microsoft", USD(price));
    }

    public static Holding appleHolding(double price, int quantity) {
        return Holding.from(appleStock(price), quantity);
    }

    public static Holding googHolding(double price, int quantity) {
        return Holding.from(googStock(price), quantity);
    }

    public static Holding msftHolding(double price, int quantity) {
        return Holding.from(msftStock(price), quantity);
    }
}
