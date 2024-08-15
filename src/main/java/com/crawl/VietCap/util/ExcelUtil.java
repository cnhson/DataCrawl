package com.crawl.VietCap.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.BiConsumer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExcelUtil {

    private SXSSFSheet sheet;
    private SXSSFWorkbook workbook;
    private File file;
    private FileInputStream fis;
    private Row headerRow;
    private FileOutputStream fileOut;
    // private String symbol;

    private String PUBLIC_RESOURCE_PATH = "src/main/resources/";

    public ExcelUtil() {
        this.workbook = new SXSSFWorkbook();
    }

    public void setFileName(String fileName) {
        try {
            this.fileOut = new FileOutputStream(new File(PUBLIC_RESOURCE_PATH + fileName + ".xlsx"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addNewSymbolSheet(String symbol, String[] headers) {
        this.sheet = workbook.createSheet(symbol);
        this.headerRow = this.sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = this.headerRow.createCell(i);
            cell.setCellValue(headers[i].toString());
        }
    }

    // public void writeContentTest(String[] headers) {

    // try {
    // String[] sheetNames = { "Sheet1", "Sheet2", "Sheet3" };
    // for (int i = 0; i < sheetNames.length; i++) {
    // // Create a new sheet
    // this.sheet = workbook.createSheet(sheetNames[i]);
    // // }

    // // this.headerRow = this.sheet.createRow(0);
    // // for (int x = 0; x < headers.length; x++) {
    // // Cell cell = this.headerRow.createCell(i);
    // // cell.setCellValue(headers[i].toString());
    // // }

    // // int rowNum = 1;
    // // for (Integer y = 0; y < 2; y++) {
    // // Row row = sheet.createRow(rowNum++);
    // // row.createCell(0).setCellValue("0");
    // // row.createCell(1).setCellValue("1");

    // // }
    // }
    // workbook.write(fileOut);

    // } catch (IOException e) {
    // e.printStackTrace();
    // System.err.println(e.getMessage());

    // }
    // }

    public <T> void loopWriteContent(T[] entityList, BiConsumer<Row, T> innerFunction) {
        try {
            Integer rowNum = 1;
            for (T entity : entityList) {
                Row row = sheet.createRow(rowNum++);
                innerFunction.accept(row, entity);
            }
        } catch (Exception e) {
            System.err.println("Error in writing content to excel file: " + e.getMessage());

        }
    }

    public void saveAndClose() {
        try {
            workbook.write(fileOut);
            fileOut.flush(); // Ensure all data is written
            fileOut.close();
            workbook.close();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
