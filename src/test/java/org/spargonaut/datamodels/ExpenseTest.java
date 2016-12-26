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
        String mcc = "0";
        String category = "a category";
        String tag = "another tag";
        String comment = "code comment";
        String usd = "USD";
        String receiptUrl = "some url";

        Expense expenseOne = new ExpenseBuilder()
                .setTimestamp(new DateTime(year, month, day, 0, 0))
                .setMerchant(merchant)
                .setAmount(amount)
                .setMcc(mcc)
                .setCategory(category)
                .setTag(tag)
                .setComment(comment)
                .setReimbursable(true)
                .setOriginalCurrency(usd)
                .setOriginalAmmount(amount)
                .setReceiptUrl(receiptUrl)
                .build();


        Expense expenseTwo = new ExpenseBuilder()
                .setTimestamp(new DateTime(year, month, day, 0, 0))
                .setMerchant(merchant)
                .setAmount(amount)
                .setMcc(mcc)
                .setCategory(category)
                .setTag(tag)
                .setComment(comment)
                .setReimbursable(true)
                .setOriginalCurrency(usd)
                .setOriginalAmmount(amount)
                .setReceiptUrl(receiptUrl)
                .build();


        assertThat(expenseOne.equals(expenseTwo), is(true));
    }
}
