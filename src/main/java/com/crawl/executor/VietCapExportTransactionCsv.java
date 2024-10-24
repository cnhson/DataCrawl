package com.crawl.executor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import com.crawl.controller.VietCapBusinessProfileRequest;
import com.crawl.controller.VietCapTransactionRequest;
import com.crawl.model.VietCapBusinessProfileEntity;
import com.crawl.model.VietCapTransactionEntity;
import com.crawl.util.ExcelWriteUtil;
import com.crawl.util.TextUtil;
import com.crawl.util.WriteCsvUtil;

//`import com.foxthehuman.VietCapCrawl.controller.TransactionController;

public class VietCapExportTransactionCsv {
    private static BigDecimal prevAverageGain = BigDecimal.ZERO;
    private static BigDecimal prevAverageLoss = BigDecimal.ZERO;
    private static BigDecimal prevEMA12 = BigDecimal.ZERO;
    private static BigDecimal prevEMA26 = BigDecimal.ZERO;

    public static void main(String[] args) throws InterruptedException {
        WriteCsvUtil wcu = new WriteCsvUtil();
        VietCapTransactionRequest vct = new VietCapTransactionRequest();
        VietCapBusinessProfileRequest bpr = new VietCapBusinessProfileRequest();
        TextUtil lastestTickerFetchUtil = new TextUtil("src/main/resources/lastestTickerFetchList.txt");
        TextUtil listOfTickerUtil = new TextUtil("src/main/resources/tickerList.txt");
        try {

            Integer averageTotalVolumeDays = 10;
            Integer RSI_Days = 14;
            Integer MA_Days = 10;
            Integer MACD_Min_Days = 12;
            Integer MACD_Max_Days = 26;

            List<String> ticketList;
            String filename;
            Boolean isSingleFetch;
            Integer pageNum = 1;
            Integer limit = 5000;
            String[] baseHeadersList = new String[] { "Product_ID", "Report_Date", "Items_Name", "Items_Value", };
            String[] extendHeaderList = new String[] { "Open_Price", "Close_Price", "Highest_Price", "Lowest_Price",
                    "Total_Match_Volume", "Total_Match_Value", "Total_Value", "Total_Volume", "Average_Total_Value_10",
                    "Average_Matching_Total_Value_10", "RSI_14", "Prev_AVG", "Prev_AVL", "MA_10", "MACD", "Prev_SMA12",
                    "Prev_SMA26", "EV", "IssueShare", "EPS", "PE", "PB" };
            String path = "D:\\GithubProjects\\DataCrawl\\src\\main\\resources\\";

            //
            String startDate = "2000-01-01";
            String endDate = "2100-01-01";
            isSingleFetch = true;
            //
            if (isSingleFetch) {
                String symbol = "VNM";
                filename = symbol + "_testcsv.csv";
                ticketList = Arrays.asList(symbol);
            } else {
                filename = "all_";
                ticketList = listOfTickerUtil.readFileAsListString();
            }
            wcu.setFile(path, filename, baseHeadersList);

            for (int j = 0; j < ticketList.size(); j++) {
                Thread.sleep(300);
                String symbol = ticketList.get(j);
                System.out.println("\nCurrent symbol: " + symbol);

                List<VietCapTransactionEntity> transList = vct.crawlData(symbol, pageNum, limit, startDate, endDate);
                List<VietCapBusinessProfileEntity> businessProfileList = bpr.crawlData(symbol);

                // Revert the list (from oldest to lastest)
                Collections.reverse(businessProfileList);
                // Ascending sort on datetime
                transList.sort(Comparator.comparingLong(VietCapTransactionEntity::getLongTradingDate));

                Deque<Integer> averageTotalVolumeArray = new ArrayDeque<>(averageTotalVolumeDays);
                Deque<Integer> averageTotalMatchVolumeArray = new ArrayDeque<>(averageTotalVolumeDays);
                Deque<Integer> rsiArray = new ArrayDeque<>(averageTotalVolumeDays);
                Deque<Integer> MA_array = new ArrayDeque<>(averageTotalVolumeDays);
                Deque<Integer> MACD_array = new ArrayDeque<>(MACD_Max_Days);
                final String[] latestDateFetchHolder = { null };

                for (VietCapTransactionEntity entity : transList) {

                    VietCapBusinessProfileEntity foundBPE = findProfileByDate(businessProfileList,
                            entity.getFormatStringTradingDate());

                    String averageTotalVolumeValue = calculateATV(averageTotalVolumeArray, averageTotalVolumeDays,
                            entity.getTotalVolume());
                    String averageTotalMatchVolumeValue = calculateATV(averageTotalMatchVolumeArray,
                            averageTotalVolumeDays, entity.getTotalMatchVolume());
                    String RSI_Value = calculateRSI(rsiArray, RSI_Days, entity.getPriceChange());
                    String MA_Value = calculateMA(MA_array, MA_Days, entity.getClosePrice());

                    String MACD_Value = calculateMACD(MACD_array, MACD_Min_Days, MACD_Max_Days, entity.getClosePrice());

                    Object[] extendValueList = new Object[] { entity.getOpenPrice(), entity.getClosePrice(),
                            entity.getHighestPrice(), entity.getLowestPrice(), entity.getTotalMatchVolume(),
                            entity.getTotalMatchValue(), entity.getTotalValue(), entity.getTotalVolume(),
                            averageTotalVolumeValue, averageTotalMatchVolumeValue, RSI_Value,
                            prevAverageGain.compareTo(new BigDecimal(0)) != 0 ? prevAverageGain.toString() : "N/A",
                            prevAverageLoss.compareTo(new BigDecimal(0)) != 0 ? prevAverageLoss.toString() : "N/A",
                            MA_Value, MACD_Value,
                            prevEMA12.compareTo(new BigDecimal(0)) != 0 ? prevEMA12.toString() : "N/A",
                            prevEMA26.compareTo(new BigDecimal(0)) != 0 ? prevEMA26.toString() : "N/A",
                            foundBPE.getEv() != null ? foundBPE.getEv().toString() : "N/A",
                            foundBPE.getIssueShare() != null ? foundBPE.getIssueShare().toString() : "N/A",
                            foundBPE.getEps() != null ? foundBPE.getEps().toString() : "N/A",
                            foundBPE.getPe() != null ? foundBPE.getPe().toString() : "N/A",
                            foundBPE.getPb() != null ? foundBPE.getPb().toString() : "N/A" };

                    for (int i = 0; i < extendHeaderList.length; i++) {
                        wcu.writeLine(new String[] { symbol, entity.getFormatStringTradingDate(), extendHeaderList[i],
                                extendValueList[i].toString() });
                    }

                    latestDateFetchHolder[0] = entity.getFormatStringTradingDate();
                }
                ;
                System.out.println("Done: " + (j + 1) + "/" + ticketList.size());
                System.out.flush();
                lastestTickerFetchUtil.setAppendMode(true);
                lastestTickerFetchUtil.writeContentToFile(symbol + "," + latestDateFetchHolder[0]);
            }
            wcu.closeCSV();
            System.out.println("\nDone fetching all!");
            listOfTickerUtil.close();
            lastestTickerFetchUtil.close();
        } catch (Exception e) {
            wcu.closeCSV();
            System.out.println(e.getMessage());
            System.out.println("Error!");
            listOfTickerUtil.close();
            lastestTickerFetchUtil.close();
        }
    }

