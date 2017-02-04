package org.spargonaut.io;

import org.spargonaut.io.parser.Parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataLoader<T> {

    private final CSVFileLoader csvFileLoader;
    private final List<T> things;
    private final List<T> ignoredThings;

    public DataLoader(CSVFileLoader csvFilecsvFileLoader) {
        this.csvFileLoader = csvFilecsvFileLoader;
        things = new ArrayList<T>();
        ignoredThings = new ArrayList<T>();
    }

    public List<T> getLoadedFiles() {
        return this.things;
    }

    public List<T> getIgnoredData() {
        return this.ignoredThings;
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

    public void ignore(String directoryNameOfFilesToIgnore, Parser parser) {
        List<File> csvFileNames = csvFileLoader.getFileNamesIn(directoryNameOfFilesToIgnore);

        for (File csvFile : csvFileNames) {
            List<T> parsedThings = parser.parseFile(csvFile);
            for (T parsedThing : parsedThings) {
                ignoredThings.add(parsedThing);
                if (things.contains(parsedThing)) {
                    things.remove(parsedThing);
                }
            }
        }
    }
}
