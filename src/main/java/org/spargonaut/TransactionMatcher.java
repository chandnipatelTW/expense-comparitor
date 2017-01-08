package org.spargonaut;

import org.joda.time.DateTime;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;

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
                if(isMatched(expense, creditCardActivity)) {
                    matchedTransactions.add(new MatchedTransaction(creditCardActivity, expense));
                }
            }
        }

        return matchedTransactions;
    }

    private boolean isMatched(Expense expense, CreditCardActivity creditCardActivity) {
        BigDecimal creditCardActivityAmount = creditCardActivity.getAmount();
        double positiveCreditCardActivityAmount = Math.abs(creditCardActivityAmount.doubleValue());
        double expenseAmount = expense.getAmount().doubleValue();

        DateTime expenseDate = expense.getTimestamp();

        DateTime creditCardActivityTransactionDate = creditCardActivity.getTransactionDate();
        DateTime dateBeforeCreditCardActivityTransactionDate = creditCardActivityTransactionDate.minusDays(1);
        DateTime dateAfterCreditCardActivityTransactionDate = creditCardActivityTransactionDate.plusDays(1);

        boolean isWithinDateTolerance = (expenseDate.equals(creditCardActivityTransactionDate) ||
                                         expenseDate.equals(dateBeforeCreditCardActivityTransactionDate) ||
                                         expenseDate.equals(dateAfterCreditCardActivityTransactionDate));

        return expenseAmount == positiveCreditCardActivityAmount && isWithinDateTolerance;
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

    public List<Expense> getUnmatchedExpenses(List<Expense> expenses) {
        List<Expense> unmatchedExpenses = new ArrayList<>();

        for (Expense expense : expenses) {
            boolean isMatched = false;
            for (MatchedTransaction matchedTransaction : matchedTransactions) {
                Expense matchedExpense = matchedTransaction.getMatchedExpense();
                if (expense.equals(matchedExpense)) {
                    isMatched = true;
                }
            }

            if (!isMatched) {
                unmatchedExpenses.add(expense);
            }
        }
        return unmatchedExpenses;
    }
}
