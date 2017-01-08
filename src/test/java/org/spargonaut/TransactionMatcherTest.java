package org.spargonaut;

import org.joda.time.DateTime;
import org.junit.Test;
import org.spargonaut.datamodels.ActivityType;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.datamodels.testbuilders.CreditCardActivityBuilder;
import org.spargonaut.datamodels.testbuilders.ExpenseBuilder;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TransactionMatcherTest {

    @Test
    public void shouldCreateATransactionMatch_whenACreditCardActivityHasTheSameAmountAndTransactionDateAsAnExpenseEntry() {
        int yearToMatchOn = 2016;
        int monthOfYearToMatchOn = 12;

        int dayOfMonthForPostDate = 30;
        int dayOfMonthForTransactionDateToMatchOn = 31;
        int dayOfMonthForAnotherTransactionDate = 28;

        DateTime postDateToMatchOn = new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthForPostDate, 0, 0);
        DateTime transactionDateToMatchOn = new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthForTransactionDateToMatchOn, 0, 0);
        DateTime transactionDateOfDifferentCreditCardActivity = new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthForAnotherTransactionDate, 0, 0);

        String descriptionToMatchOn = "this is a description for a matched amount";
        double amountToMatchOn = 3.38;
        double amountToMatchOnForCreditCardActivityOne = amountToMatchOn * -1;
        double amountForCreditCardActivityTwo = 5.56;

        CreditCardActivity creditCardActivityOne = new CreditCardActivityBuilder()
                .setAmount(amountToMatchOnForCreditCardActivityOne)
                .setDescription(descriptionToMatchOn)
                .setPostDate(postDateToMatchOn)
                .setTransactionDate(transactionDateToMatchOn)
                .setType(ActivityType.SALE)
                .build();

        CreditCardActivity creditCardActivityTwo = new CreditCardActivityBuilder()
                .setAmount(amountForCreditCardActivityTwo)
                .build();

        CreditCardActivity creditCardActivityThree = new CreditCardActivityBuilder()
                .setAmount(amountToMatchOnForCreditCardActivityOne)
                .setDescription(descriptionToMatchOn)
                .setPostDate(postDateToMatchOn)
                .setTransactionDate(transactionDateOfDifferentCreditCardActivity)
                .setType(ActivityType.SALE)
                .build();

        String merchant = "some merchant";
        Expense expenseOne = new ExpenseBuilder()
                .setMerchant(merchant)
                .setTimestamp(transactionDateToMatchOn)
                .setAmount(amountToMatchOn)
                .setTimestamp(transactionDateToMatchOn)
                .setMerchant(merchant)
                .build();

        List<CreditCardActivity> creditCardActivitiesForTesting = Arrays.asList(creditCardActivityTwo, creditCardActivityOne, creditCardActivityThree);
        TransactionMatcher transactionMatcher = new TransactionMatcher(creditCardActivitiesForTesting);

        List<Expense> expenses = Arrays.asList(expenseOne);

        List<MatchedTransaction> matchedTransactions = transactionMatcher.createMatchedTransactionsWithExpenses(expenses);
        assertThat(matchedTransactions.size(), is(1));

        CreditCardActivity expectedCreditCardActivityMatch = new CreditCardActivityBuilder()
                .setAmount(amountToMatchOnForCreditCardActivityOne)
                .setDescription(descriptionToMatchOn)
                .setPostDate(new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthForPostDate, 0, 0))
                .setTransactionDate(new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthForTransactionDateToMatchOn, 0, 0))
                .build();

        MatchedTransaction matchedTransaction = matchedTransactions.get(0);
        CreditCardActivity matchedCreditCardActivity = matchedTransaction.getMatchedCreditCardActivity();
        assertThat(matchedCreditCardActivity.equals(expectedCreditCardActivityMatch), is(true));

        Expense expectedExpenseMatch = new ExpenseBuilder()
                .setMerchant(merchant)
                .setTimestamp(transactionDateToMatchOn)
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

    @Test
    public void shouldCreateAListOfCreditCardActivitiesThatHaveNotBeenMatched() {
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
        transactionMatcher.createMatchedTransactionsWithExpenses(expenses);

        List<CreditCardActivity> unmatchedCreditCardActivities =  transactionMatcher.getUnmatchedCreditCardActivies();

        assertThat(unmatchedCreditCardActivities.size(), is(1));

        CreditCardActivity unmatchedCreditCardActivity = unmatchedCreditCardActivities.get(0);
        assertThat(unmatchedCreditCardActivity, is(creditCardActivity));
    }

    @Test
    public void shouldCreateAListOfExpensesThatAreUnmatched() {
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
        transactionMatcher.createMatchedTransactionsWithExpenses(expenses);

        List<Expense> unmatchedExpenses =  transactionMatcher.getUnmatchedExpenses(expenses);

        assertThat(unmatchedExpenses.size(), is(1));

        Expense unmatchedExpense = unmatchedExpenses.get(0);
        assertThat(unmatchedExpense, is(expense));
    }

    @Test
    public void shouldCreateATransactionMatch_whenExpenseDateEqualsOneDayBeforeTransactionDate() {
        int yearToMatchOn = 2016;
        int monthOfYearToMatchOn = 12;

        int dayOfMonthForPostDate = 23;
        int dayOfMonthForExpenseDateToMatchOn = 24;
        int dayOfMonthForTransactionDateToMatchOn = 25;
        int dayOfMonthForAnotherTransactionDate = 20;

        DateTime postDate = new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthForPostDate, 0, 0);
        DateTime expenseDateToMatchOn = new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthForExpenseDateToMatchOn, 0, 0);
        DateTime transactionDateToMatchOn = new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthForTransactionDateToMatchOn, 0, 0);
        DateTime transactionDateOfDifferentCreditCardActivity = new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthForAnotherTransactionDate, 0, 0);

        String descriptionToMatchOn = "this is a description for a matched amount";
        double amountToMatchOn = 3.38;
        double amountToMatchOnForCreditCardActivityOne = amountToMatchOn * -1;
        double amountForCreditCardActivityTwo = 5.56;

        CreditCardActivity creditCardActivityOne = new CreditCardActivityBuilder()
                .setAmount(amountToMatchOnForCreditCardActivityOne)
                .setDescription(descriptionToMatchOn)
                .setPostDate(postDate)
                .setTransactionDate(transactionDateToMatchOn)
                .setType(ActivityType.SALE)
                .build();

        CreditCardActivity creditCardActivityTwo = new CreditCardActivityBuilder()
                .setAmount(amountForCreditCardActivityTwo)
                .build();

        CreditCardActivity creditCardActivityThree = new CreditCardActivityBuilder()
                .setAmount(amountToMatchOnForCreditCardActivityOne)
                .setDescription(descriptionToMatchOn)
                .setPostDate(postDate)
                .setTransactionDate(transactionDateOfDifferentCreditCardActivity)
                .setType(ActivityType.SALE)
                .build();

        String merchant = "some merchant";
        Expense expenseOne = new ExpenseBuilder()
                .setMerchant(merchant)
                .setTimestamp(expenseDateToMatchOn)
                .setAmount(amountToMatchOn)
                .build();

        List<CreditCardActivity> creditCardActivitiesForTesting = Arrays.asList(creditCardActivityTwo, creditCardActivityOne, creditCardActivityThree);
        TransactionMatcher transactionMatcher = new TransactionMatcher(creditCardActivitiesForTesting);

        List<Expense> expenses = Arrays.asList(expenseOne);

        List<MatchedTransaction> matchedTransactions = transactionMatcher.createMatchedTransactionsWithExpenses(expenses);
        assertThat(matchedTransactions.size(), is(1));

        CreditCardActivity expectedCreditCardActivityMatch = new CreditCardActivityBuilder()
                .setAmount(amountToMatchOnForCreditCardActivityOne)
                .setDescription(descriptionToMatchOn)
                .setPostDate(new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthForPostDate, 0, 0))
                .setTransactionDate(new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthForTransactionDateToMatchOn, 0, 0))
                .build();

        MatchedTransaction matchedTransaction = matchedTransactions.get(0);
        CreditCardActivity matchedCreditCardActivity = matchedTransaction.getMatchedCreditCardActivity();
        assertThat(matchedCreditCardActivity.equals(expectedCreditCardActivityMatch), is(true));

        Expense expectedExpenseMatch = new ExpenseBuilder()
                .setMerchant(merchant)
                .setTimestamp(expenseDateToMatchOn)
                .setAmount(amountToMatchOn)
                .build();

        Expense matchedExpense = matchedTransaction.getMatchedExpense();
        assertThat(matchedExpense.equals(expectedExpenseMatch), is(true));
    }
}