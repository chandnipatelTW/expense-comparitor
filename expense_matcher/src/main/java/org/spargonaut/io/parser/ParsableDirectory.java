package org.spargonaut.io.parser;

import org.spargonaut.datamodels.CreditCardActivity;

public class ParsableDirectory {
    private String directoryPath;
    private Parser<CreditCardActivity> parser;

    public ParsableDirectory(String directoryPath, Parser<CreditCardActivity> parser) {
        this.directoryPath = directoryPath;
        this.parser = parser;
    }

    public String getDirectory() {
        return directoryPath;
    }

    public Parser<CreditCardActivity> getParser() {
        return parser;
    }
}
