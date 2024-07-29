package com.crawl.VietCap.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.crawl.VietCap.model.TransactionEntity;

public class ExcelUtil {

    private Sheet sheet;
    private Workbook workbook;
    private FileInputStream fis;
    private Row headerRow;
    private String[] headers;
    private String fileName;
    private String PUBLIC_RESOURCE_PATH = "src/main/resources/";

    public ExcelUtil(String fileName) {
        try {

            this.fileName = fileName;
            this.workbook = new XSSFWorkbook();
            this.sheet = workbook.createSheet("Transactions");
            setHeadersConfig();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void setHeadersConfig() {
        this.headers = new String[] {
                "Trading_Date", "Price_Change", "Percent_Price_Change", "Open_Price", "Close_Price",
                "Highest_Price", "Lowest_Price", "Total_Match_Volume", "Total_Match_Value",
                "Total_Deal_Volume", "Total_Deal_Value", "Unmatched_Buy_Trade_Volume",
                "Unmatched_Sell_Trade_Volume", "Total_Value", "Total_Volume"
        };
    }

    public void writeContent(TransactionEntity[] transList) {

        File file = new File(this.PUBLIC_RESOURCE_PATH + this.fileName + ".xlsx");
        try {
            this.headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = this.headerRow.createCell(i);
                cell.setCellValue(headers[i].toString());
            }

            int rowNum = 1;
            for (TransactionEntity transEntity : transList) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(transEntity.getFormatStringTradingDate());
                row.createCell(1).setCellValue(transEntity.getPriceChange());
                row.createCell(2).setCellValue(transEntity.getPercentPriceChange().doubleValue());
                row.createCell(3).setCellValue(transEntity.getOpenPrice());
                row.createCell(4).setCellValue(transEntity.getClosePrice());
                row.createCell(5).setCellValue(transEntity.getHighestPrice());
                row.createCell(6).setCellValue(transEntity.getLowestPrice());
                row.createCell(7).setCellValue(transEntity.getTotalMatchVolume());
                row.createCell(8).setCellValue(transEntity.getTotalMatchValue());
                row.createCell(9).setCellValue(transEntity.getTotalDealVolume());
                row.createCell(10).setCellValue(transEntity.getTotalDealValue());
                row.createCell(11).setCellValue(transEntity.getUnMatchedBuyTradeVolume());
                row.createCell(12).setCellValue(transEntity.getUnMatchedSellTradeVolume());
                row.createCell(13).setCellValue(transEntity.getTotalValue());
                row.createCell(14).setCellValue(transEntity.getTotalVolume());
            }
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());

        }
    }
}
