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

    private final int yearToMatchOn = 2016;
    private final int monthOfYearToMatchOn = 12;
    private final double amountToMatchOn = 3.38;
    private final String descriptionToMatchOn = "this is a description for a matched amount";
    private final double amountToMatchOnForCreditCardActivityOne = amountToMatchOn * -1;
    private final String merchantToMatch = "some merchant";

    @Test
    public void shouldCreateATransactionMatch_whenACreditCardActivityHasTheSameAmountAndTransactionDateAsAnExpenseEntry() {

        int dayOfMonthForTransactionDateToMatchOn = 31;
        DateTime transactionDateToMatchOn = getDateTimeForDay(dayOfMonthForTransactionDateToMatchOn);

        CreditCardActivity creditCardActivityToMatch = new CreditCardActivityBuilder()
                .setAmount(amountToMatchOnForCreditCardActivityOne)
                .setDescription(descriptionToMatchOn)
                .setTransactionDate(transactionDateToMatchOn)
                .setType(ActivityType.SALE)
                .build();

        CreditCardActivity creditCardActivityTwo = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityThree = new CreditCardActivityBuilder().build();

        Expense expenseToMatch = new ExpenseBuilder()
                .setMerchant(merchantToMatch)
                .setTimestamp(transactionDateToMatchOn)
                .setAmount(amountToMatchOn)
                .build();

        List<CreditCardActivity> creditCardActivities = Arrays.asList(creditCardActivityTwo, creditCardActivityToMatch, creditCardActivityThree);
        TransactionMatcher transactionMatcher = new TransactionMatcher(creditCardActivities);
        List<Expense> expenses = Arrays.asList(expenseToMatch);

        List<MatchedTransaction> matchedTransactions = transactionMatcher.createMatchedTransactionsWithExpenses(expenses);
        assertThat(matchedTransactions.size(), is(1));

        CreditCardActivity expectedCreditCardActivityMatch = new CreditCardActivityBuilder()
                .setAmount(amountToMatchOnForCreditCardActivityOne)
                .setTransactionDate(getDateTimeForDay(dayOfMonthForTransactionDateToMatchOn))
                .setDescription(creditCardActivityToMatch.getDescription())
                .setPostDate(creditCardActivityToMatch.getPostDate())
                .build();

        MatchedTransaction matchedTransaction = matchedTransactions.get(0);
        CreditCardActivity matchedCreditCardActivity = matchedTransaction.getMatchedCreditCardActivity();
        assertThat(matchedCreditCardActivity.equals(expectedCreditCardActivityMatch), is(true));

        Expense expectedExpenseMatch = new ExpenseBuilder()
                .setMerchant(merchantToMatch)
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
        int dayOfMonthForPostDate = 23;
        int dayOfMonthForExpenseDateToMatchOn = 24;
        int dayOfMonthForTransactionDateToMatchOn = 25;
        int dayOfMonthForAnotherTransactionDate = 20;

        DateTime postDate = getDateTimeForDay(dayOfMonthForPostDate);
        DateTime expenseDateToMatchOn = getDateTimeForDay(dayOfMonthForExpenseDateToMatchOn);
        DateTime transactionDateToMatchOn = getDateTimeForDay(dayOfMonthForTransactionDateToMatchOn);
        DateTime transactionDateOfDifferentCreditCardActivity = getDateTimeForDay(dayOfMonthForAnotherTransactionDate);

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

        Expense expenseOne = new ExpenseBuilder()
                .setMerchant(merchantToMatch)
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
                .setPostDate(getDateTimeForDay(dayOfMonthForPostDate))
                .setTransactionDate(getDateTimeForDay(dayOfMonthForTransactionDateToMatchOn))
                .build();

        MatchedTransaction matchedTransaction = matchedTransactions.get(0);
        CreditCardActivity matchedCreditCardActivity = matchedTransaction.getMatchedCreditCardActivity();
        assertThat(matchedCreditCardActivity.equals(expectedCreditCardActivityMatch), is(true));

        Expense expectedExpenseMatch = new ExpenseBuilder()
                .setMerchant(merchantToMatch)
                .setTimestamp(expenseDateToMatchOn)
                .setAmount(amountToMatchOn)
                .build();

        Expense matchedExpense = matchedTransaction.getMatchedExpense();
        assertThat(matchedExpense.equals(expectedExpenseMatch), is(true));
    }

    @Test
    public void shouldCreateATransactionMatch_whenExpenseDateEqualsOneDayAfterTransactionDate() {
        int dayOfMonthForPostDate = 23;
        int dayOfMonthForExpenseDateToMatchOn = 26;
        int dayOfMonthForTransactionDateToMatchOn = 25;
        int dayOfMonthForAnotherTransactionDate = 20;

        DateTime postDate = getDateTimeForDay(dayOfMonthForPostDate);
        DateTime expenseDateToMatchOn = getDateTimeForDay(dayOfMonthForExpenseDateToMatchOn);
        DateTime transactionDateToMatchOn = getDateTimeForDay(dayOfMonthForTransactionDateToMatchOn);
        DateTime transactionDateOfDifferentCreditCardActivity = getDateTimeForDay(dayOfMonthForAnotherTransactionDate);

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

        Expense expenseOne = new ExpenseBuilder()
                .setMerchant(merchantToMatch)
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
                .setPostDate(getDateTimeForDay(dayOfMonthForPostDate))
                .setTransactionDate(getDateTimeForDay(dayOfMonthForTransactionDateToMatchOn))
                .build();

        MatchedTransaction matchedTransaction = matchedTransactions.get(0);
        CreditCardActivity matchedCreditCardActivity = matchedTransaction.getMatchedCreditCardActivity();
        assertThat(matchedCreditCardActivity.equals(expectedCreditCardActivityMatch), is(true));

        Expense expectedExpenseMatch = new ExpenseBuilder()
                .setMerchant(merchantToMatch)
                .setTimestamp(expenseDateToMatchOn)
                .setAmount(amountToMatchOn)
                .build();

        Expense matchedExpense = matchedTransaction.getMatchedExpense();
        assertThat(matchedExpense.equals(expectedExpenseMatch), is(true));
    }

    private DateTime getDateTimeForDay(int dayOfMonthForPostDate) {
        return new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthForPostDate, 0, 0);
    }
}