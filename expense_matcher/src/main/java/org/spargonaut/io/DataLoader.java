package org.spargonaut.io;

import org.spargonaut.io.parser.ParsableDirectory;
import org.spargonaut.io.parser.Parser;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class DataLoader<T> {

    private final CSVFileLoader csvFileLoader;
    private final Set<T> things;
    private final Set<T> ignoredThings;
    private final ParsableDirectory<T> parsableDirectory;

    public DataLoader(CSVFileLoader csvFileLoader, ParsableDirectory<T> parsableDirectory) {
        this.parsableDirectory = parsableDirectory;
        this.csvFileLoader = csvFileLoader;
        things = new HashSet<>();
        ignoredThings = new HashSet<>();
    }

    public Set<T> getIgnoredData() {
        return this.ignoredThings;
    }

    public Set<T> loadTransactions() {
        Parser<T> parser = parsableDirectory.getParser();

        String directoryNameToLoad = parsableDirectory.getDirectory();
        loadAllTransactions(directoryNameToLoad, parser);

        String directoryToIgnore = parsableDirectory.getDirectoryToIgnore();
        removeManuallyIgnoredTransactions(directoryToIgnore, parser);
        return things;
    }

    private void loadAllTransactions(String directoryName, Parser<T> parser) {
        csvFileLoader.getFileNamesIn(directoryName)
                .forEach(csvFile -> {
                    Set<T> uniqueParsedThings = parser.parseFile(csvFile)
                            .stream()
                            .filter(parsedThing -> !things.contains(parsedThing))
                            .collect(Collectors.toSet());
                    things.addAll(uniqueParsedThings);
                });
    }

    private void removeManuallyIgnoredTransactions(String directoryNameOfFilesToIgnore, Parser<T> parser) {
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