    private static String calculateRSI(Deque<Integer> dequeArray, Integer dayAmount, Integer priceChange) {
        try {
            String result = "N/A";
            dequeArray.add(priceChange);
            if (dequeArray.size() == dayAmount) {

                BigDecimal averageGain = new BigDecimal(0);
                BigDecimal averageLoss = new BigDecimal(0);
                Boolean isPreviousExist = prevAverageGain.compareTo(BigDecimal.ZERO) != 0;
                if (isPreviousExist) {
                    Integer currentGain = priceChange > 0 ? priceChange : 0;
                    Integer currentLoss = priceChange < 0 ? -priceChange : 0;

                    // Calculate average gain/loss using exponential smoothing
                    averageGain = (prevAverageGain.multiply(BigDecimal.valueOf(dayAmount - 1))
                            .add(BigDecimal.valueOf(currentGain))).divide(BigDecimal.valueOf(dayAmount), 10,
                                    RoundingMode.HALF_UP);
                    averageLoss = (prevAverageLoss.multiply(BigDecimal.valueOf(dayAmount - 1))
                            .add(BigDecimal.valueOf(currentLoss))).divide(BigDecimal.valueOf(dayAmount), 10,
                                    RoundingMode.HALF_UP);
                } else {
                    BigDecimal totalGain = BigDecimal
                            .valueOf(dequeArray.stream().filter(value -> value > 0).mapToInt(Integer::intValue).sum());
                    BigDecimal totalLoss = BigDecimal
                            .valueOf(dequeArray.stream().filter(value -> value < 0).mapToInt(Integer::intValue).sum())
                            .abs();
                    averageGain = totalGain.divide(BigDecimal.valueOf(dayAmount), 10, RoundingMode.HALF_UP);
                    averageLoss = totalLoss.divide(BigDecimal.valueOf(dayAmount), 10, RoundingMode.HALF_UP);
                }

                BigDecimal rs = averageGain.divide(averageLoss, 10, RoundingMode.HALF_UP);
                BigDecimal rsi = BigDecimal.valueOf(100)
                        .subtract(BigDecimal.valueOf(100).divide(BigDecimal.ONE.add(rs), 10, RoundingMode.HALF_UP));

                // Checking RSI is under threshhold (70)
                if (rsi.compareTo(new BigDecimal(70)) < 0) {
                    result = String.valueOf(rsi);
                    prevAverageGain = averageGain;
                    prevAverageLoss = averageLoss;
                }

                dequeArray.pop();
            }
            return result;
        } catch (Exception e) {
            // System.err.println("Error in RSI: " + e.getMessage());
            return "N/A";
        }
    }

