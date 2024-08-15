package com.crawl.VietCap.bodyParam;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonParser;

public class BusinessProfileBody {

    private static String query = "fragment Ratios on CompanyFinancialRatio {\r\n" + //
            "    yearReport\r\n" + //
            "    lengthReport\r\n" + //
            "    ev\r\n" + //
            "    issueShare\r\n" + //
            "    eps\r\n" + //
            "    pe\r\n" + //
            "    pb\r\n" + //
            "}\r\n" + //
            "\r\n" + //
            "query Query($ticker: String!, $period: String!) {\r\n" + //
            "    CompanyFinancialRatio(ticker: $ticker, period: $period) {\r\n" + //
            "    ratio {\r\n" + //
            "      ...Ratios\r\n" + //
            "    }\r\n" + //
            "  }\r\n" + //
            "}\r\n";

    public static Map<String, Object> get(String inputSymbol) {
        // Default period: Q
        try {

            String variable = "{\"ticker\":\"" + inputSymbol + "\",\"period\":\"Q\"}";
            Map<String, Object> jsonBody = new HashMap<>();
            jsonBody.put("query", query);
            jsonBody.put("variables", JsonParser.parseString(variable)
                    .getAsJsonObject().toString());
            return jsonBody;
        } catch (

        Exception e) {
            System.err.println("Error in BusinessProfileBody param: " + e.getMessage());
            return null;
        }
    }
}
