package com.crawl;

import com.crawl.VietCap.controller.TransactionExcel;

//`import com.foxthehuman.VietCapCrawl.controller.TransactionController;

public class ExportExcel {
    public static void main(String[] args) {
        String inputSymbol = "VCI";
        Integer pageNum = 1;
        Integer limit = 100;
        String startDate = "2024-07-01";
        String endDate = "2100-01-01";
        TransactionExcel vct = new TransactionExcel();
        vct.crawlData(inputSymbol, pageNum, limit, startDate, endDate);
    }
}