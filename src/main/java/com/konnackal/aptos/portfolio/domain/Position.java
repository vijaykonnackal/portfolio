package com.konnackal.aptos.portfolio.domain;

/**
 * Models a specific number of a Scrip
 */
public class Position {
    private final Symbol symbol;
    private final int quantity;

    private Position(Symbol symbol, int quantity) {
        this.symbol = symbol;
        this.quantity = quantity;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "" + quantity + " " + symbol;
    }

    public static Position from(Symbol symbol, int quantity) {
        return new Position(symbol, quantity);
    }
}
