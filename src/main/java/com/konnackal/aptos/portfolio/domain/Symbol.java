package com.konnackal.aptos.portfolio.domain;

import java.util.Objects;

public final class Symbol {
    private final String symbol;

    private Symbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol1 = (Symbol) o;
        return symbol.equals(symbol1.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    @Override
    public String toString() {
        return symbol;
    }

    public static Symbol of(String symbol) {
        return new Symbol(symbol);
    }
}
