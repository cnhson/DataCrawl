package com.crawl.VietCap.run;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

import com.crawl.VietCap.controller.BusinessProfileRequest;
import com.crawl.VietCap.controller.TransactionRequest;
import com.crawl.VietCap.model.BusinessProfileEntity;
import com.crawl.VietCap.model.TransactionEntity;
import com.crawl.VietCap.util.ExcelUtil;
import com.crawl.VietCap.util.TextUtil;

public class Test {

    // public static void main(String[] args) throws InterruptedException {
    // TextUtil textUtil = new TextUtil("src/main/resources/tickerList.txt");
    // List<String> ticketList = textUtil.readFileAsListString();

    // String[] animationFrames = { "Fetching Tickers. ", "Fetching Tickers.. ",
    // "Fetching Tickers... " };
    // Integer i = 0;
    // System.out.println("\n\nStarting...\n");
    // // textUtil.readContentFromFile((line) -> {
    // // // ticketList.add(line);
    // // System.out.println(line);
    // // });

    // for (int j = 0; j < ticketList.size(); j++) {
    // // System.out.print("\r" + animationFrames[i]);
    // // i = (i + 1) % animationFrames.length;
    // System.out.println("\nCurrent: " + j + "/" + ticketList.size());
    // System.out.println("\nHere: " + ticketList.get(j));
    // System.out.print("\033[H\033[2J");
    // System.out.flush();
    // Thread.sleep(5);
    // }
    // System.out.print("Done");

    // textUtil.close();
    // }

    public static void main(String[] args) throws InterruptedException {
        Integer fileNameIndex = 1;
        ExcelUtil eu = new ExcelUtil();
        String filename = "test";
        TextUtil listOfTickerUtil = new TextUtil("src/main/resources/tickerList.txt");
        try {

            List<String> ticketList = listOfTickerUtil.readFileAsListString();
            System.out.println("\n\nHere: " + ticketList.get(921));

        } catch (Exception e) {
            eu.saveAndClose();
            System.out.println("Done!");
            listOfTickerUtil.close();
        }
    }
}
