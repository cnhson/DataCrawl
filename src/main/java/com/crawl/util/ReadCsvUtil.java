package com.crawl.util;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.opencsv.CSVReader;

public class ReadCsvUtil {

    String filename;
    String path;
    FileReader fileReader;
    Boolean isStop = false;

    public ReadCsvUtil(String path, String filename) {
        try {
            fileReader = new FileReader(path + filename);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void readLines(Consumer<String[]> innerFunction) {
        if (fileReader != null) {
            String[] row;
            try (CSVReader reader = new CSVReader(fileReader)) {
                while ((row = reader.readNext()) != null) {
                    innerFunction.accept(row);
                }
                isStop = true;

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Boolean isStopCheck() {
        return this.isStop;
    }
}
