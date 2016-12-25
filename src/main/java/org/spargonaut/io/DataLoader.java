package org.spargonaut.io;

import org.spargonaut.datamodels.Expense;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    private final CSVFileLoader csvFileLoader;

    public DataLoader(CSVFileLoader csvFilecsvFileLoader) {
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
