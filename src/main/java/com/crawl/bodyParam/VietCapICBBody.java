package com.crawl.bodyParam;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonParser;

public class VietCapICBBody {

    private static String query = "query Query($icbLevel: Int!) {\n" + "OverviewIcbLevel(icbLevel: $icbLevel) {\n"
            + "icbName\n" + "icbCode\n" + "enIcbName\n" + "RSD11\n" + "percentPriceChange1Day\n"
            + "percentPriceChange1Week\n" + "percentPriceChange1Month\n" + "RSD21\n" + "RSD25\n" + "RSQ34\n" + "RSQ41\n"
            + "RSQ42\n" + "RSQ14\n" + "RSQ12\n" + "netProfitMargin\n" + "RSQ25\n" + "RSQ11\n" + "RSD30\n" + "\n}\n}";

    public static Map<String, Object> get() {

        try {
            String variable = "{icbLevel:2}";
            Map<String, Object> jsonBody = new HashMap<>();
            jsonBody.put("query", query);
            jsonBody.put("variables", JsonParser.parseString(variable).getAsJsonObject().toString());
            return jsonBody;
        } catch (Exception e) {
            System.err.println("VietCapICBBody: " + e.getMessage());
            return null;
        }
    }
}
