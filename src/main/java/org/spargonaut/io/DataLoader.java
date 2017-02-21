package org.spargonaut.io;

import org.spargonaut.io.parser.Parser;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DataLoader<T> {

    private final CSVFileLoader csvFileLoader;
    private final Set<T> things;
    private final Set<T> ignoredThings;

    public DataLoader(CSVFileLoader csvFilecsvFileLoader) {
        this.csvFileLoader = csvFilecsvFileLoader;
        things = new HashSet<T>();
        ignoredThings = new HashSet<T>();
    }

    public Set<T> getLoadedFiles() {
        return this.things;
    }

    public Set<T> getIgnoredData() {
        return this.ignoredThings;
    }

    public void load(String directoryName, Parser<T> parser) {
        csvFileLoader.getFileNamesIn(directoryName).forEach(csvFile -> {
            Set<T> uniqueParsedThings = parser.parseFile(csvFile)
                    .stream()
                    .filter(parsedThing -> !things.contains(parsedThing))
                    .collect(Collectors.toSet());
                    things.addAll(uniqueParsedThings);
                });
    }

    public void ignore(String directoryNameOfFilesToIgnore, Parser parser) {
        Set<File> csvFileNames = csvFileLoader.getFileNamesIn(directoryNameOfFilesToIgnore);

        for (File csvFile : csvFileNames) {
            Set<T> parsedThings = parser.parseFile(csvFile);
            for (T parsedThing : parsedThings) {
                ignoredThings.add(parsedThing);
                if (things.contains(parsedThing)) {
                    things.remove(parsedThing);
                }
            }
        }
    }
}
