package com.konnackal.aptos.portfolio.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class TransactionHistory {
    private List<Transaction> history = new LinkedList<>();

    void bought(Holding holding) {
        history.add(new Transaction(Transaction.Type.BUY, holding));
    }

    void sold(Holding holding) {
        history.add(new Transaction(Transaction.Type.SELL, holding));
    }

    public List<Transaction> getSummary() {
        Map<Symbol, List<Transaction>> transactionBySymbol = history.stream()
                .collect(Collectors.groupingBy(transaction -> transaction.getHolding().getSymbol()));

        return transactionBySymbol.values().stream()
                .map(transactions -> {
                    int delta = 0;
                    for (Transaction transaction : transactions) {
                        if (transaction.getType() == Transaction.Type.BUY) delta+=transaction.getHolding().getQuantity();
                        if (transaction.getType() == Transaction.Type.SELL) delta-=transaction.getHolding().getQuantity();
                    }
                    Holding holding = Holding.from(transactions.get(0).getHolding().getStock(), Math.abs(delta));
                    return (delta > 0) ? new Transaction(Transaction.Type.BUY, holding) : new Transaction(Transaction.Type.SELL, holding);
                })
                .collect(Collectors.toList());
    }
}
