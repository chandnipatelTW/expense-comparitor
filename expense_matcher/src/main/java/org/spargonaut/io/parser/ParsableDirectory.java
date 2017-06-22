package org.spargonaut.io.parser;

public class ParsableDirectory<T> {
    private String directoryPath;
    private Parser<T> parser;

    public ParsableDirectory(String directoryPath, Parser<T> parser) {
        this.directoryPath = directoryPath;
        this.parser = parser;
    }

    public String getDirectory() {
        return directoryPath;
    }

    public Parser<T> getParser() {
        return parser;
    }

    public String getDirectoryToIgnore() {
        return directoryPath + "/manually_ignored";
    }
}
