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

public class FuzzyMerchantExactAmountMatcherTest {
    @Test
    public void shouldCreateATransactionMatche_whenAmountMatchesExactlyAndMerchantFuzzyScoreISGreaterThanTen() {
        int dayOfMonthToMatchOn = 24;
        int yearToMatchOn = 2016;
        int monthOfYearToMatchOn = 12;
        DateTime dateToMatchOn = new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthToMatchOn, 0, 0);

        double amountToMatchOn = 3.38;
        double amountToMatchOnForCreditCardActivityOne = amountToMatchOn * -1;

        String merchantForCreditCardActivity = "Uber";
        String merchantForExpense = "UBER   *US DEC16 IPG6V";

        CreditCardActivity creditCardActivity = new CreditCardActivityBuilder()
                .setAmount(amountToMatchOnForCreditCardActivityOne)
                .setDescription(merchantForCreditCardActivity)
                .setTransactionDate(dateToMatchOn)
                .setType(ActivityType.SALE)
                .build();

        Expense expense = new ExpenseBuilder()
                .setMerchant(merchantForExpense)
                .setTimestamp(dateToMatchOn)
                .setAmount(amountToMatchOn)
                .build();

        TransactionMatcher matcher = new FuzzyMerchantExactAmountMatcher();
        assertThat(matcher.isMatch(expense, creditCardActivity), is(true));
    }

    @Test
    public void shouldIndicateItsTypeOfMatching() {
        FuzzyMerchantExactAmountMatcher fuzzyMerchantExactAmountMatcher = new FuzzyMerchantExactAmountMatcher();
        assertThat(fuzzyMerchantExactAmountMatcher.getType(), is("Exact Amount, Close Merchant"));
    }
}