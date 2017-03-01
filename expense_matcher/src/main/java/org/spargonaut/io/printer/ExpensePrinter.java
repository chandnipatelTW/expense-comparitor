package org.spargonaut.io.printer;

import org.spargonaut.datamodels.Expense;

import java.util.Set;

public class ExpensePrinter {
    public static void printExpensesAsHumanReadable(Set<Expense> expenses) {
        System.out.format("expenses (%d) -------------------------------------------\n", expenses.size());
        expenses.forEach(expense -> {
            System.out.format("%-15s %-30s %10s\n",
                    expense.getTimestamp().toLocalDate(),
                    expense.getMerchant(),
                    expense.getAmount());
        });
    }
}
