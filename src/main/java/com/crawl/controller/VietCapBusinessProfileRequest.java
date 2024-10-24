package com.crawl.controller;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import com.crawl.bodyParam.VietCapBusinessProfileBody;
import com.crawl.endpoints.HTTPRequest;
import com.crawl.model.VietCapBusinessProfileEntity;
import com.crawl.model.VietCapTransactionEntity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import io.restassured.response.Response;

public class VietCapBusinessProfileRequest {

    public List<VietCapBusinessProfileEntity> crawlData(String inputSymbol) {
        Response response = null;
        try {
            Gson gson = new Gson();
            Type gsonType = new TypeToken<HashMap<String, Object>>() {
            }.getType();

            String bodyToString = gson.toJson(VietCapBusinessProfileBody.get(inputSymbol), gsonType);
            HTTPRequest request = new HTTPRequest();
            request.setPayLoad(bodyToString);
            request.setRequestUrl("https://api.vietcap.com.vn/data-mt/graphql");
            response = request.post();
            String jsonString = response.then().extract().asString();
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            JsonElement transDataArray = jsonObject.get("data").getAsJsonObject().get("CompanyFinancialRatio")
                    .getAsJsonObject().get("ratio");

            Type businessProfileListType = new TypeToken<List<VietCapBusinessProfileEntity>>() {
            }.getType();

            List<VietCapBusinessProfileEntity> businessProfileList = gson.fromJson(transDataArray,
                    businessProfileListType);

            return businessProfileList;
        } catch (Exception e) {
            System.err.println("[BusinessProfileRequest] Current symbol:" + inputSymbol);
            System.err.println("[BusinessProfileRequest] Error: " + e.getMessage());
            System.err.println("[BusinessProfileRequest] Response: " + response.asString());
            return null;
        }
    }

}
