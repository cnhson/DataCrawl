package com.crawl.controller;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.crawl.endpoints.HTTPRequest;
import com.crawl.model.CafeFTransactionEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import io.restassured.response.Response;

public class CafeFTransactionRequest {

    public List<CafeFTransactionEntity> crawlData(String inputSymbol, String startDate, String endDate, Integer pageNum,
            Integer limit) {
        Response response = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Change "yyyy-MM-dd" to "MM/dd/YYYY"
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate tempStartDate = LocalDate.parse(startDate, inputFormatter);
            LocalDate tempEndDate = LocalDate.parse(endDate, inputFormatter);
            startDate = tempStartDate.format(outputFormatter);
            endDate = tempEndDate.format(outputFormatter);
            //

            HTTPRequest request = new HTTPRequest();
            String url = MessageFormat.format(
                    "https://s.cafef.vn/Ajax/PageNew/DataHistory/PriceHistory.ashx?Symbol={0}&StartDate={1}&EndDate={2}&PageIndex={3}&PageSize={4}",
                    inputSymbol, startDate, endDate, pageNum, limit.toString());

            request.setRequestUrl(url);
            response = request.get();
            String jsonString = response.then().extract().asString();
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            JsonElement transDataArray = jsonObject.get("Data").getAsJsonObject().get("Data");

            Type businessProfileListType = new TypeToken<List<CafeFTransactionEntity>>() {
            }.getType();

            List<CafeFTransactionEntity> businessProfileList = gson.fromJson(transDataArray, businessProfileListType);

            return businessProfileList;
        } catch (Exception e) {
            System.err.println("[BusinessProfileRequest] Current symbol:" + inputSymbol);
            System.err.println("[BusinessProfileRequest] Error: " + e.getMessage());
            System.err.println("[BusinessProfileRequest] Response: " + response.asString());
            return null;
        }
    }

}
