package org.spargonaut;

import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionMatcher {
    private List<CreditCardActivity> creditCardActivities;
    private List<CreditCardActivity> unmatchedCreditCardActivies;
    private List<MatchedTransaction> matchedTransactions;

    public TransactionMatcher(List<CreditCardActivity> creditCardActivities) {
        this.creditCardActivities = creditCardActivities;
    }

    public List<MatchedTransaction> createMatchedTransactionsWithExpenses(List<Expense> expenses) {
        matchedTransactions = new ArrayList<>();

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

    public List<CreditCardActivity> getUnmatchedCreditCardActivies() {
        this.unmatchedCreditCardActivies = new ArrayList<>();

        for (CreditCardActivity creditCardActivity : creditCardActivities) {
            boolean isMatched = false;
            for (MatchedTransaction matchedTransaction : matchedTransactions) {
                CreditCardActivity matchedCreditCardActivity = matchedTransaction.getMatchedCreditCardActivity();
                if (creditCardActivity.equals(matchedCreditCardActivity)) {
                    isMatched = true;
                }
            }

            if (!isMatched) {
                unmatchedCreditCardActivies.add(creditCardActivity);
            }
        }

        return unmatchedCreditCardActivies;
    }
}
