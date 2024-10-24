package com.crawl.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;

public class TextUtil {

    // private String pathToFile;
    private File file;
    private BufferedWriter bw;
    private BufferedReader br;

    public TextUtil(String pathToFile) {
        try {
            // this.pathToFile = pathToFile;
            this.file = new File(pathToFile);
            if (!file.exists()) {
                System.out.println("Text file not found, creating new one!");
                file.createNewFile();
            }

        } catch (Exception e) {
            System.err.println("[TextUtil] Error in setPathToFile: " + e.getMessage());
        }
    }

    // public void setPathToFile(String pathToFile) {
    // try {
    // // this.pathToFile = pathToFile;
    // this.file = new File(pathToFile);
    // if (!file.exists()) {
    // System.out.println("Text file not found, creating new one!");
    // file.createNewFile();
    // }

    // } catch (Exception e) {
    // System.err.println("[TextUtil] Error in setPathToFile: " + e.getMessage());
    // }
    // }

    public void setAppendMode(Boolean appendMode) {
        try {
            this.bw = new BufferedWriter(new FileWriter(this.file, appendMode));
        } catch (IOException e) {
            System.out.println("[TextUtil] Error in setAppendMode: " + e.getMessage());
        }
    }

    public void writeContentToFile(String content) {
        try {
            bw.write(content);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            System.err.println("[TextUtil] Error in writeContentToFile: " + e.getMessage());
        }
    }

    public void readContentFromFile(Consumer<String> innerFunction) {
        try {
            br = new BufferedReader(new FileReader(this.file));

            String line = null;
            while ((line = br.readLine()) != null) {
                innerFunction.accept(line);
            }
        } catch (IOException e) {
            System.err.println("[TextUtil] Error in readContentFromFile: " + e.getMessage());
        }
    }

    public List<String> readFileAsListString() {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(this.file.getPath()))) {
            return bufferedReader.lines().filter(line -> !line.trim().isEmpty()) // Skip empty lines
                    .toList();
        } catch (IOException e) {
            System.err.println("[TextUtil] Error reading all lines from file: " + e.getMessage());
            return null;
        }
    }

    public void close() {
        try {
            if (bw != null)
                bw.close();
            if (br != null)
                br.close();
        } catch (IOException e) {
            System.err.println("[TextUtil] Error in closing: " + e.getMessage());
        }
    }
}
