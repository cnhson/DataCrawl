package com.crawl.run;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import com.crawl.model.TransactionEntity;
import com.crawl.util.ReadCsvUtil;
import com.crawl.util.WriteCsvUtil;

public class test {

    // private static Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        String pathFile = "src/main/resources/export_csv/";
        // ReadCsvUtil rcu = new ReadCsvUtil(pathFile, "2024-10-23_14-50-37_all.csv");
        ReadCsvUtil rcu = new ReadCsvUtil(pathFile, "2024-10-24_15-23-37_all.csv");

        WriteCsvUtil wcu = new WriteCsvUtil();
        String[] headers = new String[] { "Product_id", "Report_time", "Item_name", "Item_value" };
        String[] specialCase = new String[] { "EPS", "PE", "PB", "EV", "IssueShare" };
        wcu.setFile("src/main/resources/export_csv/", "extracted_2024-10-24_15-23-37(part2)_all.csv", headers);
        try {

            rcu.readLines(row -> {
                if (!Arrays.stream(specialCase).anyMatch(row[2]::equals)) {
                    wcu.writeLine(row);
                }
            });

            rcu.close();
            wcu.closeCSV();
        } catch (Exception e) {
            System.err.println(("Error: " + e.getMessage()));
            rcu.close();
            wcu.closeCSV();
        }
        // System.out.println("\nHere: " + list.size());

        // try {
        // String path =
        // test.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        // System.out.println(path);

        // } catch (Exception e) {
        // System.err.println(e.getMessage());
        // }
        // String projectDir = System.getProperty("user.dir");

        // // Print the project directory
        // System.out.println("Current project directory: " + projectDir +
        // "\\src\\main\\java\\com\\crawl\\");

        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        // Long past = LocalDate.parse("10/23/2024", formatter).toEpochDay();
        // Long now = LocalDate.now().toEpochDay();
        // System.out.println(past + " " + now);
        // System.out.println(past.compareTo(now));
        // String symbol = "VNM";
        // String startDate = "2024-07-01";
        // String endDate = "2024-07-20";
        // Integer limit = 200;
        // Integer pageNum = 1;
        // List<CafeFTransactionEntity> list = new
        // CafeFTransactionRequest().crawlData(symbol, startDate, endDate, pageNum,
        // limit);

        // for (CafeFTransactionEntity cafeFTransactionEntity : list) {
        // System.out.println(cafeFTransactionEntity.getTradingDate());
        // System.out.println(cafeFTransactionEntity.getTradingLocalDate());
        // System.out.println("\n");
        // // System.out.println(cafeFTransactionEntity.getPriceChangeAsInteger());
        // }
    }
}
