package org.spargonaut;

import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.matchers.PreviousMatchDetector;
import org.spargonaut.matchers.TransactionMatcher;
import org.spargonaut.matchers.UnmatchedCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        UnmatchedCollector<CreditCardActivity> unmatchedCollector = new UnmatchedCollector<>();
        return unmatchedCollector.collect(this.creditCardActivities, this.matchedTransactions.values());
    }

    public List<Expense> getUnmatchedExpenses() {
        UnmatchedCollector<Expense> unmatchedCollector = new UnmatchedCollector<>();
        return unmatchedCollector.collect(this.expenses, this.matchedTransactions.values());
    }

    public Map<String, List<MatchedTransaction>> getMatchedTransactions() {
        return this.matchedTransactions;
    }

    public void processTransactions(List<TransactionMatcher> matchers) {
        for (TransactionMatcher matcher : matchers) {
            List<MatchedTransaction> matchedTransactionList = createMatchedTransactions(matcher);
            this.matchedTransactions.put(matcher.getType(), matchedTransactionList);
        }
    }

    private List<MatchedTransaction> createMatchedTransactions(TransactionMatcher matcher) {
        List<MatchedTransaction> matchedTransactions = new ArrayList<>();
        PreviousMatchDetector previousMatchDetector = new PreviousMatchDetector();

        for (Expense expense : this.getUnmatchedExpenses()) {
            for (CreditCardActivity creditCardActivity : this.getUnmatchedCreditCardActivies()) {
                boolean isMatch = matcher.isMatch(expense, creditCardActivity);
                boolean creditCardActivityIsPreviouslyMatched = previousMatchDetector.isPreviouslyMatched(creditCardActivity, matchedTransactions);
                boolean expenseIsPreviouslyMatched = previousMatchDetector.isPreviouslyMatched(expense, matchedTransactions);

                if (isMatch && !creditCardActivityIsPreviouslyMatched && !expenseIsPreviouslyMatched) {
                    matchedTransactions.add(new MatchedTransaction(creditCardActivity, expense));
                }
            }
        }
        return matchedTransactions;
    }
}