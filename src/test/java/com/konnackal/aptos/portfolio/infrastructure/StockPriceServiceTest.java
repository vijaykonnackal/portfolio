package com.konnackal.aptos.portfolio.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.konnackal.aptos.portfolio.Slow;
import com.konnackal.aptos.portfolio.domain.Stock;
import com.konnackal.aptos.portfolio.domain.Symbol;
import com.konnackal.aptos.portfolio.infrastructure.StockPriceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

class StockPriceServiceTest {

    @Test
    void testStockDataParse() throws Exception {
        String stockData = "[{\n" +
                "            \"symbol\": \"SNAP\",\n" +
                "            \"name\": \"Snap Inc.\",\n" +
                "            \"currency\": \"USD\",\n" +
                "            \"price\": \"14.74\",\n" +
                "            \"price_open\": \"14.86\",\n" +
                "            \"timezone_name\": \"America/New_York\"\n" +
                "        }, {\n" +
                "            \"symbol\": \"TWTR\",\n" +
                "            \"name\": \"Twitter, Inc.\",\n" +
                "            \"currency\": \"USD\",\n" +
                "            \"price\": \"30.19\",\n" +
                "            \"price_open\": \"30.35\"}]";
        ArrayNode node = (ArrayNode) new ObjectMapper().readTree(stockData);
        Map<Symbol, Stock> stocks = StockPriceService.parseStocks(node);
        Assertions.assertEquals(2, stocks.size());
    }

    @Slow
    @Test
    void testStockPriceGet() throws Exception {
        StockPriceService stockPriceService = new StockPriceService();
        Map<Symbol, Stock> stocks = stockPriceService.getPrice(Arrays.asList(Symbol.of("AAPL"), Symbol.of("GOOG")));
        Assertions.assertEquals(2, stocks.size());
    }
}
