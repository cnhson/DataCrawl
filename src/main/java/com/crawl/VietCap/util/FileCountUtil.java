package com.crawl.VietCap.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryIteratorException;

public class FileCountUtil {
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("^[~!@#$%^&*()_+=\\[\\]{};':\"\\\\|,.<>?].*");

    public static List<String> getFilesWithExtension(String directoryPath, String fileExtension) {
        Path dir = Paths.get(directoryPath);
        List<String> result = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*" + fileExtension)) {
            for (Path entry : stream) {
                String fileName = entry.getFileName().toString();

                // Check if the filename doesn't start with a special character
                if (!SPECIAL_CHAR_PATTERN.matcher(fileName).matches()) {
                    result.add(fileName);
                }
            }
        } catch (IOException | DirectoryIteratorException e) {
            System.err.println("Error counting files: " + e.getMessage());
        }

        return result;
    }
}
