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

    public List<MatchedTransaction> getExactMatchedTransactions() {
        return exactMatchedTransactions;
    }

    public List<MatchedTransaction> getCloselyMatchedTransactions() {
        return closelyMatchedTransactions;
    }

    public void processTransactions() {
        this.exactMatchedTransactions = createExactMatchedTransactions();
        this.closelyMatchedTransactions = createCloselyMatchedTransactions();
        this.unmatchedExpenses = collectUnmatchedExpenses();
        this.unmatchedCreditCardActivies = collectUnmatchedCreditCardActivities();
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

    private List<CreditCardActivity> collectUnmatchedCreditCardActivities() {
        List<CreditCardActivity> unmatchedCreditCardActivities = cleanOutUnmatchedCreditCardActivities(this.creditCardActivities, this.exactMatchedTransactions);
        unmatchedCreditCardActivities = cleanOutUnmatchedCreditCardActivities(unmatchedCreditCardActivities, this.closelyMatchedTransactions);
        return unmatchedCreditCardActivities;
    }

    private List<CreditCardActivity> cleanOutUnmatchedCreditCardActivities(List<CreditCardActivity> creditCardActivities, List<MatchedTransaction> matchedTransactions) {
        List<CreditCardActivity> unmamtchedCreditCardActivities = new ArrayList<>(creditCardActivities);
        for (MatchedTransaction matchedTransaction : matchedTransactions) {
            CreditCardActivity matchedCreditCardActivity = matchedTransaction.getMatchedCreditCardActivity();
            if (unmamtchedCreditCardActivities.contains(matchedCreditCardActivity)) {
                unmamtchedCreditCardActivities.remove(matchedCreditCardActivity);
            }
        }
        return unmamtchedCreditCardActivities;
    }

    private List<Expense> collectUnmatchedExpenses() {
        List<Expense> unmatchedExpenses = cleanOutUnmatchedExpenses(this.expenses, this.exactMatchedTransactions);
        unmatchedExpenses = cleanOutUnmatchedExpenses(unmatchedExpenses, this.closelyMatchedTransactions);
        return unmatchedExpenses;
    }

    private List<Expense> cleanOutUnmatchedExpenses(List<Expense> expenses, List<MatchedTransaction> matchedTransactions) {
        List<Expense> unmatchedExpenses = new ArrayList<>(expenses);
        for (MatchedTransaction matchedTransaction : matchedTransactions) {
            Expense matchedExpense = matchedTransaction.getMatchedExpense();
            if (unmatchedExpenses.contains(matchedExpense)) {
                unmatchedExpenses.remove(matchedExpense);
            }
        }
        return unmatchedExpenses;
    }

    private List<MatchedTransaction> createCloselyMatchedTransactions() {
        List<CreditCardActivity> unmatchedCreditCardActivities = cleanOutUnmatchedCreditCardActivities(this.creditCardActivities, this.exactMatchedTransactions);
        List<Expense> unmatchedExpenses = cleanOutUnmatchedExpenses(this.expenses, this.exactMatchedTransactions);

        List<MatchedTransaction> closelyMatchedTransactions = new ArrayList<>();
        for (Expense expense : unmatchedExpenses) {
            for (CreditCardActivity creditCardActivity : unmatchedCreditCardActivities) {
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

    private boolean isMatchedExactly(Expense expense, CreditCardActivity creditCardActivity) {
        BigDecimal creditCardActivityAmount = creditCardActivity.getAmount();
        double positiveCreditCardActivityAmount = Math.abs(creditCardActivityAmount.doubleValue());
        double expenseAmount = expense.getAmount().doubleValue();

        DateTime expenseDate = expense.getTimestamp();

        DateTime creditCardActivityTransactionDate = creditCardActivity.getTransactionDate();
        return expenseAmount == positiveCreditCardActivityAmount && expenseDate.equals(creditCardActivityTransactionDate);
    }
}
