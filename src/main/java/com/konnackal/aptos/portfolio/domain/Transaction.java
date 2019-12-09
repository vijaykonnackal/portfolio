package com.konnackal.aptos.portfolio.domain;

public class Transaction {
    enum Type {
        BUY, SELL
    }

    private final Type type;
    private final Holding holding;

    Transaction(Type type, Holding holding) {
        this.type = type;
        this.holding = holding;
    }

    public Type getType() {
        return type;
    }

    public Holding getHolding() {
        return holding;
    }

    @Override
    public String toString() {
        return String.format("%s %s", type == Type.BUY ? "B" : "S", holding);
    }
}
