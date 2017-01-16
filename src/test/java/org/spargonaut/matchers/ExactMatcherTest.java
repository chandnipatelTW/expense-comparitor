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

public class ExactMatcherTest {

    @Test
    public void shouldCreateAnExactTransactionMatch_whenACreditCardActivityHasTheSameAmountAndTransactionDateAsAnExpenseEntry() {

        int dayOfMonthForTransactionDateToMatchOn = 31;
        int yearToMatchOn = 2016;
        int monthOfYearToMatchOn = 12;
        DateTime transactionDateToMatchOn = new DateTime(yearToMatchOn, monthOfYearToMatchOn, dayOfMonthForTransactionDateToMatchOn, 0, 0);

        double amountToMatchOn = 3.38;
        double amountToMatchOnForCreditCardActivityOne = amountToMatchOn * -1;
        String descriptionToMatchOn = "this is a description for a matched amount";
        String merchantToMatch = "some merchant";

        CreditCardActivity creditCardActivityToMatch = new CreditCardActivityBuilder()
                .setAmount(amountToMatchOnForCreditCardActivityOne)
                .setDescription(descriptionToMatchOn)
                .setTransactionDate(transactionDateToMatchOn)
                .setType(ActivityType.SALE)
                .build();

        Expense expenseToMatch = new ExpenseBuilder()
                .setMerchant(merchantToMatch)
                .setTimestamp(transactionDateToMatchOn)
                .setAmount(amountToMatchOn)
                .build();

        assertThat(ExactMatcher.isMatch(expenseToMatch, creditCardActivityToMatch), is(true));
    }
}