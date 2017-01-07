package org.spargonaut.io.printer;

import org.spargonaut.datamodels.Expense;

import java.util.List;

public class ExpensePrinter {
    public static void printExpensesAsHumanReadable(List<Expense> expenses) {
        for (Expense expense : expenses) {
            System.out.format("%-15s %-30s %10s\n",
                    expense.getTimestamp().toLocalDate(),
                    expense.getMerchant(),
                    expense.getAmount());
        }
    }
}
