package com.crawl.util;

import java.io.FileWriter;

import com.opencsv.CSVWriter;

public class WriteCsvUtil {

    private CSVWriter writer;

    public WriteCsvUtil() {

    }

    public void setFile(String path, String filename, String[] headers) {
        try {
            writer = new CSVWriter(new FileWriter(path + filename));
            writeLine(headers);
            writer.flush();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void writeLine(String[] record) {
        if (writer != null)
            try {
                writer.writeNext(record);
                writer.flush();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
    }

    public void closeCSV() {
        if (writer != null)
            try {
                writer.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
    }
}
