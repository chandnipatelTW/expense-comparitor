package org.spargonaut.io;

import org.spargonaut.io.parser.Parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataLoader<T> {

    private final CSVFileLoader csvFileLoader;

    public DataLoader(CSVFileLoader csvFilecsvFileLoader) {
        this.csvFileLoader = csvFilecsvFileLoader;
    }

    public List<T> load(String directoryName, Parser parser) {
        List<File> csvFileNames = csvFileLoader.getFileNamesIn(directoryName);

        List<T> things = new ArrayList<T>();
        for (File csvFile : csvFileNames) {
            List<T> parsedThings = parser.parseFile(csvFile);
            for (T parsedThing : parsedThings) {
                if (!things.contains(parsedThing)) {
                    things.add(parsedThing);
                }
            }
        }
        return  things;
    }
}
