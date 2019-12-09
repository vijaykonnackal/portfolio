package com.konnackal.aptos.portfolio.domain;

import java.util.Currency;
import java.util.Objects;

public final class Money {

    private final double amount;
    private final Currency currency;

    public Money(double amount, Currency currency) {
        this.amount = amount;
        if (null == currency) {
            throw new IllegalArgumentException("Currency must not be null.");
        }
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Double.compare(money.amount, amount) == 0 &&
                Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", amount, currency);
    }

    public Money add(Money money) {
        if (null == money) return this;

        if (!money.getCurrency().equals(this.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return new Money(this.amount + money.getAmount(), this.getCurrency());
    }

    public Money minus(Money money) {
        if (null == money) return this;
        if (!money.getCurrency().equals(this.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return new Money(this.amount - money.getAmount(), this.getCurrency());
    }

    public boolean lessThan(Money money) {
        if (!money.getCurrency().equals(this.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }
        return this.getAmount() < money.getAmount();
    }

    public Money times(double times) {
        return new Money(this.amount * times, this.currency);
    }
}
