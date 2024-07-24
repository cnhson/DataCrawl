package T4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import T4.model.TransactionEntity;

public class TradingVietStatisticCrawl {
    public static void main(String[] args) throws Exception {

        ///////////////////////////////////////////////////
        //
        String inputSymbol = "VCI";
        Integer pageNum = 1;
        //
        ///////////////////////////////////////////////////
        String stockSymbolUrl = "https://api.vietcap.com.vn/data-mt/graphql";
        String line = "";

        String query = "query Query($ticker: String!, $offset: Int!, $offsetInsider: Int!, $limit: Int!, $fromDate: String!, $toDate: String!) "
                + "{ TickerPriceHistory( ticker: $ticker offset: $offset limit: $limit fromDate: $fromDate toDate: $toDate ) "
                + "{ history { tradingDate priceChange openPrice closePrice highestPrice lowestPrice totalMatchVolume "
                + "totalMatchValue totalDealVolume totalDealValue totalVolume totalValue unMatchedBuyTradeVolume "
                + "unMatchedSellTradeVolume __typename } totalRecords __typename } "
                + "OrganizationDeals( ticker: $ticker offset: $offsetInsider limit: $limit fromDate: $fromDate toDate: $toDate ) "
                + "{ history { id organCode tradeTypeCode dealTypeCode actionTypeCode tradeStatusCode traderOrganCode shareBeforeTrade "
                + "ownershipBeforeTrade shareRegister shareAcquire shareAfterTrade ownershipAfterTrade startDate endDate sourceUrl publicDate "
                + "ticker traderPersonId traderName en_TraderName positionShortName en_PositionShortName positionName en_PositionName __typename } "
                + "totalRecords __typename } }";

        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        client = HttpClientBuilder.create().build();

        try {

            HttpPost getStockDataPost = new HttpPost(stockSymbolUrl);
            Gson gson = new Gson();

            getStockDataPost.addHeader("Accept", "application/json");
            getStockDataPost.addHeader("Content-Type", "application/json");
            String variable = "{\"ticker\":\"" + inputSymbol + "\",\"limit\":" + pageNum * 15
                    + ",\"offset\":0,\"offsetInsider\":0,\"fromDate\":\"2000-01-01\",\"toDate\":\"2100-01-01\"}";

            Map<String, Object> postBody = new HashMap<>();
            postBody.put("query", query);
            postBody.put("variables", variable);

            Type gsonType = new TypeToken<HashMap<String, Object>>() {
            }.getType();
            String bodyToString = gson.toJson(postBody, gsonType);

            StringEntity strEntity = new StringEntity(bodyToString);
            getStockDataPost.setEntity(strEntity);

            response = client.execute(getStockDataPost);

            BufferedReader buffReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder strBuilder = new StringBuilder();

            while ((line = buffReader.readLine()) != null) {
                strBuilder.append(line);
                strBuilder.append(System.lineSeparator());
            }

            JsonObject jsonObject = JsonParser.parseString(strBuilder.toString()).getAsJsonObject();
            JsonElement transDataArray = jsonObject.get("data").getAsJsonObject().get("TickerPriceHistory")
                    .getAsJsonObject()
                    .get("history");

            TransactionEntity[] transList = gson.fromJson(transDataArray,
                    TransactionEntity[].class);

            System.out.println("\n[Symbol: " + inputSymbol + ", page: " + String.valueOf(pageNum) + "]");
            System.out.printf("\n%s %14s %8s %10s %11s %10s %16s %16s %12s %12s %12s %12s %13s %13s\n\n",
                    "Ngày", "Thay đổi", "Mở cửa", "Cao nhất", "Thấp nhất", "Đóng cửa", "KLGD khớp lệnh",
                    "GTGD khớp lệnh", "KLGD tt", "GTGD tt", "Dư mua", "Dư bán",
                    "Tổng GTGD", "Tổng KLGD");
            for (TransactionEntity transEntity : transList) {
                System.out.printf("%10s", transEntity.getTradingDate());
                System.out.printf("%9.0f", checkValue(transEntity.getPriceChange(), 0.0));
                System.out.printf("%9.0f", checkValue(transEntity.getOpenPrice(), 0.0));
                System.out.printf("%11.0f", checkValue(transEntity.getHighestPrice(), 0.0));
                System.out.printf("%12.0f", checkValue(transEntity.getLowestPrice(), 0.0));
                System.out.printf("%11.0f", checkValue(transEntity.getClosePrice(), 0.0));
                System.out.printf("%17.0f", checkValue(transEntity.getTotalMatchVolume(), 0.0));
                System.out.printf("%17.2f", checkValue(transEntity.getTotalMatchValue(), 0.0) / Math.pow(10, 9));
                System.out.printf("%13.0f", checkValue(transEntity.getTotalDealVolume(), 0.0));
                System.out.printf("%13.2f", checkValue(transEntity.getTotalDealValue(), 0.0) / Math.pow(10, 9));
                System.out.printf("%13.3f",
                        checkValue(transEntity.getUnMatchedBuyTradeVolume(), 0.0) / Math.pow(10, 3));
                System.out.printf("%13.3f",
                        checkValue(transEntity.getUnMatchedSellTradeVolume(), 0.0) / Math.pow(10, 3));
                System.out.printf("%14.2f", checkValue(transEntity.getTotalValue(), 0.0) / Math.pow(10, 9));
                System.out.printf("%14.3f", checkValue(transEntity.getTotalVolume(), 0.0) / Math.pow(10, 3));

                System.out.println(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double checkValue(Double value, double defaultValue) {
        return value != null ? value : defaultValue;
    }
}
