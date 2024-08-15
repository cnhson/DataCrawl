package com.crawl.VietCap.controller;

import java.lang.reflect.Type;
import java.util.HashMap;

import com.crawl.VietCap.bodyParam.TransactionRequestBody;
import com.crawl.VietCap.endpoints.HTTPRequest;
import com.crawl.VietCap.model.TransactionEntity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import io.restassured.response.Response;

public class TransactionRequest {

    public TransactionEntity[] crawlData(String filename, String inputSymbol, Integer pageNum, Integer limit,
            String startDate, String endDate) {

        try {
            Gson gson = new Gson();
            Type gsonType = new TypeToken<HashMap<String, Object>>() {
            }.getType();

            String bodyToString = gson
                    .toJson(TransactionRequestBody.get(inputSymbol, pageNum, limit, startDate, endDate), gsonType);
            HTTPRequest request = new HTTPRequest(bodyToString);
            Response response = request.post();
            String jsonString = response.then().extract().asString();
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            JsonElement transDataArray = jsonObject.get("data").getAsJsonObject().get("TickerPriceHistory")
                    .getAsJsonObject()
                    .get("history");

            TransactionEntity[] transList = gson.fromJson(transDataArray,
                    TransactionEntity[].class);
            return transList;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    // private static double checkValue(Double value, double defaultValue) {
    // return value != null ? value : defaultValue;
    // }
}
