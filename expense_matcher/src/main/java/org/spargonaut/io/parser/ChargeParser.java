package org.spargonaut.io.parser;

import org.spargonaut.io.CSVFileReader;

import java.io.File;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ChargeParser<T> implements Parser<T> {

    private final CSVFileReader csvFileReader;

    ChargeParser(CSVFileReader csvFileReader) {
        this.csvFileReader = csvFileReader;
    }

    public Set<T> parseFile(File chargeFile) {
        String chargeDelimiter = ",";

        return csvFileReader.readCsvFile(chargeFile).stream()
                .filter(this::isParsable)
                .map(chargeLine -> parseCreditCardActivity(chargeDelimiter, chargeLine))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public String fileLocation() {
        return csvFileReader.getFileNameOfLastReadFile();
    }

    private boolean isParsable(String chargeLine) {
        return !(isHeaderLine(chargeLine) || isCommentLine(chargeLine));
    }

    private boolean isCommentLine(String chargeLine) {
        return chargeLine.startsWith("#");
    }

    abstract boolean isHeaderLine(String chargeLine);

    abstract T parseCreditCardActivity(String chargeDelimiter, String chargeLine);
}
