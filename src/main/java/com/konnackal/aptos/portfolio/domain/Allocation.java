package com.konnackal.aptos.portfolio.domain;

/*
An object that maps Symbol to allocation percentage
 */
public class Allocation {
    private final Symbol symbol;
    private final double percentage;

    public Allocation(Symbol symbol, double percentage) {
        this.symbol = symbol;
        this.percentage = percentage;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public double getPercentage() {
        return percentage;
    }

    @Override
    public String toString() {
        return String.format("%s %.2f%%", symbol, percentage);
    }
}
