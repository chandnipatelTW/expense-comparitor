package org.spargonaut.io;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CSVFileLoader {

    public Set<File> getFileNamesIn(String directoryName) {
        File directory = new File(directoryName);
        return getCSVFiles(new HashSet<>(Arrays.asList(directory.listFiles())));
    }

    private Set<File> getCSVFiles(Set<File> allFiles) {
        return allFiles.stream()
                .filter(this::isCSVFile)
                .collect(Collectors.toSet());
    }

    private boolean isCSVFile(File csvFile) {
        String csvFileName = csvFile.getName();
        int csvFileLength = csvFileName.length();
        int indexOfSuffix = csvFileName.lastIndexOf('.');
        String csvFileNameSuffix = csvFileName.substring(indexOfSuffix, csvFileLength);
        return ".csv".equalsIgnoreCase(csvFileNameSuffix);
    }
}
