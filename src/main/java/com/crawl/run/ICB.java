package com.crawl.run;

import com.crawl.executor.CafeFExportTransactionCsv;
import com.crawl.executor.VietCapExportICBCsv;

public class ICB {

    public static void main(String[] args) throws Exception {

        VietCapExportICBCsv vietCapExportICBCsv = new VietCapExportICBCsv();
        vietCapExportICBCsv.execute();
    }
}
