package com.crawl.VietCap.controller;

import com.crawl.VietCap.bodyParam.TransactionRequestBody;
import com.crawl.VietCap.endpoints.HTTPRequest;
import com.crawl.VietCap.util.JsonUtil;
import com.google.gson.JsonObject;
import io.restassured.response.Response;

public class TransactionServer {

    public JsonObject crawlData(String requestBody) {

        try {
            JsonObject jsonRequestBody = JsonUtil.getJsonAsObject(requestBody);
            ////
            String inputSymbol = JsonUtil.getJsonValue(jsonRequestBody, "inputSymbol");
            Integer pageNum = Integer.parseInt(JsonUtil.getJsonValue(jsonRequestBody, "pageNum"));
            Integer limit = Integer.parseInt(JsonUtil.getJsonValue(jsonRequestBody, "limit"));
            String startDate = JsonUtil.getJsonValue(jsonRequestBody, "startDate");
            String endDate = JsonUtil.getJsonValue(jsonRequestBody, "endDate");
            ////
            String bodyToString = JsonUtil
                    .getMapToStringJson(TransactionRequestBody.get(inputSymbol, pageNum, limit, startDate, endDate));
            ////
            HTTPRequest request = new HTTPRequest(bodyToString);
            Response response = request.post();
            String jsonString = response.then().extract().asString();
            ////
            JsonObject transactionData = JsonUtil.getJsonAsObject(jsonString).get("data").getAsJsonObject()
                    .get("TickerPriceHistory")
                    .getAsJsonObject();
            return transactionData;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
