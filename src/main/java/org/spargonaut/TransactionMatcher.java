package org.spargonaut;

import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;

import java.math.BigDecimal;
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
                BigDecimal creditCardActivityAmount = creditCardActivity.getAmount();
                double positiveCreditCardActivityAmount = Math.abs(creditCardActivityAmount.doubleValue());
                double expenseAmount = expense.getAmount().doubleValue();
                if (expenseAmount == positiveCreditCardActivityAmount) {
                    matchedTransactions.add(new MatchedTransaction(creditCardActivity, expense));
                }
            }
        }

        return matchedTransactions;
    }
}
