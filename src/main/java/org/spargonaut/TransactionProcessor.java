package org.spargonaut;

import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.matchers.CloseDateMatcher;
import org.spargonaut.matchers.ExactMatcher;
import org.spargonaut.matchers.TransactionMatcher;

import java.util.*;

public class TransactionProcessor {
    private final List<Expense> expenses;
    private List<CreditCardActivity> creditCardActivities;
    private Map<String, List<MatchedTransaction>> matchedTransactions;

    public TransactionProcessor(List<CreditCardActivity> creditCardActivities, List<Expense> expenses) {
        this.creditCardActivities = creditCardActivities;
        this.expenses = expenses;
        this.matchedTransactions = new HashMap<>();
    }

    public List<CreditCardActivity> getUnmatchedCreditCardActivies() {
        return collectUnmatchedCreditCardActivities(this.creditCardActivities, this.matchedTransactions.values());
    }

    public List<Expense> getUnmatchedExpenses() {
        return collectUnmatchedExpenses(this.expenses, this.matchedTransactions.values());
    }

    public List<MatchedTransaction> getExactMatchedTransactions() {
        ExactMatcher exactMatcher = new ExactMatcher();
        return matchedTransactions.get(exactMatcher.getType());
    }

    public List<MatchedTransaction> getCloselyMatchedTransactions() {
        CloseDateMatcher closeDateMatcher = new CloseDateMatcher();
        return matchedTransactions.get(closeDateMatcher.getType());
    }

    public Map<String, List<MatchedTransaction>> getMatchedTransactions() {
        return null;
    }

    public void processTransactions() {
        List<TransactionMatcher> matchers = Arrays.asList(new ExactMatcher(), new CloseDateMatcher());

        for (TransactionMatcher matcher : matchers) {
            List<Expense> unmatchedExpenses = collectUnmatchedExpenses(this.expenses, this.matchedTransactions.values());
            List<CreditCardActivity> unmatchedCreditCardActivities = collectUnmatchedCreditCardActivities(this.creditCardActivities, this.matchedTransactions.values());

            List<MatchedTransaction> matchedTransactionList = createMatchedTransactions(unmatchedCreditCardActivities, unmatchedExpenses, matcher);
            this.matchedTransactions.put(matcher.getType(), matchedTransactionList);
        }
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

    private List<CreditCardActivity> collectUnmatchedCreditCardActivities(List<CreditCardActivity> creditCardActivities, Collection<List<MatchedTransaction>> matchedTransactionList) {
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

    private List<Expense> collectUnmatchedExpenses(List<Expense> expenses, Collection<List<MatchedTransaction>> matchedTransactionLists) {
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
