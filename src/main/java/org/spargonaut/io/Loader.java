package org.spargonaut.io;

import org.spargonaut.datamodels.Expense;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private final CSVFileLoader csvFileLoader;

    public Loader(CSVFileLoader csvFilecsvFileLoader) {
        this.csvFileLoader = csvFilecsvFileLoader;
    }

    public List<Expense> loadExpenses(String directoryName, Parser parser) {
        List<File> csvFileNames = csvFileLoader.getFileNamesIn(directoryName);

        List<Expense> things = new ArrayList<>();
        for (File csvFile : csvFileNames) {
            things.addAll(parser.parseFile(csvFile));
        }
        return  things;
    }
}
