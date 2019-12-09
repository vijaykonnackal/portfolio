package com.konnackal.aptos.portfolio.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.konnackal.aptos.portfolio.domain.Money;
import com.konnackal.aptos.portfolio.domain.Stock;
import com.konnackal.aptos.portfolio.domain.Symbol;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class StockPriceService {

    private static final String stockUrl = "https://api.worldtradingdata.com/api/v1/stock?symbol=%s&api_token=%s";

    public Map<Symbol, Stock> getPrice(Collection<Symbol> stockSymbols) throws Exception {
        AtomicInteger counter = new AtomicInteger();

        Collection<List<Symbol>> partitions = stockSymbols.stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 5))
                .values();

        Map<Symbol, Stock> all = new HashMap<>();
        for (List<Symbol> partition : partitions) {
            String symbols = partition.stream()
                    .map(Symbol::getSymbol)
                    .distinct()
                    .collect(Collectors.joining(","));

            URL url = new URL(String.format(stockUrl, symbols, getWorldTradingDataApiToken()));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(url);

            all.putAll(parseStocks((ArrayNode) root.get("data")));
        }

        return all;
    }

    static Map<Symbol, Stock> parseStocks(ArrayNode data) {
        Map<Symbol, Stock> stocks = new HashMap<>();

        data.forEach(stockData -> {
            Symbol symbol = Symbol.of(stockData.get("symbol").asText());
            String name = stockData.get("name").asText();
            Currency currency = Currency.getInstance(stockData.get("currency").asText());
            double price = stockData.get("price").asDouble();
            Stock stock = new Stock(symbol, name, new Money(price, currency));
            stocks.put(symbol, stock);
        });

        return stocks;
    }

    private static String getWorldTradingDataApiToken() {
        String apiToken = System.getenv("WORLDTRADEDATA_APITOKEN");
        if (null == apiToken) {
            throw new IllegalStateException("Cannot find env variable 'WORLDTRADEDATA_APITOKEN'");
        }
        return apiToken;
    }
}
