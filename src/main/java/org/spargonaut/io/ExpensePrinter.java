package org.spargonaut.io;

import org.spargonaut.datamodels.Expense;

import java.util.List;

public class ExpensePrinter {
    public void printExpensesAsHumanReadable(List<Expense> expenses) {
        for (Expense expense : expenses) {
            System.out.format("%-30s %-30s %-10s\n",
                    expense.getTimestamp(),
                    expense.getMerchant(),
                    expense.getAmount());
        }
    }
}
