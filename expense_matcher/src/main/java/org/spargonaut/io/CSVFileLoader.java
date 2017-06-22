package org.spargonaut.io;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CSVFileLoader {

    Set<File> getFileNamesIn(String directoryName) {
        try {
            File[] files = new File(directoryName).listFiles();
            return (files == null) ?
                    new HashSet<>() :
                    Arrays.stream(files)
                        .filter(this::isCSVFile)
                        .collect(Collectors.toSet());

        } catch (Exception e) {
            String message = "Trouble loading the file: " + directoryName + "\n" +e.getMessage();
            throw new RuntimeException(message, e.getCause());
        }
    }

    private boolean isCSVFile(File csvFile) {
        String csvFileName = csvFile.getName();
        int csvFileLength = csvFileName.length();
        int indexOfSuffix = csvFileName.lastIndexOf('.');
        if (indexOfSuffix >= 0) {
            String csvFileNameSuffix = csvFileName.substring(indexOfSuffix, csvFileLength);
            return ".csv".equalsIgnoreCase(csvFileNameSuffix);
        } else {
            return false;
        }
    }
}
