package com.crawl.executor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import com.crawl.controller.CafeFTransactionRequest;
import com.crawl.controller.VietCapBusinessProfileRequest;
import com.crawl.controller.VietCapICBRequest;
import com.crawl.model.CafeFTransactionEntity;
import com.crawl.model.VietCapBusinessProfileEntity;
import com.crawl.model.VietCapICBEntity;
import com.crawl.util.TextUtil;
import com.crawl.util.WriteCsvUtil;

//`import com.foxthehuman.VietCapCrawl.controller.TransactionController;

public class VietCapExportICBCsv {

    public VietCapExportICBCsv() {

    }

    public void execute() throws InterruptedException {
        WriteCsvUtil wcu = new WriteCsvUtil();
        VietCapICBRequest vicbr = new VietCapICBRequest();
        // TextUtil lastestTickerFetchUtil = new
        // TextUtil("src/main/resources/lastestTickerFetchList.txt");
        // TextUtil listOfTickerUtil = new
        // TextUtil("src/main/resources/tickerList.txt");
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String currentDate = LocalDate.now().format(formatter);
            String filename;

            String[] baseHeadersList = new String[] { "ICB_ID", "Report_Date", "Items_Name", "Items_Value", };
            String[] extendHeaderList = new String[] { "ICBName", "PercentPriceChange1Day", "PercentPriceChange1Week",
                    "PercentPriceChange1Month", "Pe", "Pb", "RevenueGrowth", "AssetGrowth", "OwnerEquityGrowth", "Roa",
                    "Roe", "NetProfitMargin", "GrossProfitMargin", "DebtRatio", "Ev" };

            String projectDir = System.getProperty("user.dir");
            String exportPath = projectDir + "\\src\\main\\resources\\export_csv\\";

            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

            filename = currentDateTime + "_ICB.csv";

            wcu.setFile(exportPath, filename, baseHeadersList);

            List<VietCapICBEntity> icbList = vicbr.crawlData();

            icbList.sort(Comparator.comparing(VietCapICBEntity::getIcbCode));

            for (VietCapICBEntity entity : icbList) {

                String currentICB = entity.getNameAsCode();

                Object[] extendValueList = new Object[] { entity.getName(), entity.getPercentPriceChange1Day(),
                        entity.getPercentPriceChange1Week(), entity.getPercentPriceChange1Month(), entity.getPe(),
                        entity.getPb(), entity.getRevenueGrowth(), entity.getAssetGrowth(), entity.getOeGrowth(),
                        entity.getRoa(), entity.getRoe(), entity.getNetProfitMargin(), entity.getGrossProfitMargin(),
                        entity.getDebtRatio(), entity.getEv() };

                for (int i = 0; i < extendHeaderList.length; i++) {

                    wcu.writeLine(new String[] { currentICB, currentDate, extendHeaderList[i],
                            extendValueList[i].toString() });
                }
            }

            System.out.flush();
            wcu.closeCSV();
            System.out.println("\nDone fetching all!");

        } catch (Exception e) {
            wcu.closeCSV();
            System.out.println(e.getMessage());
            System.out.println("Error!");

        }
    }
}