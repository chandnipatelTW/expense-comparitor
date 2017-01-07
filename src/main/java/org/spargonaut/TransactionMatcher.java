package org.spargonaut;

import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;

import java.util.ArrayList;
import java.util.List;

public class TransactionMatcher {
    private List<CreditCardActivity> creditCardActivities;

    public TransactionMatcher(List<CreditCardActivity> creditCardActivities) {
        this.creditCardActivities = creditCardActivities;
    }

    public List<MatchedTransaction> createMatchedTransactionsWithExpenses(List<Expense> expenses) {
        List<MatchedTransaction> matchedTransactions = new ArrayList<>();

        for (Expense expense : expenses) {
            for (CreditCardActivity creditCardActivity : creditCardActivities) {
                if (creditCardActivity.getAmount().equals(expense.getAmount())) {
                    matchedTransactions.add(new MatchedTransaction(creditCardActivity, expense));
                }
            }
        }

        return matchedTransactions;
    }
}
