package org.spargonaut;

import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.matchers.PreviousMatchDetector;
import org.spargonaut.matchers.TransactionMatcher;
import org.spargonaut.matchers.UnmatchedCollector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TransactionProcessor {

    private final Set<Expense> expenses;
    private Set<CreditCardActivity> creditCardActivities;
    private Map<String, Set<MatchedTransaction>> matchedTransactions;

    public TransactionProcessor(Set<CreditCardActivity> creditCardActivities, Set<Expense> expenses) {
        this.creditCardActivities = creditCardActivities;
        this.expenses = expenses;
        this.matchedTransactions = new HashMap<>();
    }

    public Set<CreditCardActivity> getUnmatchedCreditCardActivies() {
        UnmatchedCollector<CreditCardActivity> unmatchedCollector = new UnmatchedCollector<>();
        return unmatchedCollector.collect(this.creditCardActivities, this.matchedTransactions.values());
    }

    public Set<Expense> getUnmatchedExpenses() {
        UnmatchedCollector<Expense> unmatchedCollector = new UnmatchedCollector<>();
        return unmatchedCollector.collect(this.expenses, this.matchedTransactions.values());
    }

    public Map<String, Set<MatchedTransaction>> getMatchedTransactions() {
        return this.matchedTransactions;
    }

    public void processTransactions(Set<TransactionMatcher> matchers) {
        matchers.forEach( matcher -> {
            Set<MatchedTransaction> matchedTransactionList = createMatchedTransactions(matcher);
            this.matchedTransactions.put(matcher.getType(), matchedTransactionList);
        });
    }

    private Set<MatchedTransaction> createMatchedTransactions(TransactionMatcher matcher) {
        Set<MatchedTransaction> matchedTransactions = new HashSet<>();
        PreviousMatchDetector previousMatchDetector = new PreviousMatchDetector();

        for (Expense expense : this.getUnmatchedExpenses()) {
            for (CreditCardActivity creditCardActivity : this.getUnmatchedCreditCardActivies()) {
                boolean isMatch = matcher.isMatch(expense, creditCardActivity);
                boolean creditCardActivityIsPreviouslyMatched = previousMatchDetector.isPreviouslyMatched(creditCardActivity, new HashSet<>(matchedTransactions));
                boolean expenseIsPreviouslyMatched = previousMatchDetector.isPreviouslyMatched(expense, new HashSet<>(matchedTransactions));

                if (isMatch && !creditCardActivityIsPreviouslyMatched && !expenseIsPreviouslyMatched) {
                    matchedTransactions.add(new MatchedTransaction(creditCardActivity, expense));
                }
            }
        }
        return matchedTransactions;
    }
}