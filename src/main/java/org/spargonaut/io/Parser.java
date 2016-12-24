package org.spargonaut.io;

import org.spargonaut.datamodels.Expense;

import java.io.File;
import java.util.List;

public interface Parser {
    List<Expense> parseFile(File filename);
}
