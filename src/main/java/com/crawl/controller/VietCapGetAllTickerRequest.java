package com.crawl.controller;

import com.crawl.endpoints.HTTPRequest;
import com.crawl.model.VietCapTickerEntity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.response.Response;

public class VietCapGetAllTickerRequest {

    public VietCapTickerEntity[] crawlData() {
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

            VietCapTickerEntity[] tickerList = gson.fromJson(transDataArray, VietCapTickerEntity[].class);

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
