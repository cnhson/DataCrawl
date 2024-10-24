package com.crawl.executor;

import com.crawl.controller.VietCapGetAllTickerRequest;
import com.crawl.model.VietCapTickerEntity;
import com.crawl.util.TextUtil;

public class VietCapExportTickerList {

    public static void main(String[] args) throws InterruptedException {

        String[] animationFrames = { "Fetching Tickers. ", "Fetching Tickers..  ", "Fetching Tickers...   " };
        Integer i = 0;

        VietCapGetAllTickerRequest gatr = new VietCapGetAllTickerRequest();
        TextUtil textUtil = new TextUtil("src/main/resources/tickerList.txt");

        VietCapTickerEntity[] tickerList = gatr.crawlData();

        System.out.println("\n\nStarting...\n");
        // Thread.sleep(1000);
        textUtil.setAppendMode(false);
        for (VietCapTickerEntity ticker : tickerList) {
            System.out.print("\r" + animationFrames[i]);
            i = (i + 1) % animationFrames.length;
            if (ticker.getType().equals("STOCK"))
                textUtil.writeContentToFile(ticker.getSymbol());
        }

        textUtil.close();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("\nDone!");
    }
}
