package com.crawl.VietCap.run;

import com.crawl.VietCap.controller.GetAllTickerRequest;
import com.crawl.VietCap.model.TickerEntity;
import com.crawl.VietCap.util.TextUtil;

public class ExportTickerList {

    public static void main(String[] args) throws InterruptedException {

        String[] animationFrames = { "Fetching Tickers. ", "Fetching Tickers..  ", "Fetching Tickers...   " };
        Integer i = 0;

        GetAllTickerRequest gatr = new GetAllTickerRequest();
        TextUtil textUtil = new TextUtil("src/main/resources/tickerList.txt");

        TickerEntity[] tickerList = gatr.crawlData();

        System.out.println("\n\nStarting...\n");
        // Thread.sleep(1000);
        textUtil.setAppendMode(false);
        for (TickerEntity ticker : tickerList) {
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
