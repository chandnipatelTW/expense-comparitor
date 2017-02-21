package org.spargonaut.io;

import org.spargonaut.io.parser.Parser;

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
        csvFileLoader.getFileNamesIn(directoryName)
        .forEach(csvFile -> {
            Set<T> uniqueParsedThings = parser.parseFile(csvFile)
                    .stream()
                    .filter(parsedThing -> !things.contains(parsedThing))
                    .collect(Collectors.toSet());
            things.addAll(uniqueParsedThings);
        });
    }

    public void ignore(String directoryNameOfFilesToIgnore, Parser<T> parser) {
        csvFileLoader.getFileNamesIn(directoryNameOfFilesToIgnore)
        .forEach(csvFile -> {
            Set<T> uniqueParsedThings = parser.parseFile(csvFile)
                    .stream()
                    .filter(things::contains)
                    .collect(Collectors.toSet());
            things.removeAll(uniqueParsedThings);
            ignoredThings.addAll(uniqueParsedThings);
        });
    }
}
