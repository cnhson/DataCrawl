package com.crawl.executor;

import com.crawl.util.ConvertToCsvUtil;

public class ConvertExcelToCsv {

    public static void main(String[] args) throws Exception {

        ConvertToCsvUtil excelReaderUtil = new ConvertToCsvUtil();
        String filename = "vietcap_21_10_2024";
        excelReaderUtil.startConvertProcess(filename);

    }
}
