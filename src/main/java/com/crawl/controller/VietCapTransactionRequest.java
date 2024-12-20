package com.crawl.controller;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import com.crawl.bodyParam.VietCapTransactionBody;
import com.crawl.endpoints.HTTPRequest;
import com.crawl.model.VietCapTransactionEntity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import io.restassured.response.Response;

public class VietCapTransactionRequest {

    private Integer totalRecords;

    public List<VietCapTransactionEntity> crawlData(String inputSymbol, Integer pageNum, Integer limit,
            String startDate, String endDate) {

        try {
            Gson gson = new Gson();
            Type gsonType = new TypeToken<HashMap<String, Object>>() {
            }.getType();

            String bodyToString = gson
                    .toJson(VietCapTransactionBody.get(inputSymbol, pageNum, limit, startDate, endDate), gsonType);
            HTTPRequest request = new HTTPRequest();
            request.setPayLoad(bodyToString);
            request.setRequestUrl("https://api.vietcap.com.vn/data-mt/graphql");
            Response response = request.post();
            String jsonString = response.then().extract().asString();
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject().get("data").getAsJsonObject()
                    .get("TickerPriceHistory").getAsJsonObject();

            setTotalRecords(jsonObject.get("totalRecords").getAsInt());

            Type transListType = new TypeToken<List<VietCapTransactionEntity>>() {
            }.getType();

            List<VietCapTransactionEntity> transList = gson.fromJson(jsonObject.get("history"), transListType);
            return transList;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }
    // private static double checkValue(Double value, double defaultValue) {
    // return value != null ? value : defaultValue;
    // }
}
