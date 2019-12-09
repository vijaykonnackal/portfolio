package com.konnackal.aptos.portfolio.domain;

/**
 * Models a specific number of Stocks
 */
public class Holding {
    private final Stock stock;
    private final int quantity;

    private Holding(Stock stock, int quantity) {
        this.stock = stock;
        this.quantity = quantity;
    }

    public Stock getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public Symbol getSymbol() {
        return stock.getSymbol();
    }

    /**
     * Gets the value of the number of stocks held
     * @return
     */
    public Money getValue() {
        return stock.getPrice().times(this.quantity);
    }

    @Override
    public String toString() {
        return quantity + " " + stock;
    }

    public static Holding from(Stock stock, int quantity) {
        return new Holding(stock, quantity);
    }
}
