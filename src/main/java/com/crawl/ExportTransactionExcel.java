package com.crawl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;

import com.crawl.VietCap.controller.TransactionRequest;
import com.crawl.VietCap.model.TransactionEntity;
import com.crawl.VietCap.util.ExcelUtil;

//`import com.foxthehuman.VietCapCrawl.controller.TransactionController;

public class ExportTransactionExcel {
    public static void main(String[] args) {

        String[] vn30SymbolList = { "VCI", "VHM", "VRE", "VNM", "GAS", "SAB", "BID",
                "CTG", "BVH", "STB", "KBC",
                "NVL", "CII", "MBB", "PNJ", "DHG", "DPM", "HCM", "BMP", "REE", "NT2", "BMI",
                "EIB" };
        // String[] vn30SymbolList = { "VCI", "VHM" };

        Integer averageTotalVolumeDays = 10;
        Integer rsiDays = 14;
        Integer maDays = 10;
        Integer macdDays = 26;

        Deque<Integer> averageTotalVolumeArray = new ArrayDeque<>(averageTotalVolumeDays);
        Deque<Integer> averageTotalMatchVolumeArray = new ArrayDeque<>(averageTotalVolumeDays);
        Deque<Integer> movingAverageArray = new ArrayDeque<>(averageTotalVolumeDays);
        Deque<Integer> movingAverageConvergenceDivergenceArray = new ArrayDeque<>(averageTotalVolumeDays);

        Deque<Integer> rsiArray = new ArrayDeque<>(averageTotalVolumeDays);

        String[] headersList = new String[] {
                "Trading_Date", "Open_Price", "Close_Price",
                "Highest_Price", "Lowest_Price", "Total_Match_Volume", "Total_Match_Value",
                "Total_Value", "Total_Volume", "10Days_Average_Trading_Volume", "10Days_Average_Match_Trading_Volume",
                "RSI_14", "MA_10"
        };
        String filename = "vietcap_transaction";
        Integer pageNum = 1;
        Integer limit = 5000;
        String startDate = "2000-01-01";
        String endDate = "2100-01-01";

        ExcelUtil eu = new ExcelUtil();
        TransactionRequest vct = new TransactionRequest();

        eu.setFileName(filename);
        // eu.addNewSymbolSheet("VCI");
        // eu.setHeadersConfig(headersList);
        // eu.writeContentTest(headersList);
        // eu.addNewSymbolSheet("VNM");
        // eu.setHeadersConfig(headersList);
        for (String symbol : vn30SymbolList) {
            eu.addNewSymbolSheet(symbol, headersList);
            // eu.setHeadersConfig(headersList);
            TransactionEntity[] transList = vct.crawlData(filename, symbol, pageNum,
                    limit, startDate, endDate);

            Arrays.sort(transList,
                    Comparator.comparingLong(TransactionEntity::getLongTradingDate));

            // for (TransactionEntity entity : transList) {
            // System.out.println(entity.getFormatStringTradingDate());
            // }
            eu.loopWriteContent(transList, (row, entity) -> {

                String averageTotalVolumeValue = calculateATV(averageTotalVolumeArray,
                        averageTotalVolumeDays,
                        entity.getTotalVolume());
                String averageTotalMatchVolumeValue = calculateATV(averageTotalMatchVolumeArray, averageTotalVolumeDays,
                        entity.getTotalMatchVolume());
                String rsiValue = calculateRSI(rsiArray, rsiDays, entity.getPriceChange());
                String maValue = calculateMA(movingAverageArray, maDays,
                        entity.getClosePrice());

                row.createCell(0).setCellValue(entity.getFormatStringTradingDate());
                row.createCell(1).setCellValue(entity.getOpenPrice());
                row.createCell(2).setCellValue(entity.getClosePrice());
                row.createCell(3).setCellValue(entity.getHighestPrice());
                row.createCell(4).setCellValue(entity.getLowestPrice());
                row.createCell(5).setCellValue(entity.getTotalMatchVolume());
                row.createCell(6).setCellValue(entity.getTotalMatchValue());
                row.createCell(7).setCellValue(entity.getTotalValue());
                row.createCell(8).setCellValue(entity.getTotalVolume());
                row.createCell(9).setCellValue(averageTotalVolumeValue);
                row.createCell(10).setCellValue(averageTotalMatchVolumeValue);
                row.createCell(11).setCellValue(rsiValue);
                row.createCell(12).setCellValue(maValue);

            });
        }
        eu.saveAndClose();
    }

