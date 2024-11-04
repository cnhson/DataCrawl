package com.crawl.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;

public class ReadCsvUtil {

    private String[] header;
    private BufferedReader bufferedReader;
    private CSVParser csvParser;

    public ReadCsvUtil(String path, String filename) {
        try {
            bufferedReader = new BufferedReader(new FileReader(path + filename));
            // Read the first line as header and initialize CSVParser with it
            csvParser = CSVParser.parse(bufferedReader, CSVFormat.DEFAULT.builder().setSkipHeaderRecord(true).build());
            // header = csvParser.getHeaderMap().keySet().toArray(new String[0]);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void readLines(Consumer<Object[]> innerFunction) {
        if (csvParser != null) {
            try {
                for (CSVRecord record : csvParser) {
                    // Convert CSVRecord to String array
                    Object[] row = new Object[record.size()];
                    for (int i = 0; i < record.size(); i++) {
                        row[i] = record.get(i);
                    }
                    innerFunction.accept(row);
                }
            } catch (Exception e) {
                System.out.println("Error reading line: " + e.getMessage());
            } finally {
                close();
            }
        }
    }

    public void close() {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (csvParser != null) {
                csvParser.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }

    public String[] getHeader() {
        return header;
    }
}
