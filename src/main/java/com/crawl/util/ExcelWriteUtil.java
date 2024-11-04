package com.crawl.util;

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

public class ExcelWriteUtil {

    private SXSSFSheet sheet;
    private SXSSFWorkbook workbook;
    // private File file;
    // private FileInputStream fis;
    private Row row;
    private Integer rowIndex = 1;
    private FileOutputStream fileOut;
    private String[] baseHeadersList = new String[] { "product_id", "report_time", "items_name", "items_value", };

    private String PUBLIC_RESOURCE_PATH = "src/main/resources/export_excel/";

    public ExcelWriteUtil() {

    }

    public void setFileName(String fileName) {
        try {
            this.fileOut = new FileOutputStream(new File(PUBLIC_RESOURCE_PATH + fileName + ".xlsx"));

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
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
            System.err.println("Error: " + e.getMessage());
        }
    }
}