    private static String calculateRSI(Deque<Integer> dequeArray, Integer dayAmount, Integer priceChange) {
        try {
            String result;
            dequeArray.add(priceChange);
            if (dequeArray.size() == dayAmount) {

                BigDecimal totalGain = BigDecimal.valueOf(
                        dequeArray.stream().filter(value -> value > 0).mapToInt(Integer::intValue).sum());

                BigDecimal averageGain = totalGain.divide(BigDecimal.valueOf(dayAmount), 10, RoundingMode.HALF_UP);

                BigDecimal totalLoss = BigDecimal.valueOf(
                        dequeArray.stream().filter(value -> value < 0).mapToInt(Integer::intValue).sum()).abs();

                BigDecimal averageLoss = totalLoss.divide(BigDecimal.valueOf(dayAmount), 10, RoundingMode.HALF_UP);

                BigDecimal rs = averageGain.divide(averageLoss, 10, RoundingMode.HALF_UP);

                BigDecimal rsi = BigDecimal.valueOf(100)
                        .subtract(BigDecimal.valueOf(100).divide(BigDecimal.ONE.add(rs), 10, RoundingMode.HALF_UP));

                // System.out.println(
                // "Average Gain: " + averageGain + "\nAverage Loss: " + averageLoss + "\nRS: "
                // + rs + "\nRSI: "
                // + rsi + "\n\n");
                result = String.valueOf(rsi);
                dequeArray.pop();
            } else
                result = "0";
            return result;
        } catch (Exception e) {
            System.err.println("Error in RSI: " + e.getMessage());
            return "0";
        }
    }

    private static String calculateMACD(Deque<Integer> dequeArray, Integer dayAmount, Integer closedPrice) {
        try {
            String result;
            dequeArray.add(closedPrice);
            if (dequeArray.size() == dayAmount) {
                // Smoothing Factor
                BigDecimal alpha12 = new BigDecimal(0.15384615384);
                BigDecimal alpha26 = new BigDecimal(0.07407407407);
                // Calculate EMA12

                BigDecimal ema12 = new BigDecimal((dequeArray.stream().mapToInt(Integer::intValue).sum()));
                // Calculate EMA26
                result = String.valueOf((dequeArray.stream().mapToInt(Integer::intValue).sum() / dayAmount));
                dequeArray.pop();
            } else
                result = "0";
            return result;
        } catch (Exception e) {
            System.err.println("Error in MACD: " + e.getMessage());
            return "0";
        }
    }

    // Calculate (x) days average trading volume
    private static String calculateATV(Deque<Integer> dequeArray, Integer dayAmount, Integer totalVolume) {
        return calculateAverageSum(dequeArray, dayAmount, totalVolume, "ATV");
    }

    private static String calculateMA(Deque<Integer> dequeArray, Integer dayAmount,
            Integer closedPrice) {
        return calculateAverageSum(dequeArray, dayAmount, closedPrice, "MA");
    }

    private static String calculateAverageSum(Deque<Integer> dequeArray, Integer dayAmount,
            Integer value, String type) {
        try {
            String result;
            dequeArray.add(value);
            if (dequeArray.size() == dayAmount) {
                result = String.valueOf((dequeArray.stream().mapToInt(Integer::intValue).sum() / dayAmount));
                dequeArray.pop();
            } else
                result = "0";
            return result;
        } catch (Exception e) {
            System.err.println("Error in " + type + ": " + e.getMessage());
            return "0";
        }
    }

}