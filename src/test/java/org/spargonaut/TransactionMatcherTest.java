package org.spargonaut;

import org.joda.time.DateTime;
import org.junit.Test;
import org.spargonaut.datamodels.ActivityType;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.testbuilders.CreditCardActivityBuilder;
import org.spargonaut.datamodels.testbuilders.ExpenseBuilder;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TransactionMatcherTest {

    @Test
    public void shouldCreateATransactionMatch_whenACreditCardActivityHasTheSameAmountAsAnExpenseEntry() {
        int yearToMatchOn = 2016;
        int monthOfYearToMatchOn = 12;
        int dayOfMonthForPostToMatchOn = 30;
        int dayOfMonthForTransactionToMatchOn = 31;
        String descriptionToMatchOn = "this is a description for a matched amount";
        double amountToMatchOn = 3.38;

        double amountToMatchOnForCreditCardActivityOne = amountToMatchOn * -1;

        DateTime postDateToMatchOn = new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthForPostToMatchOn, 0, 0);
        DateTime transactionDateToMatchOn = new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthForTransactionToMatchOn, 0, 0);
        CreditCardActivity creditCardActivityOne = new CreditCardActivityBuilder()
                .setAmount(amountToMatchOnForCreditCardActivityOne)
                .setDescription(descriptionToMatchOn)
                .setPostDate(postDateToMatchOn)
                .setTransactionDate(transactionDateToMatchOn)
                .setType(ActivityType.SALE)
                .build();

        double amountForCreditCardActivityTwo = 5.56;
        CreditCardActivity creditCardActivityTwo = new CreditCardActivityBuilder()
                .setAmount(amountForCreditCardActivityTwo)
                .build();

        Expense expenseOne = new ExpenseBuilder()
                .setAmount(amountToMatchOn)
                .build();

        List<CreditCardActivity> creditCardActivitiesForTesting = Arrays.asList(creditCardActivityTwo, creditCardActivityOne);
        TransactionMatcher transactionMatcher = new TransactionMatcher(creditCardActivitiesForTesting);

        List<Expense> expenses = Arrays.asList(expenseOne);

        List<MatchedTransaction> matchedTransactions = transactionMatcher.createMatchedTransactionsWithExpenses(expenses);
        assertThat(matchedTransactions.size(), is(1));

        CreditCardActivity expectedCreditCardActivityMatch = new CreditCardActivityBuilder()
                .setAmount(amountToMatchOnForCreditCardActivityOne)
                .setDescription(descriptionToMatchOn)
                .setPostDate(new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthForPostToMatchOn, 0, 0))
                .setTransactionDate(new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthForTransactionToMatchOn, 0, 0))
                .build();

        MatchedTransaction matchedTransaction = matchedTransactions.get(0);
        CreditCardActivity matchedCreditCardActivity = matchedTransaction.getMatchedCreditCardActivity();
        assertThat(matchedCreditCardActivity.equals(expectedCreditCardActivityMatch), is(true));

        Expense expectedExpenseMatch = new ExpenseBuilder()
                .setAmount(amountToMatchOn)
                .build();

        Expense matchedExpense = matchedTransaction.getMatchedExpense();
        assertThat(matchedExpense.equals(expectedExpenseMatch), is(true));
    }

    @Test
    public void shouldRejectATransactionMatch_whenACreditCardActivityHasADifferentAmountAsAnExpenseEntry() {
        double amountForCreditCardActivity = 5.56;
        CreditCardActivity creditCardActivity = new CreditCardActivityBuilder()
                .setAmount(amountForCreditCardActivity)
                .build();

        double amountForExpense = 4.45;
        Expense expense = new ExpenseBuilder()
                .setAmount(amountForExpense)
                .build();

        List<Expense> expenses = Arrays.asList(expense);

        TransactionMatcher transactionMatcher = new TransactionMatcher(Arrays.asList(creditCardActivity));
        List<MatchedTransaction> matchedTransactions = transactionMatcher.createMatchedTransactionsWithExpenses(expenses);

        assertThat(matchedTransactions.size(), is(0));
    }

}