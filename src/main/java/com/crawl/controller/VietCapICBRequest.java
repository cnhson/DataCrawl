package com.crawl.controller;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import com.crawl.bodyParam.VietCapICBBody;
import com.crawl.endpoints.HTTPRequest;
import com.crawl.model.VietCapICBEntity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import io.restassured.response.Response;

public class VietCapICBRequest {

    public List<VietCapICBEntity> crawlData() {
        try {
            Gson gson = new Gson();
            Type gsonType = new TypeToken<HashMap<String, Object>>() {
            }.getType();

            String bodyToString = gson.toJson(VietCapICBBody.get(), gsonType);
            HTTPRequest request = new HTTPRequest();
            request.setPayLoad(bodyToString);

            request.setRequestUrl("https://trading.vietcap.com.vn/data-mt/graphql");
            Response response = request.post();

            String jsonString = response.then().extract().asString();
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            JsonElement transDataArray = jsonObject.get("data").getAsJsonObject().get("OverviewIcbLevel")
                    .getAsJsonArray();

            Type icbListType = new TypeToken<List<VietCapICBEntity>>() {
            }.getType();
            List<VietCapICBEntity> tickerList = gson.fromJson(transDataArray, icbListType);

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
