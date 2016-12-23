package org.spargonaut.io;

import org.spargonaut.datamodels.Expense;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExpenseParser {

    private final CSVFileReader csvFileReader;

    public ExpenseParser(CSVFileReader csvFileReader) {
        this.csvFileReader = csvFileReader;
    }

    public List<Expense> parseExpenses(File expenseFile) {
        String expenseDelimiter = ",";

        List<String> expenseLines = csvFileReader.readCreditCardFile(expenseFile);
        List<Expense> expenses = new ArrayList<>();

        for (String expenseLine : expenseLines) {
            if (isHeaderLine(expenseLine)) { continue; }
            String[] expenseTokens = expenseLine.split(expenseDelimiter);
            Expense expense = new Expense(
                    cleanOffQuotes(expenseTokens[0]),
                    cleanOffQuotes(expenseTokens[1]),
                    cleanOffQuotes(expenseTokens[2]),
                    cleanOffQuotes(expenseTokens[3]),
                    cleanOffQuotes(expenseTokens[4]),
                    cleanOffQuotes(expenseTokens[5]),
                    cleanOffQuotes(expenseTokens[6]),
                    cleanOffQuotes(expenseTokens[7]),
                    cleanOffQuotes(expenseTokens[8]),
                    cleanOffQuotes(expenseTokens[9]),
                    cleanOffQuotes(expenseTokens[10])
            );
            expenses.add(expense);

        }

        return expenses;
    }

    private String cleanOffQuotes(String token) {
        String cleanedString = token;
        if (token.startsWith("\"") && token.endsWith("\"")) {
            cleanedString = token.substring(1, token.lastIndexOf("\""));
        }
        return cleanedString;
    }

    private boolean isHeaderLine(String expenseLine) {
        return "Timestamp,Merchant,Amount,MCC,Category,Tag,Comment,Reimbursable,\"Original Currency\",\"Original Amount\",Receipt".equals(expenseLine);
    }
}
