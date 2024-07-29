package com.crawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;

import com.crawl.VietCap.controller.TransactionServer;
import com.crawl.VietCap.util.JsonUtil;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

//`import com.foxthehuman.VietCapCrawl.controller.TransactionController;

public class Server {
    public static void main(String[] args) {
        // String inputSymbol = "VCI";
        // Integer pageNum = 1;
        // Integer limit = 100;
        // String startDate = "2024-07-01";
        // String endDate = "2100-01-01";
        // TransactionController vct = new TransactionController();
        // vct.crawlData(inputSymbol, pageNum, limit, startDate, endDate);

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/api/crawl/vietcap", new DataProcessHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
            System.out.println("Server started on port 8080");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    static class DataProcessHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                handlePost(exchange);
            } else {
                handleGet(exchange);
            }
        }

        private void handleGet(HttpExchange exchange) throws IOException {
            String response = JsonUtil.getJsonResponseAsString("Fail", "Use POST method to send data");
            exchange.sendResponseHeaders(200, response.length());
            OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody(), "UTF-8");
            writer.write(response);
            writer.close();
        }

        private void handlePost(HttpExchange exchange) throws IOException {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), "UTF-8"));
                StringBuilder requestBody = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }

                TransactionServer ts = new TransactionServer();
                JsonObject jsonData = ts.crawlData(requestBody.toString());
                String response = JsonUtil.getJsonResponseAsString("Success", jsonData.toString());
                exchange.sendResponseHeaders(200, response.length());
                OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody(), "UTF-8");
                writer.write(response);
                writer.close();
            } catch (Exception e) {
                String response = JsonUtil.getJsonResponseAsString("Fail", "Error occured");
                exchange.sendResponseHeaders(400, response.length());
                OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody(), "UTF-8");
                writer.write(response);
                writer.close();
            }
        }
    }
}