package org.spargonaut;

import org.joda.time.DateTime;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.matchers.CloseDateMatcher;
import org.spargonaut.matchers.ExactMatcher;

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
        List<List<MatchedTransaction>> matchedTransactionLists = new ArrayList<>();

        this.exactMatchedTransactions = createExactMatchedTransactions(this.creditCardActivities, this.expenses);
        matchedTransactionLists.add(exactMatchedTransactions);

        List<CreditCardActivity> unmatchedCreditCardActivities = collectUnmatchedCreditCardActivities(this.creditCardActivities, matchedTransactionLists);
        List<Expense> unmatchedExpenses = collectUnmatchedExpenses(this.expenses, matchedTransactionLists);

        this.closelyMatchedTransactions = createCloselyMatchedTransactions(unmatchedCreditCardActivities, unmatchedExpenses);
        matchedTransactionLists.add(this.closelyMatchedTransactions);

        this.unmatchedExpenses = collectUnmatchedExpenses(this.expenses, matchedTransactionLists);
        this.unmatchedCreditCardActivies = collectUnmatchedCreditCardActivities(this.creditCardActivities, matchedTransactionLists);
    }

    private List<MatchedTransaction> createExactMatchedTransactions(List<CreditCardActivity> creditCardActivities, List<Expense> expenses) {
        List<MatchedTransaction> matchedTransactions = new ArrayList<>();
        for (Expense expense : expenses) {
            for (CreditCardActivity creditCardActivity : creditCardActivities) {
                if(ExactMatcher.isMatch(expense, creditCardActivity)) {
                    matchedTransactions.add(new MatchedTransaction(creditCardActivity, expense));
                }
            }
        }
        return matchedTransactions;
    }

    private List<MatchedTransaction> createCloselyMatchedTransactions(List<CreditCardActivity> creditCardActivities, List<Expense> expenses) {
        List<MatchedTransaction> matchedTransactions = new ArrayList<>();
        for (Expense expense : expenses) {
            for (CreditCardActivity creditCardActivity : creditCardActivities) {
                if(CloseDateMatcher.isMatch(expense, creditCardActivity)) {
                    matchedTransactions.add(new MatchedTransaction(creditCardActivity, expense));
                }
            }
        }
        return matchedTransactions;
    }

    private List<CreditCardActivity> collectUnmatchedCreditCardActivities(List<CreditCardActivity> creditCardActivities, List<List<MatchedTransaction>> matchedTransactionList) {
        List<CreditCardActivity> unmamtchedCreditCardActivities = new ArrayList<>(creditCardActivities);
        for (List<MatchedTransaction> matchedTransactions : matchedTransactionList) {
            for (MatchedTransaction matchedTransaction : matchedTransactions) {
                CreditCardActivity matchedCreditCardActivity = matchedTransaction.getMatchedCreditCardActivity();
                if (unmamtchedCreditCardActivities.contains(matchedCreditCardActivity)) {
                    unmamtchedCreditCardActivities.remove(matchedCreditCardActivity);
                }
            }
        }
        return unmamtchedCreditCardActivities;
    }

    private List<Expense> collectUnmatchedExpenses(List<Expense> expenses, List<List<MatchedTransaction>> matchedTransactionLists) {
        List<Expense> unmatchedExpenses = new ArrayList<>(expenses);
        for (List<MatchedTransaction> matchedTransactions : matchedTransactionLists) {
            for (MatchedTransaction matchedTransaction : matchedTransactions) {
                Expense matchedExpense = matchedTransaction.getMatchedExpense();
                if (unmatchedExpenses.contains(matchedExpense)) {
                    unmatchedExpenses.remove(matchedExpense);
                }
            }
        }
        return unmatchedExpenses;
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
}
