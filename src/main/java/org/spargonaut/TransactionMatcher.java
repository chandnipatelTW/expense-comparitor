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

    public List<MatchedTransaction> createMatchedTransactionsWithExpense(Expense expense) {
        List<MatchedTransaction> matchedTransactions = new ArrayList<>();

        for (CreditCardActivity creditCardActivity : creditCardActivities) {
            if (creditCardActivity.getAmount().equals(expense.getAmount())) {
                matchedTransactions.add(new MatchedTransaction(creditCardActivity, expense));
            }
        }

        return matchedTransactions;
    }
}
