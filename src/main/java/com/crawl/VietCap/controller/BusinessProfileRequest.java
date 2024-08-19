package com.crawl.VietCap.controller;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import com.crawl.VietCap.bodyParam.BusinessProfileBody;
import com.crawl.VietCap.endpoints.HTTPRequest;
import com.crawl.VietCap.model.BusinessProfileEntity;
import com.crawl.VietCap.model.TransactionEntity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import io.restassured.response.Response;

public class BusinessProfileRequest {

    public List<BusinessProfileEntity> crawlData(String inputSymbol) {
        try {
            Gson gson = new Gson();
            Type gsonType = new TypeToken<HashMap<String, Object>>() {
            }.getType();

            String bodyToString = gson
                    .toJson(BusinessProfileBody.get(inputSymbol), gsonType);
            HTTPRequest request = new HTTPRequest();
            request.setPayLoad(bodyToString);
            request.setRequestUrl("https://api.vietcap.com.vn/data-mt/graphql");
            Response response = request.post();
            String jsonString = response.then().extract().asString();
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            JsonElement transDataArray = jsonObject.get("data").getAsJsonObject().get("CompanyFinancialRatio")
                    .getAsJsonObject()
                    .get("ratio");

            Type businessProfileListType = new TypeToken<List<BusinessProfileEntity>>() {
            }.getType();

            List<BusinessProfileEntity> businessProfileList = gson.fromJson(transDataArray,
                    businessProfileListType);

            return businessProfileList;
        } catch (Exception e) {
            System.err.println("[BusinessProfileRequest] Current symbol:" + inputSymbol);
            System.err
                    .println("[BusinessProfileRequest] Error: " + e.getMessage());
            return null;
        }
    }

    // private static double checkValue(Double value, double defaultValue) {
    // return value != null ? value : defaultValue;
    // }
}
