package com.crawl.VietCap.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.function.BiConsumer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public class ExcelUtil {

    private SXSSFSheet sheet;
    private SXSSFWorkbook workbook;
    private File file;
    private FileInputStream fis;
    private Row row;
    private Integer rowIndex = 1;
    private FileOutputStream fileOut;
    private String[] baseHeadersList = new String[] {
            "Stock_ID", "Date", "Items_Name", "Items_Value",
    };
    private String[] extendHeaderList = new String[] { "Open_Price", "Close_Price",
            "Highest_Price", "Lowest_Price", "Total_Match_Volume", "Total_Match_Value",
            "Total_Value", "Total_Volume", "ATV_10", "AMTV_10",
            "RSI_14", "MA_10", "MACD"
    };
    // private String symbol;

    private String PUBLIC_RESOURCE_PATH = "src/main/resources/";

    public ExcelUtil() {

    }

    public void setFileName(String fileName) {
        try {
            this.fileOut = new FileOutputStream(new File(PUBLIC_RESOURCE_PATH + fileName + ".xlsx"));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void createWorkSheet() {
        this.workbook = new SXSSFWorkbook();
        this.sheet = workbook.createSheet("transaction");
        this.createBaseHeaders();
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public void resetRowIndex() {
        this.rowIndex = 1;
    }

    public Integer getRowIndex() {
        return this.rowIndex;
    }

    public void increaseRowIndex() {
        this.rowIndex++;
    }

    public void createRow(Integer rowIndex) {
        this.row = this.sheet.createRow(rowIndex);
    }

    public <T> void writeCellContent(Integer cellIndex, T value) {
        Cell cell = this.row.createCell(cellIndex);

        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(String.valueOf(value));
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Calendar) {
            cell.setCellValue((Calendar) value);
        } else if (value instanceof RichTextString) {
            cell.setCellValue((RichTextString) value);
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + value.getClass().getName());
        }
    }

    public void createBaseHeaders() {
        this.row = this.sheet.createRow(0);
        for (int i = 0; i < this.baseHeadersList.length; i++) {
            Cell cell = this.row.createCell(i);
            cell.setCellValue(this.baseHeadersList[i].toString());
        }
    }

    // public void createHeaders(String[] headers) {
    // this.headerRow = this.sheet.createRow(0);
    // for (int i = 0; i < headers.length; i++) {
    // Cell cell = this.headerRow.createCell(i);
    // cell.setCellValue(headers[i].toString());
    // }
    // }

    // public <T> void loopWriteContentTest(T[] entityList, BiConsumer<Row, T>
    // innerFunction) {
    // try {
    // createBaseHeaders();
    // for (T entity : entityList) {
    // for (String extendHeader : this.extendHeaderList) {
    // Row row = sheet.createRow(1);
    // row.createCell(2).setCellValue(extendHeader);
    // row.cellIterator().next().setCellValue("?");
    // innerFunction.accept(row, entity);
    // }
    // }
    // } catch (

    // Exception e) {
    // System.err.println("Error in writing content to excel file: " +
    // e.getMessage());

    // }
    // }

    public <T> void loopWriteContent(List<T> entityList, BiConsumer<Row, T> innerFunction) {
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
