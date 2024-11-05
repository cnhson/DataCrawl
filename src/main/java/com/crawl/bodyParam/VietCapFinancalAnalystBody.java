package com.crawl.bodyParam;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonParser;

public class VietCapFinancalAnalystBody {

    private static String query = "fragment Ratios on CompanyFinancialRatio {\n" + //
            "   yearReport\n" + //
            "   lengthReport\n" + //
            "   revenue\n" + //
            "   revenueGrowth\n" + //
            "   netProfit\n" + //
            "   netProfitGrowth\n" + //
            "   roe\n" + //
            "   roic\n" + //
            "   roa\n" + //
            "   ev\n" + //
            "   issueShare\n" + //
            "   eps\n" + //
            "   pe\n" + //
            "   pb\n" + //
            "   ebit\n" + //
            "   }\n" + //
            "   query Query($ticker: String!, $period: String!) {\n" + //
            "    CompanyFinancialRatio(ticker: $ticker, period: $period) {\n" + //
            "    ratio {\n" + //
            "      ...Ratios\n" + //
            "           }\n" + //
            "       }\n" + //
            "   }\n";

    public static Map<String, Object> get(String inputSymbol) {
        // Default period: Q
        try {

            String variable = "{\"ticker\":\"" + inputSymbol + "\",\"period\":\"Q\"}";
            Map<String, Object> jsonBody = new HashMap<>();
            jsonBody.put("query", query);
            jsonBody.put("variables", JsonParser.parseString(variable).getAsJsonObject().toString());
            return jsonBody;
        } catch (

        Exception e) {
            System.err.println("Error in FinancalAnalystBody param: " + e.getMessage());
            return null;
        }
    }
}
