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
import java.util.function.Predicate;

public class TransactionProcessor {

    class CreditCardPredicate implements Predicate<CreditCardActivity> {

        TransactionMatcher transactionMatcher;
        private final Expense expense;
        private final List<MatchedTransaction> matchedTransactions;

        public CreditCardPredicate(TransactionMatcher transactionMatcher, Expense expense, List<MatchedTransaction> matchedTransactions) {
            this.transactionMatcher = transactionMatcher;
            this.expense = expense;
            this.matchedTransactions = matchedTransactions;
        }

        @Override
        public boolean test(CreditCardActivity creditCardActivity) {
            boolean isMatched = transactionMatcher.isMatch(this.expense, creditCardActivity);
            boolean expense = expenseIsNotPreviouslyMatched(this.expense, matchedTransactions);
            boolean creditCardActivityIsNotPreviouslyMatched = creditCardActivityIsNotPreviouslyMatched(creditCardActivity, matchedTransactions);
            return isMatched && expense && creditCardActivityIsNotPreviouslyMatched;
        }
    }


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
        UnmatchedCollector<CreditCardActivity> unmatchedCreditCardActivityCollector = new UnmatchedCollector<>();
        UnmatchedCollector<Expense> unmatchedExpenseCollector = new UnmatchedCollector<>();
        for (TransactionMatcher matcher : matchers) {
            List<Expense> unmatchedExpenses = unmatchedExpenseCollector.collect(this.expenses, this.matchedTransactions.values());
            List<CreditCardActivity> unmatchedCreditCardActivities = unmatchedCreditCardActivityCollector.collect(this.creditCardActivities, this.matchedTransactions.values());
            List<MatchedTransaction> matchedTransactionList = createMatchedTransactions(unmatchedCreditCardActivities, unmatchedExpenses, matcher);
            this.matchedTransactions.put(matcher.getType(), matchedTransactionList);
        }
    }

    private List<MatchedTransaction> createMatchedTransactions(List<CreditCardActivity> creditCardActivities, List<Expense> expenses, TransactionMatcher matcher) {
        List<MatchedTransaction> matchedTransactions = new ArrayList<>();
        PreviousMatchDetector previousMatchDetector = new PreviousMatchDetector();

        for (Expense expense : expenses) {
            for (CreditCardActivity creditCardActivity : creditCardActivities) {
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

    private boolean expenseIsNotPreviouslyMatched(Expense expense, List<MatchedTransaction> matchedTransactions) {
        boolean isMatched = false;
        for (MatchedTransaction matchedTransaction : matchedTransactions) {
            isMatched = matchedTransaction.contains(expense) || isMatched;
        }
        return !isMatched;
    }

    private boolean creditCardActivityIsNotPreviouslyMatched(CreditCardActivity creditCardActivity, List<MatchedTransaction> matchedTransactions) {
        boolean isMatched = false;
        for (MatchedTransaction matchedTransaction : matchedTransactions) {
            isMatched = matchedTransaction.contains(creditCardActivity) || isMatched;
        }
        return !isMatched;
    }
}