package com.crawl.VietCap.run;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import com.crawl.VietCap.controller.BusinessProfileRequest;
import com.crawl.VietCap.controller.TransactionRequest;
import com.crawl.VietCap.model.BusinessProfileEntity;
import com.crawl.VietCap.model.TransactionEntity;
import com.crawl.VietCap.util.ExcelUtil;
import com.crawl.VietCap.util.TextUtil;

//`import com.foxthehuman.VietCapCrawl.controller.TransactionController;

public class ExportTransactionExcel_Single_Ticker {
    public static void main(String[] args) throws InterruptedException {
        ExcelUtil eu = new ExcelUtil();
        TransactionRequest vct = new TransactionRequest();
        BusinessProfileRequest bpr = new BusinessProfileRequest();
        TextUtil lastestTickerFetchUtil = new TextUtil("src/main/resources/lastestTickerFetchList.txt");
        TextUtil listOfTickerUtil = new TextUtil("src/main/resources/tickerList.txt");
        try {

            Integer averageTotalVolumeDays = 10;
            Integer RSI_Days = 14;
            Integer MA_Days = 10;
            Integer MACD_Days = 26;

            String[] extendHeaderList = new String[] { "Open_Price", "Close_Price",
                    "Highest_Price", "Lowest_Price", "Total_Match_Volume", "Total_Match_Value",
                    "Total_Value", "Total_Volume", "Average_Total_Value_10", "Average_Matching_Total_Value_10",
                    "RSI_14", "MA_10", "MACD", "EV", "IssueShare", "EPS", "PE", "PB"
            };

            String filename = "vietcap_";
            Integer fileNameIndex = 31;

            Integer pageNum = 1;
            Integer limit = 5000;

            // String startDate = "2000-01-01";
            String startDate = "2000-01-01";
            String endDate = "2100-01-01";
            // Boolean continueFetch = true;

            List<String> ticketList = listOfTickerUtil.readFileAsListString();
            eu.createWorkSheet();
            eu.setFileName(filename + fileNameIndex);

            for (int j = 922; j < ticketList.size(); j++) {
                String symbol = ticketList.get(j);
                List<TransactionEntity> transList = vct.crawlData(symbol, pageNum,
                        limit, startDate, endDate);
                List<BusinessProfileEntity> businessProfileList = bpr.crawlData(symbol);

                // Revert the list (from oldest to lastest)
                Collections.reverse(businessProfileList);
                // Ascending sort on datetime
                transList.sort(Comparator.comparingLong(TransactionEntity::getLongTradingDate));

                Deque<Integer> averageTotalVolumeArray = new ArrayDeque<>(averageTotalVolumeDays);
                Deque<Integer> averageTotalMatchVolumeArray = new ArrayDeque<>(averageTotalVolumeDays);
                Deque<Integer> MA_array = new ArrayDeque<>(averageTotalVolumeDays);
                Deque<Integer> MACD_array = new ArrayDeque<>(MACD_Days);
                BigDecimal prevSMA12 = new BigDecimal(0);
                BigDecimal prevSMA26 = new BigDecimal(0);
                BigDecimal[] prevArray = new BigDecimal[2];
                prevArray[0] = prevSMA12;
                prevArray[1] = prevSMA26;

                final String[] latestDateFetchHolder = { null };

                Deque<Integer> rsiArray = new ArrayDeque<>(averageTotalVolumeDays);

                for (TransactionEntity entity : transList) {

                    BusinessProfileEntity foundBPE = findProfileByDate(businessProfileList,
                            entity.getFormatStringTradingDate());

                    String averageTotalVolumeValue = calculateATV(averageTotalVolumeArray,
                            averageTotalVolumeDays,
                            entity.getTotalVolume());
                    String averageTotalMatchVolumeValue = calculateATV(averageTotalMatchVolumeArray,
                            averageTotalVolumeDays,
                            entity.getTotalMatchVolume());
                    String RSI_Value = calculateRSI(rsiArray, RSI_Days, entity.getPriceChange());
                    String MA_Value = calculateMA(MA_array, MA_Days,
                            entity.getClosePrice());

                    String MACD_Value = calculateMACD(MACD_array, prevArray, MACD_Days,
                            entity.getClosePrice());

                    Object[] extendValueList = new Object[] { entity.getOpenPrice(), entity.getClosePrice(),
                            entity.getHighestPrice(), entity.getLowestPrice(),
                            entity.getTotalMatchVolume(), entity.getTotalMatchValue(), entity.getTotalValue(),
                            entity.getTotalVolume(), averageTotalVolumeValue,
                            averageTotalMatchVolumeValue, RSI_Value, MA_Value, MACD_Value, foundBPE.getEv(),
                            foundBPE.getIssueShare(), foundBPE.getEps(), foundBPE.getPe(), foundBPE.getPb() };

                    for (int i = 0; i < extendHeaderList.length; i++) {
                        if (eu.getRowIndex() == 1048575) {
                            // Save and close the current file
                            eu.saveAndClose();
                            eu.createWorkSheet();
                            fileNameIndex++;
                            eu.setFileName(filename + fileNameIndex);
                            eu.resetRowIndex();
                        }
                        eu.createRow(eu.getRowIndex());
                        eu.writeCellContent(0, symbol);
                        eu.writeCellContent(1, entity.getFormatStringTradingDate());
                        eu.writeCellContent(2, extendHeaderList[i]);
                        eu.writeCellContent(3, extendValueList[i]);
                        eu.increaseRowIndex();
                    }

                    latestDateFetchHolder[0] = entity.getFormatStringTradingDate();
                }
                ;
                System.out.println("\nCurrent: " + j + "/" + ticketList.size());
                // System.out.print("\033[H\033[2J");
                System.out.flush();
                lastestTickerFetchUtil.setAppendMode(true);
                lastestTickerFetchUtil.writeContentToFile(symbol + "," + latestDateFetchHolder[0]);
            }
            eu.saveAndClose();
            System.out.println("Done!");
            listOfTickerUtil.close();
            lastestTickerFetchUtil.close();
        } catch (Exception e) {
            eu.saveAndClose();
            System.out.println("Done!");
            listOfTickerUtil.close();
            lastestTickerFetchUtil.close();
        }
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

    private static String calculateMACD(Deque<Integer> dequeArray, BigDecimal[] prevArray,
            Integer dayAmount,
            Integer closedPrice) {
        try {
            /**
             * Calculate EMA with the formula:
             * EMA = (CurrentPrice * alpha) + (PreviousEMA * (1 - alpha))
             **/
            String result;
            dequeArray.add(closedPrice);
            BigDecimal prevEma12 = prevArray[0];
            BigDecimal prevEma26 = prevArray[1];

            if (dequeArray.size() >= 12) {
                // create initial EMA12
                if (prevEma12.compareTo(new BigDecimal(0)) == 0) {
                    List<Integer> listArray = dequeArray.stream().collect(Collectors.toList());
                    Integer sum = 0;
                    for (int i = 0; i < 12; i++) {
                        sum += listArray.get(i);
                    }

                    prevArray[0] = new BigDecimal((dequeArray.stream().mapToInt(Integer::intValue).sum()))
                            .divide(new BigDecimal(12), 10, RoundingMode.HALF_UP);

                } else {
                    BigDecimal alpha12 = new BigDecimal(0.15384615384);
                    BigDecimal oneMinusAlpha12 = BigDecimal.ONE.subtract(alpha12);
                    prevArray[0] = (new BigDecimal(closedPrice).multiply(alpha12))
                            .add(prevEma12.multiply(oneMinusAlpha12)).setScale(10, RoundingMode.CEILING);
                }
            }

            // when 26 days have passed
            if (dequeArray.size() == dayAmount) {

                if (prevEma26.compareTo(new BigDecimal(0)) == 0) {
                    prevArray[1] = new BigDecimal((dequeArray.stream().mapToInt(Integer::intValue).sum()))
                            .divide(new BigDecimal(dayAmount), 10, RoundingMode.HALF_UP);
                } else {
                    BigDecimal alpha26 = new BigDecimal(0.07407407407);
                    BigDecimal oneMinusAlpha26 = BigDecimal.ONE.subtract(alpha26);
                    prevArray[1] = (new BigDecimal(closedPrice).multiply(alpha26))
                            .add(prevEma26.multiply(oneMinusAlpha26)).setScale(10, RoundingMode.CEILING);
                }

                result = String.valueOf((prevArray[0].subtract(prevArray[1])));
                dequeArray.pop();
            } else {
                result = "0";
            }
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

    private static BusinessProfileEntity findProfileByDate(List<BusinessProfileEntity> businessProfileArray,
            String dateString) {
        try {
            // Parse the input date string to extract the year and month

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(dateString, formatter);

            int year = date.getYear();
            int month = date.getMonthValue();
            int lengthReport = (month - 1) / 3 + 1; // Calculate the quarter

            // Find the matching BusinessProfileEntity in the list
            for (BusinessProfileEntity profile : businessProfileArray) {
                if (profile.getYearReport() == year && profile.getLengthReport() == lengthReport) {
                    return profile;
                }
            }

            // It there is no data exist
            BusinessProfileEntity nullProfile = new BusinessProfileEntity();
            nullProfile.setIssueShare(0L);
            nullProfile.setEv(0L);
            nullProfile.setPb(new BigDecimal(0));
            nullProfile.setPe(new BigDecimal(0));
            nullProfile.setEps(new BigDecimal(0));

            return nullProfile;

        } catch (Exception e) {
            System.err.println("Invalid date format: " + e.getMessage());
        }

        return null; // Return null if no match is found
    }
}