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

import T4.model.RatioEntity;

public class TradingVietCrawl {
    public static void main(String[] args) throws Exception {

        ///////////////////////////////////////////////////
        String inputSymbol = "VNM";
        String inputType = "Y";
        ///////////////////////////////////////////////////
        String stockSymbolUrl = "https://api.vietcap.com.vn/data-mt/graphql";
        String line = "";

        String query = "fragment Ratios on CompanyFinancialRatio {\n  yearReport\n  lengthReport\n  "
                + "revenue\n  revenueGrowth\n  netProfit\n  netProfitGrowth\n  ebitMargin\n  roe\n  roic\n  roa\n  pe\n  pb\n  eps\n  "
                + "currentRatio\n  cashRatio\n  quickRatio\n  interestCoverage\n  ae\n}\n\nquery Query($ticker: String!, $period: String!) {\n  CompanyFinancialRatio(ticker: $ticker, period: $period) {\n    ratio {\n      ...Ratios\n}\n    period\n}\n}\n";

        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        client = HttpClientBuilder.create().build();

        // Scanner scanner = new Scanner(System.in);
        // while (inputSymbol.trim().equals("") || inputSymbol.trim().equals(null)) {
        // System.out.print("Input stock ID: ");
        // inputSymbol = scanner.next();
        // }
        // while (inputType.trim().equals("") || inputType.trim().equals(null) ||
        // (!inputType.trim().equals("Q")
        // && !inputType.trim().equals("Y"))) {
        // System.out.print("Input type (Q/Y): ");
        // inputType = scanner.next();
        // }
        // scanner.close();

        try {

            HttpPost getStockDataPost = new HttpPost(stockSymbolUrl);
            Gson gson = new Gson();

            getStockDataPost.addHeader("Accept", "application/json");
            getStockDataPost.addHeader("Content-Type", "application/json");
            String variable = "{\"ticker\":\"" + inputSymbol + "\",\"period\":\"" + inputType + "\"}";

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
            JsonElement ratioDataArray = jsonObject.get("data").getAsJsonObject().get("CompanyFinancialRatio")
                    .getAsJsonObject()
                    .get("ratio");

            RatioEntity[] entityList = gson.fromJson(ratioDataArray,
                    RatioEntity[].class);

            System.out.printf("\n%s %10s %12s %10s %12s %12s %12s %12s %12s %10s %10s %10s %10s %10s %10s %10s\n",
                    "Year",
                    "Revenue",
                    "Revenue", "Net", "Net Profit",
                    "Ebit", "Roe", "Roic", "Roa", "Pe", "Pb", "Eps", "Current", "Quick", "Interest", "Ae");
            System.out.printf("%28s %10s %12s %12s %82s %10s %10s\n\n", "Growth", "Profit", "Growth", "Margin", "Ratio",
                    "Ratio", "Coverage");

            for (RatioEntity ratioEntity : entityList) {
                if (inputType.equals("Q"))
                    System.out.printf("%s%d %2d", "Q", ratioEntity.getLengthReport(),
                            ratioEntity.getYearReport());
                else
                    System.out.printf("%2d", ratioEntity.getYearReport());
                System.out.printf("%11.2f", checkValue(ratioEntity.getRevenue(), 0.0) / Math.pow(10, 9));
                System.out.printf("%11.2f %%", checkValue(ratioEntity.getRevenueGrowth(), 0.0) * 100);
                System.out.printf("%11.2f", checkValue(ratioEntity.getNetProfit(), 0.0) / Math.pow(10, 9));
                System.out.printf("%11.2f %%", checkValue(ratioEntity.getNetProfitGrowth(), 0.0) * 100);
                System.out.printf("%11.2f %%", checkValue(ratioEntity.getEbitMargin(), 0.0) * 100);
                System.out.printf("%11.2f %%", checkValue(ratioEntity.getRoe(), 0.0) * 100);
                System.out.printf("%11.2f %%", checkValue(ratioEntity.getRoic(), 0.0) * 100);
                System.out.printf("%11.2f %%", checkValue(ratioEntity.getRoa(), 0.0) * 100);
                System.out.printf("%11.2f", checkValue(ratioEntity.getPe(), 0.0));
                System.out.printf("%11.2f", checkValue(ratioEntity.getPb(), 0.0));
                System.out.printf("%11.2f", checkValue(ratioEntity.getEps(), 0.0));
                System.out.printf("%11.2f", checkValue(ratioEntity.getCurrentRatio(), 0.0));
                System.out.printf("%11.2f", checkValue(ratioEntity.getCashRatio(), 0.0));
                System.out.printf("%11.2f", checkValue(ratioEntity.getQuickRatio(), 0.0));
                System.out.printf("%10.2f", checkValue(ratioEntity.getInterestCoverage(), 0.0));
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
