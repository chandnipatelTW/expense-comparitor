package org.spargonaut;

import org.joda.time.DateTime;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionMatcher {
    private final List<Expense> expenses;
    private List<CreditCardActivity> creditCardActivities;
    private List<CreditCardActivity> unmatchedCreditCardActivies;
    private List<Expense> unmatchedExpenses;
    private List<MatchedTransaction> exactMatchedTransactions;
    private List<MatchedTransaction> closelyMatchedTransactions;

    public TransactionMatcher(List<CreditCardActivity> creditCardActivities, List<Expense> expenses) {
        this.creditCardActivities = creditCardActivities;
        this.expenses = expenses;
    }

    public List<CreditCardActivity> getUnmatchedCreditCardActivies() {
        return this.unmatchedCreditCardActivies;
    }

    public List<Expense> getUnmatchedExpenses() {
        return this.unmatchedExpenses;
    }

    public void processTransactions() {
        this.unmatchedExpenses = new ArrayList<>(this.expenses);
        this.unmatchedCreditCardActivies = new ArrayList<>(this.creditCardActivities);

        this.exactMatchedTransactions = createExactMatchedTransactions();
        this.closelyMatchedTransactions = createCloselyMatchedTransactions();
    }

    private List<MatchedTransaction> createCloselyMatchedTransactions() {
        List<CreditCardActivity> remainingCreditCardActivities = new ArrayList<>();
        for (CreditCardActivity creditCardActivity : creditCardActivities) {
            if (!exactMatchedTransactions.contains(creditCardActivity)) {
                remainingCreditCardActivities.add(creditCardActivity);
            }
        }

        List<MatchedTransaction> closelyMatchedTransactions = new ArrayList<>();
        for (Expense expense : this.expenses) {
            for (CreditCardActivity creditCardActivity : remainingCreditCardActivities) {
                if(isMatchedClosely(expense, creditCardActivity)) {
                    MatchedTransaction matchedTransaction = new MatchedTransaction(creditCardActivity, expense);
                    closelyMatchedTransactions.add(matchedTransaction);
                }
            }
        }
        return closelyMatchedTransactions;
    }

    private boolean isMatchedClosely(Expense expense, CreditCardActivity creditCardActivity) {
        BigDecimal creditCardActivityAmount = creditCardActivity.getAmount();
        double positiveCreditCardActivityAmount = Math.abs(creditCardActivityAmount.doubleValue());
        double expenseAmount = expense.getAmount().doubleValue();

        DateTime expenseDate = expense.getTimestamp();

        DateTime transactionDate = creditCardActivity.getTransactionDate();

        DateTime dayBeforeTransactionDate = transactionDate.minusDays(1);
        DateTime dayAfterTransactionDate = transactionDate.plusDays(1);

        boolean dateIsWithinTolerance = expenseDate.equals(dayBeforeTransactionDate) || expenseDate.equals(dayAfterTransactionDate);

        return expenseAmount == positiveCreditCardActivityAmount && dateIsWithinTolerance;
    }

    private List<MatchedTransaction> createExactMatchedTransactions() {
        List<MatchedTransaction> exactMatchedTransactions = new ArrayList<>();
        for (Expense expense : this.expenses) {
            for (CreditCardActivity creditCardActivity : creditCardActivities) {
                if(isMatchedExactly(expense, creditCardActivity)) {
                    MatchedTransaction matchedTransaction = new MatchedTransaction(creditCardActivity, expense);
                    exactMatchedTransactions.add(matchedTransaction);
                }
            }
        }
        return exactMatchedTransactions;
    }

    private boolean isMatchedExactly(Expense expense, CreditCardActivity creditCardActivity) {
        BigDecimal creditCardActivityAmount = creditCardActivity.getAmount();
        double positiveCreditCardActivityAmount = Math.abs(creditCardActivityAmount.doubleValue());
        double expenseAmount = expense.getAmount().doubleValue();

        DateTime expenseDate = expense.getTimestamp();

        DateTime creditCardActivityTransactionDate = creditCardActivity.getTransactionDate();
        return expenseAmount == positiveCreditCardActivityAmount && expenseDate.equals(creditCardActivityTransactionDate);
    }

    public List<MatchedTransaction> getExactMatchedTransactions() {
        return exactMatchedTransactions;
    }

    public List<MatchedTransaction> getCloselyMatchedTransactions() {
        return closelyMatchedTransactions;
    }
}
