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

    public MatchedTransaction createMatchedTransactionsWithExpense(Expense expense) {
        CreditCardActivity creditCardActivity = creditCardActivities.get(0);

        MatchedTransaction matchedTransaction = null;
        if (creditCardActivity.getAmount().equals(expense.getAmount())) {
            matchedTransaction = new MatchedTransaction(creditCardActivity, expense);
        }

        return matchedTransaction;
    }
}
