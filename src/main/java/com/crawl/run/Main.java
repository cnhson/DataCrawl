package com.crawl.run;

import com.crawl.executor.CafeFExportTransactionCsv;

public class Main {

    public static void main(String[] args) throws Exception {

        String startDate = "2000-01-01";
        String endDate = "2024-10-23";
        String symbol = "";
        String exportPath = "";
        Integer startIndex = 666;
        CafeFExportTransactionCsv cafeETC = new CafeFExportTransactionCsv();
        cafeETC.execute(startDate, endDate, symbol, exportPath, startIndex);
    }
}
