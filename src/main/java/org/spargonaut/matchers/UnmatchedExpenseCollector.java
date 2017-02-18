package org.spargonaut.matchers;

import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UnmatchedExpenseCollector {
    public List<Expense> collect(List<Expense> expenses, Collection<List<MatchedTransaction>> matchedTransactionLists) {
        List<Expense> unmatchedExpenses = new ArrayList<>(expenses);
        for (List<MatchedTransaction> matchedTransactions : matchedTransactionLists) {
            for (Expense expense : expenses) {
                for (MatchedTransaction matchedTransaction : matchedTransactions) {
                    if (matchedTransaction.containsExpense(expense)) {
                        unmatchedExpenses.remove(expense);
                    }
                }
            }
        }
        return unmatchedExpenses;
    }
}