    private static String calculateMACD(Deque<Integer> dequeArray, Integer minDayAmount, Integer maxDayAmount,
            Integer closedPrice) {
        try {
            /**
             * Calculate EMA with the formula: EMA = (CurrentPrice * alpha) + (PreviousEMA *
             * (1 - alpha))
             **/
            BigDecimal tempEMA12 = prevEMA12;
            BigDecimal tempEMA26 = prevEMA26;
            String result;
            dequeArray.add(closedPrice);

            if (dequeArray.size() >= minDayAmount) {
                // create initial EMA12
                if (prevEMA12.compareTo(new BigDecimal(0)) == 0) {
                    List<Integer> listArray = dequeArray.stream().collect(Collectors.toList());
                    Integer sum = 0;
                    for (int i = 0; i < 12; i++) {
                        sum += listArray.get(i);
                    }

                    tempEMA12 = new BigDecimal((dequeArray.stream().mapToInt(Integer::intValue).sum()))
                            .divide(new BigDecimal(minDayAmount), 10, RoundingMode.HALF_UP);

                } else {
                    BigDecimal alpha12 = new BigDecimal(0.15384615384);
                    BigDecimal oneMinusAlpha12 = BigDecimal.ONE.subtract(alpha12);
                    tempEMA12 = (new BigDecimal(closedPrice).multiply(alpha12)).add(prevEMA12.multiply(oneMinusAlpha12))
                            .setScale(10, RoundingMode.CEILING);
                }
            }

            // when 26 days have passed
            if (dequeArray.size() == maxDayAmount) {

                if (prevEMA26.compareTo(new BigDecimal(0)) == 0) {
                    tempEMA26 = new BigDecimal((dequeArray.stream().mapToInt(Integer::intValue).sum()))
                            .divide(new BigDecimal(maxDayAmount), 10, RoundingMode.HALF_UP);
                } else {
                    BigDecimal alpha26 = new BigDecimal(0.07407407407);
                    BigDecimal oneMinusAlpha26 = BigDecimal.ONE.subtract(alpha26);
                    tempEMA26 = (new BigDecimal(closedPrice).multiply(alpha26)).add(prevEMA26.multiply(oneMinusAlpha26))
                            .setScale(10, RoundingMode.CEILING);
                }

                prevEMA12 = tempEMA12;
                prevEMA26 = tempEMA26;
                result = String.valueOf(tempEMA12.subtract(tempEMA26));
                dequeArray.pop();
            } else {
                result = "N/A";
            }
            return result;
        } catch (Exception e) {
            // System.err.println("Error in MACD: " + e.getMessage());
            return "N/A";
        }
    }

    // Calculate (x) days average trading volume
    private static String calculateATV(Deque<Integer> dequeArray, Integer dayAmount, Integer totalVolume) {
        return calculateAverageSum(dequeArray, dayAmount, totalVolume, "ATV");
    }

    private static String calculateMA(Deque<Integer> dequeArray, Integer dayAmount, Integer closedPrice) {
        return calculateAverageSum(dequeArray, dayAmount, closedPrice, "MA");
    }

    private static String calculateAverageSum(Deque<Integer> dequeArray, Integer dayAmount, Integer value,
            String type) {
        try {
            String result;
            dequeArray.add(value);
            if (dequeArray.size() == dayAmount) {
                result = String.valueOf((dequeArray.stream().mapToInt(Integer::intValue).sum() / dayAmount));
                dequeArray.pop();
            } else
                result = "N/A";
            return result;
        } catch (Exception e) {
            // System.err.println("Error in " + type + ": " + e.getMessage());
            return "N/A";
        }
    }

    private static VietCapBusinessProfileEntity findProfileByDate(
            List<VietCapBusinessProfileEntity> businessProfileArray, String dateString) {
        try {
            // Parse the input date string to extract the year and month

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(dateString, formatter);

            int year = date.getYear();
            int month = date.getMonthValue();
            int lengthReport = (month - 1) / 3 + 1; // Calculate the quarter

            // Find the matching BusinessProfileEntity in the list
            for (VietCapBusinessProfileEntity profile : businessProfileArray) {
                if (profile.getYearReport() == year && profile.getLengthReport() == lengthReport) {
                    return profile;
                }
            }

            // It there is no data exist
            VietCapBusinessProfileEntity nullProfile = new VietCapBusinessProfileEntity();
            nullProfile.setIssueShare(null);
            nullProfile.setEv(null);
            nullProfile.setPb(null);
            nullProfile.setPe(null);
            nullProfile.setEps(null);

            return nullProfile;

        } catch (Exception e) {
            System.err.println("Invalid date format: " + e.getMessage());
            return null; // Return null if no match is found
        }
    }
}