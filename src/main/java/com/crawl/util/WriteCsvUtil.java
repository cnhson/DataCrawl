package com.crawl.util;

import java.io.FileWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import java.io.IOException;

public class WriteCsvUtil {

    private CSVPrinter printer;

    public WriteCsvUtil() {
        // No initialization required here.
    }

    public void setFile(String path, String filename, String[] headers) {
        try {
            // Initialize the CSVPrinter with a format that disables quotes
            printer = new CSVPrinter(new FileWriter(path + filename),
                    CSVFormat.POSTGRESQL_CSV.builder().setQuote(null).build());
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void writeLine(Object[] record) {
        if (printer != null) {
            try {
                printer.printRecord(record);
                printer.flush();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void closeCSV() {
        if (printer != null) {
            try {
                printer.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
