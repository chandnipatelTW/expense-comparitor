package org.spargonaut;

import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.matchers.CloseDateMatcher;
import org.spargonaut.matchers.ExactMatcher;
import org.spargonaut.matchers.TransactionMatcher;

import java.util.ArrayList;
import java.util.List;

public class TransactionProcessor {
    private final List<Expense> expenses;
    private List<CreditCardActivity> creditCardActivities;
    private List<CreditCardActivity> unmatchedCreditCardActivies;
    private List<Expense> unmatchedExpenses;
    private List<MatchedTransaction> exactMatchedTransactions;
    private List<MatchedTransaction> closelyMatchedTransactions;

    public TransactionProcessor(List<CreditCardActivity> creditCardActivities, List<Expense> expenses) {
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

        this.exactMatchedTransactions = createMatchedTransactions(this.creditCardActivities, this.expenses, new ExactMatcher());
        matchedTransactionLists.add(exactMatchedTransactions);

        List<CreditCardActivity> unmatchedCreditCardActivities = collectUnmatchedCreditCardActivities(this.creditCardActivities, matchedTransactionLists);
        List<Expense> unmatchedExpenses = collectUnmatchedExpenses(this.expenses, matchedTransactionLists);

        this.closelyMatchedTransactions = createMatchedTransactions(unmatchedCreditCardActivities, unmatchedExpenses, new CloseDateMatcher());
        matchedTransactionLists.add(this.closelyMatchedTransactions);

        this.unmatchedExpenses = collectUnmatchedExpenses(this.expenses, matchedTransactionLists);
        this.unmatchedCreditCardActivies = collectUnmatchedCreditCardActivities(this.creditCardActivities, matchedTransactionLists);
    }

    private List<MatchedTransaction> createMatchedTransactions(List<CreditCardActivity> creditCardActivities, List<Expense> expenses, TransactionMatcher matcher) {
        List<MatchedTransaction> matchedTransactions = new ArrayList<>();
        for (Expense expense : expenses) {
            for (CreditCardActivity creditCardActivity : creditCardActivities) {
                if(matcher.isMatch(expense, creditCardActivity)) {
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
}
