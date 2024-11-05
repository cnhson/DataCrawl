package com.crawl.executor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.crawl.controller.VietCapFinancalAnalystRequest;
import com.crawl.model.VietCapFinancalAnalystEntity;
import com.crawl.util.TextUtil;
import com.crawl.util.WriteCsvUtil;

//`import com.foxthehuman.VietCapCrawl.controller.TransactionController;

public class VietCapExportFinancalAnalystCsv {

    public static void main(String[] args) throws InterruptedException {

        WriteCsvUtil wcu = new WriteCsvUtil();
        VietCapFinancalAnalystRequest bpr = new VietCapFinancalAnalystRequest();
        TextUtil listOfTickerUtil = new TextUtil("src/main/resources/tickerList.txt");
        try {

            List<String> ticketList;
            String path = "D:\\GithubProjects\\DataCrawl\\src\\main\\resources\\export_csv\\";
            String filename;
            Boolean isSingleFetch;
            String[] baseHeadersList = new String[] { "product_id", "report_length", "report_year", "items_name",
                    "items_value", };
            String[] extendHeaderList = new String[] { "Revenue", "RevenueGrowth", "NetProfit", "NetProfitGrowth",
                    "ROE", "ROIC", "ROA", "EV", "IssueShare", "EPS", "PE", "PB", "Ebit" };
            //
            isSingleFetch = false;
            //
            if (isSingleFetch) {
                filename = "fa_vnm.csv";
                ticketList = Arrays.asList("VNM");
            } else {
                filename = "fa_all.csv";
                ticketList = listOfTickerUtil.readFileAsListString();
            }
            wcu.setFile(path, filename, baseHeadersList);
            for (int j = 0; j < ticketList.size(); j++) {
                Thread.sleep(300);
                String symbol = ticketList.get(j);
                System.out.println("\nCurrent symbol: " + symbol);
                List<VietCapFinancalAnalystEntity> FinancalAnalystList = bpr.crawlData(symbol);
                // Revert the list (from oldest to lastest)
                Collections.reverse(FinancalAnalystList);
                for (VietCapFinancalAnalystEntity entity : FinancalAnalystList) {
                    Object[] extendValueList = new Object[] { entity.getRevenue(), entity.getRevenueGrowth(),
                            entity.getNetProfit(), entity.getNetProfitGrowth(), entity.getRoe(), entity.getRoic(),
                            entity.getRoa(), entity.getEv(), entity.getIssueShare(), entity.getEps(), entity.getPe(),
                            entity.getPb(), entity.getEbit() };
                    for (int i = 0; i < extendHeaderList.length; i++) {
                        wcu.writeLine(new Object[] { symbol, entity.getLengthReport(), entity.getYearReport(),
                                extendHeaderList[i], extendValueList[i] });
                    }
                }
                System.out.println("Done: " + (j + 1) + "/" + ticketList.size());
                System.out.flush();
            }

            System.out.println("\nDone fetching all!");
            listOfTickerUtil.close();

        } catch (Exception e) {
            wcu.closeCSV();
            System.out.println(e.getMessage());
            System.out.println("Error!");
            listOfTickerUtil.close();

        }
    }
}