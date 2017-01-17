package org.spargonaut.matchers;

import org.joda.time.DateTime;
import org.junit.Test;
import org.spargonaut.datamodels.ActivityType;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.testbuilders.CreditCardActivityBuilder;
import org.spargonaut.datamodels.testbuilders.ExpenseBuilder;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CloseDateMatcherTest {

    @Test
    public void shouldCreateACloseTransactionMatche_whenCreditCardActivityDateIsOneDayBeforeExpenseDate() {
        int dayOfMonthToMatchOn = 24;
        int yearToMatchOn = 2016;
        int monthOfYearToMatchOn = 12;
        double amountToMatchOn = 3.38;
        String descriptionToMatchOn = "this is a description for a matched amount";
        double amountToMatchOnForCreditCardActivityOne = amountToMatchOn * -1;
        String merchantToMatch = "some merchant";

        DateTime expenseDateToMatchOn = new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthToMatchOn, 0, 0);

        CreditCardActivity creditCardActivity = new CreditCardActivityBuilder()
                .setAmount(amountToMatchOnForCreditCardActivityOne)
                .setDescription(descriptionToMatchOn)
                .setTransactionDate(expenseDateToMatchOn.minusDays(1))
                .setType(ActivityType.SALE)
                .build();

        Expense expense = new ExpenseBuilder()
                .setMerchant(merchantToMatch)
                .setTimestamp(expenseDateToMatchOn)
                .setAmount(amountToMatchOn)
                .build();

        CloseDateMatcher matcher = new CloseDateMatcher();
        assertThat(matcher.isMatch(expense, creditCardActivity), is(true));
    }

    @Test
    public void shouldCreateAListOfCloseTransactionMatches_whenCreditCardActivityDateIsOneDayAfterTheExpenseDate() {
        int dayOfMonthForExpenseToMatchOn = 26;
        int yearToMatchOn = 2016;
        int monthOfYearToMatchOn = 12;
        double amountToMatchOn = 3.38;
        String descriptionToMatchOn = "this is a description for a matched amount";
        double amountToMatchOnForCreditCardActivityOne = amountToMatchOn * -1;
        String merchantToMatch = "some merchant";

        DateTime expenseDateToMatchOn = new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthForExpenseToMatchOn, 0, 0);

        CreditCardActivity creditCardActivity = new CreditCardActivityBuilder()
                .setAmount(amountToMatchOnForCreditCardActivityOne)
                .setDescription(descriptionToMatchOn)
                .setTransactionDate(expenseDateToMatchOn.plusDays(1))
                .setType(ActivityType.SALE)
                .build();

        Expense expense = new ExpenseBuilder()
                .setMerchant(merchantToMatch)
                .setTimestamp(expenseDateToMatchOn)
                .setAmount(amountToMatchOn)
                .build();

        CloseDateMatcher matcher = new CloseDateMatcher();
        assertThat(matcher.isMatch(expense, creditCardActivity), is(true));
    }

    @Test
    public void shouldIndicateItsTypeOfMatching() {
        CloseDateMatcher closeDateMatcher = new CloseDateMatcher();
        assertThat(closeDateMatcher.getType(), is("Close Date match"));
    }

}