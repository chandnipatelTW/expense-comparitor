package org.spargonaut.datamodels;

import org.joda.time.DateTime;
import org.junit.Test;
import org.spargonaut.datamodels.testbuilders.ExpenseBuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ExpenseTest {

    @Test
    public void shouldIndicateTwoExpenseObjectsAreEqualWhenTheyHaveTheSameValues() {

        int year = 2016;
        int month = 12;
        int day = 25;
        String merchant = "Some Merchant";
        double amount = 4.56;

        Expense expenseOne = new ExpenseBuilder()
                .setTimestamp(new DateTime(year, month, day, 0, 0))
                .setMerchant(merchant)
                .setAmount(amount)
                .build();


        Expense expenseTwo = new ExpenseBuilder()
                .setTimestamp(new DateTime(year, month, day, 0, 0))
                .setMerchant(merchant)
                .setAmount(amount)
                .build();


        assertThat(expenseOne.equals(expenseTwo), is(true));
    }
}
