package com.crawl.VietCap.controller;

import com.crawl.VietCap.endpoints.HTTPRequest;
import com.crawl.VietCap.model.TickerEntity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.response.Response;

public class GetAllTickerRequest {

    public TickerEntity[] crawlData() {
        try {
            Gson gson = new Gson();
            HTTPRequest request = new HTTPRequest();
            request.setRequestUrl("https://trading.vietcap.com.vn/api/price/symbols/getAll");
            Response response = request.get();
            String jsonString = response.then().extract().asString();
            // JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            // JsonElement transDataArray = jsonObject.getAsJsonArray();
            // JsonElement transDataArray =
            // jsonObject.get(0).getAsJsonObject().get("CompanyFinancialRatio")
            // .getAsJsonObject()
            // .get("ratio");

            // System.out.println("here: " + transDataArray);
            JsonArray transDataArray = JsonParser.parseString(jsonString).getAsJsonArray();

            TickerEntity[] tickerList = gson.fromJson(transDataArray,
                    TickerEntity[].class);

            return tickerList;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    // private static double checkValue(Double value, double defaultValue) {
    // return value != null ? value : defaultValue;
    // }
}
