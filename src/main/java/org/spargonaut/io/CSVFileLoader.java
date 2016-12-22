package org.spargonaut.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVFileLoader {

    public List<File> getFileNamesIn(String directoryName) {
        File directory = new File(directoryName);
        return getChargeFiles(Arrays.asList(directory.listFiles()));
    }

    private List<File> getChargeFiles(List<File> allFiles) {
        List<File> chargeFiles = new ArrayList<>();
        for (File chargeFile : allFiles) {
            if (isCSVFile(chargeFile)) {
                chargeFiles.add(chargeFile);
            }
        }
        return chargeFiles;
    }

    private boolean isCSVFile(File chargeFile) {
        return (chargeFile.getName().endsWith(".csv") || chargeFile.getName().endsWith(".CSV"));
    }
}
