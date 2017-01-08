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
    private List<MatchedTransaction> matchedTransactions;

    public TransactionMatcher(List<CreditCardActivity> creditCardActivities, List<Expense> expenses) {
        this.creditCardActivities = creditCardActivities;
        this.expenses = expenses;
    }

    public void process() {
        createMatchedTransactions();
    }

    public List<MatchedTransaction> getMatchedTransactions() {
        return this.matchedTransactions;
    }

    public List<CreditCardActivity> getUnmatchedCreditCardActivies() {
        return this.unmatchedCreditCardActivies;
    }

    public List<Expense> getUnmatchedExpenses() {
        return this.unmatchedExpenses;
    }

    private void createMatchedTransactions() {
        this.matchedTransactions = new ArrayList<>();
        this.unmatchedExpenses = new ArrayList<>(this.expenses);
        this.unmatchedCreditCardActivies = new ArrayList<>(this.creditCardActivities);

        for (Expense expense : this.expenses) {
            for (CreditCardActivity creditCardActivity : creditCardActivities) {
                if(isMatched(expense, creditCardActivity)) {
                    matchedTransactions.add(new MatchedTransaction(creditCardActivity, expense));

                    if (this.unmatchedCreditCardActivies.contains(creditCardActivity)) {
                        int indexOfCreditCardActivity = this.unmatchedCreditCardActivies.indexOf(creditCardActivity);
                        this.unmatchedCreditCardActivies.remove(indexOfCreditCardActivity);
                    }

                    if (this.unmatchedExpenses.contains(expense)) {
                        int indexOfExpense = this.unmatchedExpenses.indexOf(expense);
                        this.unmatchedExpenses.remove(indexOfExpense);
                    }
                }
            }
        }
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
}
