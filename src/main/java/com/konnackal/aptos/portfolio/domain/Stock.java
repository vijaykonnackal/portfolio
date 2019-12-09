package com.konnackal.aptos.portfolio.domain;

import java.util.Objects;

public class Stock {
    private final Symbol symbol;
    private final String name;
    private final Money price;

    public Stock(Symbol symbol, String name, Money price) {
        if (null == symbol) {
            throw new IllegalArgumentException("Stock symbol must not be null.");
        }
        if (null == price || price.getAmount() < 0) {
            throw new IllegalArgumentException("Stock price null OR < 0.");
        }
        this.symbol = symbol;
        this.price = price;
        this.name = name == null ? symbol.getSymbol() : name;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public Money getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return symbol.equals(stock.symbol) &&
                price.equals(stock.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, price);
    }

    @Override
    public String toString() {
        return symbol + "@" + price;
    }
}
