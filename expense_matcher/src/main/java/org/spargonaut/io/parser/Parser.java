package org.spargonaut.io.parser;

import java.io.File;
import java.util.Set;

public interface Parser<T> {
    Set<T> parseFile(File filename);
}
