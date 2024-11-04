package com.crawl.executor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

import com.crawl.controller.CafeFTransactionRequest;
import com.crawl.controller.VietCapFinancalAnalystRequest;
import com.crawl.model.CafeFTransactionEntity;
import com.crawl.model.VietCapFinancalAnalystEntity;
import com.crawl.util.TextUtil;
import com.crawl.util.WriteCsvUtil;

//`import com.foxthehuman.VietCapCrawl.controller.TransactionController;

public class CafeFExportTransactionCsv {
    private static BigDecimal prevAverageGain = BigDecimal.ZERO;
    private static BigDecimal prevAverageLoss = BigDecimal.ZERO;
    private static BigDecimal prevEMA12 = BigDecimal.ZERO;
    private static BigDecimal prevEMA26 = BigDecimal.ZERO;

    public CafeFExportTransactionCsv() {

    }

    public void execute(String startDate, String endDate, String symbol, String exportPath, Integer startIndex)
            throws InterruptedException {
        WriteCsvUtil wcu = new WriteCsvUtil();
        CafeFTransactionRequest cft = new CafeFTransactionRequest();
        VietCapFinancalAnalystRequest bpr = new VietCapFinancalAnalystRequest();
        TextUtil lastestTickerFetchUtil = new TextUtil("src/main/resources/lastestTickerFetchList.txt");
        TextUtil listOfTickerUtil = new TextUtil("src/main/resources/tickerList.txt");
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Boolean isNotLatest = false;
            Integer averageTotalVolumeDays = 10;
            Integer RSI_Days = 14;
            Integer MA_Days = 10;
            Integer MACD_Min_Days = 12;
            Integer MACD_Max_Days = 26;
            List<String> ticketList;
            String filename;
            Integer pageNum = 1;
            Integer limit = 6000;
            String[] baseHeadersList = new String[] { "product_id", "report_time", "items_name", "items_value", };
            String[] extendHeaderList = new String[] { "Open_Price", "Close_Price", "Highest_Price", "Lowest_Price",
                    "Total_Match_Volume", "Total_Match_Value", "Total_Value", "Total_Volume", "Average_Total_Value_10",
                    "Average_Matching_Total_Value_10", "RSI_14", "Prev_AVG", "Prev_AVL", "MA_10", "MACD", "Prev_SMA12",
                    "Prev_SMA26", "EV", "IssueShare", "EPS", "PE", "PB" };

            if (exportPath == null || exportPath.isEmpty()) {
                String projectDir = System.getProperty("user.dir");
                exportPath = projectDir + "\\src\\main\\resources\\export_csv\\";
            }

            //
            // String startDate = "2000-01-01";
            // String endDate = "2100-01-01";
            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            if (startDate == null || startDate.isEmpty()) {
                throw new IllegalArgumentException("Start date is required");
            }

            if (endDate == null || endDate.isEmpty()) {
                throw new IllegalArgumentException("End date is required");
            }
            // Handle if input enddate is not latest according to current date
            Long inputEndDate = LocalDate.parse(endDate, formatter).toEpochDay();
            Long currentDate = LocalDate.now().toEpochDay();
            if (inputEndDate.compareTo(currentDate) < 0) {
                isNotLatest = true;
            }
            //
            if (symbol != null && !symbol.isEmpty()) {
                startIndex = 0;
                filename = currentDateTime + "_" + symbol + "_single.csv";
                ticketList = Arrays.asList(symbol);
            } else {
                filename = currentDateTime + "_all.csv";
                ticketList = listOfTickerUtil.readFileAsListString();
            }
            wcu.setFile(exportPath, filename, baseHeadersList);

            // for (int j = startIndex; j < startIndex + 1; j++) {
            for (int j = startIndex; j < ticketList.size(); j++) {

                Thread.sleep(100);
                String currentSymbol = ticketList.get(j);
                System.out.println("\nCurrent symbol: " + currentSymbol);

                List<CafeFTransactionEntity> transList = cft.crawlData(currentSymbol, startDate, endDate, pageNum,
                        limit);
                List<VietCapFinancalAnalystEntity> FinancalAnalystList = bpr.crawlData(currentSymbol);

                // If not fetching to the lastest date, then delete first element because CafeF
                // always return the lastest record no matter
                if (isNotLatest == true) {
                    transList.remove(0);
                }

                // Revert the list (from oldest to lastest)
                Collections.reverse(FinancalAnalystList);
                // Ascending sort on datetime (from oldest to lastest)
                transList.sort(Comparator.comparing(CafeFTransactionEntity::getTradingLocalDate));

                Deque<Integer> averageTotalVolumeArray = new ArrayDeque<>(averageTotalVolumeDays);
                Deque<Integer> averageTotalMatchVolumeArray = new ArrayDeque<>(averageTotalVolumeDays);
                Deque<Integer> RSI_Array = new ArrayDeque<>(averageTotalVolumeDays);
                Deque<Integer> MA_array = new ArrayDeque<>(averageTotalVolumeDays);
                Deque<Integer> MACD_array = new ArrayDeque<>(MACD_Max_Days);
                final String[] latestDateFetchHolder = { null };

                for (CafeFTransactionEntity entity : transList) {

                    VietCapFinancalAnalystEntity foundBPE = findProfileByDate(FinancalAnalystList,
                            entity.getTradingDate());

                    String averageTotalVolumeValue = calculateATV(averageTotalVolumeArray, averageTotalVolumeDays,
                            entity.getTotalVolume());
                    String averageTotalMatchVolumeValue = calculateATV(averageTotalMatchVolumeArray,
                            averageTotalVolumeDays, entity.getTotalMatchVolume());
                    String RSI_Value = calculateRSI(RSI_Array, RSI_Days, entity.getPriceChangeAsInteger());
                    String MA_Value = calculateMA(MA_array, MA_Days, entity.getClosePrice());

                    String MACD_Value = calculateMACD(MACD_array, MACD_Min_Days, MACD_Max_Days, entity.getClosePrice());

                    Object[] extendValueList = new Object[] { entity.getOpenPrice(), entity.getClosePrice(),
                            entity.getHighestPrice(), entity.getLowestPrice(), entity.getTotalMatchVolume(),
                            entity.getTotalMatchValue(), entity.getTotalValue(), entity.getTotalVolume(),
                            averageTotalVolumeValue, averageTotalMatchVolumeValue, RSI_Value,
                            prevAverageGain.compareTo(new BigDecimal(0)) != 0 ? prevAverageGain : "N/A",
                            prevAverageLoss.compareTo(new BigDecimal(0)) != 0 ? prevAverageLoss : "N/A", MA_Value,
                            MACD_Value, prevEMA12.compareTo(new BigDecimal(0)) != 0 ? prevEMA12 : "N/A",
                            prevEMA26.compareTo(new BigDecimal(0)) != 0 ? prevEMA26 : "N/A",
                            foundBPE.getEv() != null ? foundBPE.getEv() : "N/A",
                            foundBPE.getIssueShare() != null ? foundBPE.getIssueShare() : "N/A",
                            foundBPE.getEps() != null ? foundBPE.getEps() : "N/A",
                            foundBPE.getPe() != null ? foundBPE.getPe() : "N/A",
                            foundBPE.getPb() != null ? foundBPE.getPb() : "N/A" };

                    for (int i = 0; i < extendHeaderList.length; i++) {

                        wcu.writeLine(new String[] { currentSymbol, entity.getTradingDate(), extendHeaderList[i],
                                extendValueList[i].toString() });
                    }

                    latestDateFetchHolder[0] = entity.getTradingDate();
                }

                System.out.println("Done: " + (j + 1) + "/" + ticketList.size());
                System.out.flush();
                lastestTickerFetchUtil.setAppendMode(true);
                lastestTickerFetchUtil.writeContentToFile(currentSymbol + "," + latestDateFetchHolder[0]);
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

                result = String.valueOf(rsi);
                prevAverageGain = averageGain;
                prevAverageLoss = averageLoss;

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

    private static VietCapFinancalAnalystEntity findProfileByDate(
            List<VietCapFinancalAnalystEntity> FinancalAnalystArray, String dateString) {
        try {
            // Parse the input date string to extract the year and month

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(dateString, formatter);

            int year = date.getYear();
            int month = date.getMonthValue();
            int lengthReport = (month - 1) / 3 + 1; // Calculate the quarter

            // Find the matching FinancalAnalystEntity in the list
            for (VietCapFinancalAnalystEntity profile : FinancalAnalystArray) {
                if (profile.getYearReport() == year && profile.getLengthReport() == lengthReport) {
                    return profile;
                }
            }

            // It there is no data exist
            VietCapFinancalAnalystEntity nullProfile = new VietCapFinancalAnalystEntity();
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