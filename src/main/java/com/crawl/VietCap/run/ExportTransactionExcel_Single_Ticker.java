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
import com.crawl.VietCap.util.ExcelWriteUtil;
import com.crawl.VietCap.util.TextUtil;

//`import com.foxthehuman.VietCapCrawl.controller.TransactionController;

public class ExportTransactionExcel_Single_Ticker {

    private static BigDecimal previousAverageGain = BigDecimal.ZERO;
    private static BigDecimal previousAverageLoss = BigDecimal.ZERO;
    private static Boolean isPreviousExist = false;

    public static void main(String[] args) throws InterruptedException {
        ExcelWriteUtil eu = new ExcelWriteUtil();
        TransactionRequest vct = new TransactionRequest();
        BusinessProfileRequest bpr = new BusinessProfileRequest();
        TextUtil lastestTickerFetchUtil = new TextUtil("src/main/resources/lastestTickerFetchList2.txt");
        TextUtil listOfTickerUtil = new TextUtil("src/main/resources/tickerList.txt");
        try {

            Integer averageTotalVolumeDays = 10;
            Integer RSI_Days = 14;
            Integer MA_Days = 10;
            Integer MACD_Days = 26;

            String[] extendHeaderList = new String[] { "Open_Price", "Close_Price",
                    "Highest_Price", "Lowest_Price", "Total_Match_Volume", "Total_Match_Value",
                    "Total_Value", "Total_Volume", "Average_Total_Value_10", "Average_Matching_Total_Value_10",
                    "RSI_14", "Prev_AVG", "Prev_AVL" ,"MA_10" ,"MACD", "Prev_SMA12", "Prev_SMA26", "EV", "IssueShare", "EPS", "PE", "PB"
            };


            String filename = "testing_";
            Integer fileNameIndex = 1;

            Integer pageNum = 1;
            Integer limit = 5000;

            // String startDate = "2000-01-01";
            String startDate = "2000-01-01";
            String endDate = "2100-01-01";
            // Boolean continueFetch = true;

            // List<String> ticketList = listOfTickerUtil.readFileAsListString();
            List<String> ticketList = Arrays.asList(
                    "VNM");

            eu.createWorkSheet();
            eu.setFileName(filename + fileNameIndex);

            for (int j = 0; j < ticketList.size(); j++) {
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

                            Object[] extendValueList = new Object[] {
                                entity.getOpenPrice(), 
                                entity.getClosePrice(),
                                entity.getHighestPrice(), 
                                entity.getLowestPrice(),
                                entity.getTotalMatchVolume(), 
                                entity.getTotalMatchValue(), 
                                entity.getTotalValue(),
                                entity.getTotalVolume(), 
                                averageTotalVolumeValue,
                                averageTotalMatchVolumeValue, 
                                RSI_Value, 
                                previousAverageGain != null ? previousAverageGain.toString() : "N/A",
                                previousAverageLoss != null ? previousAverageLoss.toString() : "N/A",
                                MA_Value, 
                                MACD_Value,
                                prevSMA12 != null ? prevSMA12.toString() : "N/A",
                                prevSMA26 != null ? prevSMA26.toString() : "N/A", 
                                foundBPE.getEv() != null ? foundBPE.getEv().toString() : "N/A",  
                                foundBPE.getIssueShare() != null ? foundBPE.getIssueShare().toString() : "N/A", 
                                foundBPE.getEps() != null ? foundBPE.getEps().toString() : "N/A", 
                                foundBPE.getPe() != null ? foundBPE.getPe().toString() : "N/A", 
                                foundBPE.getPb() != null ? foundBPE.getPb().toString() : "N/A"   
                            };

                    for (int i = 0; i < extendHeaderList.length; i++) {
                        if (eu.getRowIndex() == (1048576 - extendValueList.length + 1)) {
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
                System.out.println("\nCurrent: " + (j + 1) + "/" + ticketList.size());
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
                if(isPreviousExist)
                {
                Integer currentGain = priceChange > 0 ? priceChange : 0;
                Integer currentLoss = priceChange < 0 ? -priceChange : 0;

                // Calculate average gain/loss using exponential smoothing
                averageGain = (previousAverageGain.multiply(BigDecimal.valueOf(dayAmount - 1))
                        .add(BigDecimal.valueOf(currentGain))).divide(BigDecimal.valueOf(dayAmount), 10, RoundingMode.HALF_UP);

                averageLoss = (previousAverageLoss.multiply(BigDecimal.valueOf(dayAmount - 1))
                        .add(BigDecimal.valueOf(currentLoss))).divide(BigDecimal.valueOf(dayAmount), 10, RoundingMode.HALF_UP);
                }

                else {
                BigDecimal totalGain = BigDecimal.valueOf(
                        dequeArray.stream().filter(value -> value > 0).mapToInt(Integer::intValue).sum());

                BigDecimal totalLoss = BigDecimal.valueOf(
                        dequeArray.stream().filter(value -> value < 0).mapToInt(Integer::intValue).sum()).abs();

                averageGain = totalGain.divide(BigDecimal.valueOf(dayAmount), 10, RoundingMode.HALF_UP);

                averageLoss = totalLoss.divide(BigDecimal.valueOf(dayAmount), 10, RoundingMode.HALF_UP);
                isPreviousExist = true;
                }

                BigDecimal rs = averageGain.divide(averageLoss, 10, RoundingMode.HALF_UP);

                BigDecimal rsi = BigDecimal.valueOf(100)
                        .subtract(BigDecimal.valueOf(100).divide(BigDecimal.ONE.add(rs), 10, RoundingMode.HALF_UP));

                if(rsi.compareTo(new BigDecimal(70)) < 0)
                {
                    result = String.valueOf(rsi);
                    previousAverageGain = averageGain;
                    previousAverageLoss = averageLoss;
                }
                else
                    isPreviousExist = false;

              
                dequeArray.pop();
            } 
            return result;
        } catch (Exception e) {
            System.err.println("Error in RSI: " + e.getMessage());
            return "N/A";
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
                result = "N/A";
            }
            return result;
        } catch (Exception e) {
            System.err.println("Error in MACD: " + e.getMessage());
            return "N/A";
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
                result = "N/A";
            return result;
        } catch (Exception e) {
            System.err.println("Error in " + type + ": " + e.getMessage());
            return "N/A";
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