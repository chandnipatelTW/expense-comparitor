package org.spargonaut.io;

import org.spargonaut.datamodels.Expense;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExpenseParser {

    private final ChargeReader csvFileReader;

    public ExpenseParser(ChargeReader csvFileReader) {
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
                    expenseTokens[0],
                    expenseTokens[1],
                    expenseTokens[2],
                    expenseTokens[3],
                    expenseTokens[4],
                    expenseTokens[5],
                    expenseTokens[6],
                    expenseTokens[7],
                    expenseTokens[8],
                    expenseTokens[9],
                    expenseTokens[10]
            );
            expenses.add(expense);

        }

        return expenses;
    }

    private boolean isHeaderLine(String expenseLine) {
        return "Timestamp,Merchant,Amount,MCC,Category,Tag,Comment,Reimbursable,\"Original Currency\",\"Original Amount\",Receipt".equals(expenseLine);
    }
}
