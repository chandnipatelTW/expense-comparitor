package org.spargonaut.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

public class CSVFileReader {

    private String fileName;

    public Set<String> readCsvFile(File creditCardFile) {
        Set<String> chargeLines = new HashSet<>();
        fileName = creditCardFile.getName();

        try (BufferedReader br = new BufferedReader(new FileReader(creditCardFile))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                chargeLines.add(currentLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return chargeLines;
    }

    public String getFileNameOfLastReadFile() {
        return fileName;
    }
}
