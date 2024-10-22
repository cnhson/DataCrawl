package com.crawl.VietCap.util;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;

public class Test {
    

    public static void main(String[] args) {
        
      Integer[] list = new Integer[1];
      list[0] = 10;
      addValue(list);

      System.out.println(list[0]);
        // ExcelOpenUtil excelutil = new ExcelOpenUtil();
        // excelutil.setFileName("vietcap_1");
        // excelutil.openWorkSheet();
        // excelutil.saveAndClose();
      // try {
      //   String path = "D:\\GithubProjects\\DataCrawl\\src\\main\\resources\\";
      //   String filename = "vietcap_21_10_2024.csv";
      //    try (CSVReader reader = new CSVReader(new FileReader(path+filename))) {
      //       List<String[]> row = reader.readAll();
      //       row.forEach(x -> 
      //       System.out.println(Arrays.toString(x)));
      //   }
      // } catch (Exception e) {
      //   System.err.println(e.getMessage());
      // }
    }

    static public void addValue(Integer[] list)
    {
     list[0] +=1;
    }
}
