package com.crawl.endpoints;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class HTTPRequest {

    private String payload;
    private String requestUrl;
    private Map<String, String> headersConfig = new HashMap<>();

    public HTTPRequest() {
        try {
            setDefaultHeadersConfig();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void setDefaultHeadersConfig() {
        this.headersConfig.put("Accept-language", "en-US,en;q=0.5");
        this.headersConfig.put("Content-type", "application/json");
        this.headersConfig.put("Accept", "application/json");
    }

    public void setPayLoad(String payload) {
        this.payload = payload;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public Response post() {
        return given().headers(this.headersConfig).accept(ContentType.ANY).body(this.payload).when()
                .post(this.requestUrl);

    }

    public Response get() {
        return given().headers(this.headersConfig).accept(ContentType.ANY).when().post(this.requestUrl);
    }

}