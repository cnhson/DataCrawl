package com.crawl.VietCap.bodyParam;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonParser;

public class TransactionBody {

    private static String query = "query Query($ticker: String!, $offset: Int!, $limit: Int!, $fromDate: String!, $toDate: String!) "
            +
            "{ " +
            "TickerPriceHistory( " +
            "ticker: $ticker " +
            "offset: $offset " +
            "limit: $limit " +
            "fromDate: $fromDate " +
            "toDate: $toDate " +
            ") { " +
            "totalRecords " +
            "history { " +
            "tradingDate " +
            "priceChange " +
            // "percentPriceChange " +
            "openPrice " +
            "closePrice " +
            "highestPrice " +
            "lowestPrice " +
            "totalMatchVolume " +
            "totalMatchValue " +
            // "totalDealVolume " +
            // "totalDealValue " +
            "totalVolume " +
            "totalValue " +
            // "unMatchedBuyTradeVolume " +
            // "unMatchedSellTradeVolume " +
            "} " +
            "} " +
            "}";

    public static Map<String, Object> get(String inputSymbol, Integer pageNum, Integer recordLimit, String startDate,
            String endDate) {
        // Default startDate: 2000-01-01
        // Default endDate: 2100-01-01
        try {
            if (pageNum > 0) {
                String variable = "{\"ticker\":\"" + inputSymbol + "\",\"limit\": " + recordLimit
                        + ",\"offset\": " + (pageNum - 1) * recordLimit + ",\"fromDate\": " + startDate
                        + ",\"toDate\": "
                        + endDate
                        + "}";
                Map<String, Object> jsonBody = new HashMap<>();
                jsonBody.put("query", query);
                jsonBody.put("variables", JsonParser.parseString(variable)
                        .getAsJsonObject().toString());
                return jsonBody;
            } else {
                System.err.println("Page number must be greater than 0");
                return null;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
