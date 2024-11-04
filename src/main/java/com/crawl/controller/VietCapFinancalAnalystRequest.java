package com.crawl.controller;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import com.crawl.bodyParam.VietCapFinancalAnalystBody;
import com.crawl.endpoints.HTTPRequest;
import com.crawl.model.VietCapFinancalAnalystEntity;
import com.crawl.model.VietCapTransactionEntity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import io.restassured.response.Response;

public class VietCapFinancalAnalystRequest {

    public List<VietCapFinancalAnalystEntity> crawlData(String inputSymbol) {
        Response response = null;
        try {
            Gson gson = new Gson();
            Type gsonType = new TypeToken<HashMap<String, Object>>() {
            }.getType();

            String bodyToString = gson.toJson(VietCapFinancalAnalystBody.get(inputSymbol), gsonType);
            HTTPRequest request = new HTTPRequest();
            request.setPayLoad(bodyToString);
            request.setRequestUrl("https://api.vietcap.com.vn/data-mt/graphql");
            response = request.post();
            String jsonString = response.then().extract().asString();
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            JsonElement transDataArray = jsonObject.get("data").getAsJsonObject().get("CompanyFinancialRatio")
                    .getAsJsonObject().get("ratio");

            Type FinancalAnalystListType = new TypeToken<List<VietCapFinancalAnalystEntity>>() {
            }.getType();

            List<VietCapFinancalAnalystEntity> FinancalAnalystList = gson.fromJson(transDataArray,
                    FinancalAnalystListType);

            return FinancalAnalystList;
        } catch (Exception e) {
            System.err.println("[FinancalAnalystRequest] Current symbol:" + inputSymbol);
            System.err.println("[FinancalAnalystRequest] Error: " + e.getMessage());
            System.err.println("[FinancalAnalystRequest] Response: " + response.asString());
            return null;
        }
    }

}
