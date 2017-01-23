package org.spargonaut.io.parser;

import java.io.File;
import java.util.List;

public interface Parser<T> {
    List<T> parseFile(File filename);
}
