package org.spargonaut.io;

import org.spargonaut.io.parser.Parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataLoader<T> {

    private final CSVFileLoader csvFileLoader;
    private final List<T> things;

    public DataLoader(CSVFileLoader csvFilecsvFileLoader) {
        this.csvFileLoader = csvFilecsvFileLoader;
        things = new ArrayList<T>();
    }

    public List<T> getLoadedFiles() {
        return this.things;
    }

    public void load(String directoryName, Parser parser) {
        List<File> csvFileNames = csvFileLoader.getFileNamesIn(directoryName);

        for (File csvFile : csvFileNames) {
            List<T> parsedThings = parser.parseFile(csvFile);
            for (T parsedThing : parsedThings) {
                if (!things.contains(parsedThing)) {
                    things.add(parsedThing);
                }
            }
        }
    }
}
