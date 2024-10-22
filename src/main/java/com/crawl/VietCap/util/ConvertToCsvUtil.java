package com.crawl.VietCap.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.crawl.VietCap.lib.ExcelSheetHandler;

public class ConvertToCsvUtil extends ExcelSheetHandler {

    private CSVPrinter csvPrinter;
    private Writer writer;
    private String excelFilePath = "src/main/resources/export_excel/";
    // private String csvFileName;
    private String pathTofile;
    private String[] baseHeadersList = new String[] {
            "Product_ID", "Report_Date", "Items_Name", "Items_Value",
    };

    public ConvertToCsvUtil() {
    }

    public void createCSVFile(String filename) {
        try {
            // this.csvFileName = "src/main/resources/crawl_result.csv";
            writer = new FileWriter("src/main/resources/"+filename+".csv");
            csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.builder()
                    .setHeader(baseHeadersList)
                    .build());
        } catch (Exception e) {
            System.err.println("Error trying to create CSV file: " + e.getMessage());
        }
    }

    public void appendToCSV(Map<String, String> content) throws IOException {
        if (csvPrinter != null) {
            csvPrinter.printRecord(rowValues.get("A"), // Stock_ID
                    rowValues.get("B"), // Date
                    rowValues.get("C"), // Items_Name
                    rowValues.get("D"));// Items_Value);
        } else {
            throw new IllegalStateException("CSVPrinter is not initialized. Call createCSV() first.");
        }
    }

    public void readingExcelFile() {
        try {
            List<String> excelFileList = FileCountUtil.getFilesWithExtension(excelFilePath, ".xlsx");
            Integer count = 1;
            for (String excelFile : excelFileList) {

                System.out.println("\nCurrent: " + count++ + "/" + excelFileList.size());
                System.out.flush();
                super.readExcelFile(new File(excelFilePath + excelFile));
            }
        } catch (Exception e) {
            System.err.println("Error in reading excel file(s): " + e.getMessage());
        }
    }

    // Close the CSVPrinter and writer
    public void closeCSV() throws IOException {
        if (csvPrinter != null) {
            csvPrinter.flush();
            csvPrinter.close();
        }
        if (writer != null) {
            writer.close();
        }
    }

    public void startConvertProcess(String filename) throws IOException {
        this.createCSVFile(filename);
        this.readingExcelFile();
        this.closeCSV();
    }

    @Override
    protected boolean processSheet(String sheetName) {
        // Decide which sheets to read; Return true for all sheets
        // return "Sheet 1".equals(sheetName);
        return true;
    }

    @Override
    protected void startSheet() {
        // Any custom logic when a new sheet starts
        // System.out.println("Sheet starts");
    }

    @Override
    protected void endSheet() {
        // Any custom logic when sheet ends
        // System.out.println("Sheet ends");
    }

    public void readFile() {
        try {

        } catch (Exception e) {
            System.err.println("Error trying to access " + pathTofile + "\n" + e.getMessage());
        }
    }

    @Override
    protected void processRow() {
        try {
            if (rowNumber > 1 && !rowValues.isEmpty()) {

                // Get specific values here
                /*
                 * String a = rowValues.get("A");
                 * String b = rowValues.get("B");
                 */
                this.appendToCSV(rowValues);
            }
        } catch (Exception e) {
            System.err.println("Error in processRow: " + e.getMessage());
        }
    }
}
