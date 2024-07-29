package com.crawl.VietCap.endpoints;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class HTTPRequest {

    private String payload;
    private String requestUrl = "https://api.vietcap.com.vn/data-mt/graphql";
    private Map<String, String> headersConfig = new HashMap<>();

    public HTTPRequest(String payload) {
        try {
            this.payload = payload;
            setDefaultHeadersConfig();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void setDefaultHeadersConfig() {
        this.headersConfig.put("Accept-language", "en-US,en;q=0.5");
        this.headersConfig.put("Content-type", "application/json");
        this.headersConfig.put("Accept", "application/json");
    }

    public Response post() {
        return given()
                .headers(this.headersConfig)
                .accept(ContentType.ANY)
                .body(this.payload)
                .when()
                .post(this.requestUrl);
    }
}