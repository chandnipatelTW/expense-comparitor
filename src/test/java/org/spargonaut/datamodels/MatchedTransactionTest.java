package org.spargonaut.datamodels;

import org.junit.Test;
import org.spargonaut.datamodels.testbuilders.CreditCardActivityBuilder;
import org.spargonaut.datamodels.testbuilders.ExpenseBuilder;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MatchedTransactionTest {

    @Test
    public void shouldIndicateWhenAMatchedTransactionContainsAnExpense() {

        CreditCardActivity creditCardActivity = new CreditCardActivityBuilder().build();
        Expense expenseToMatch = new ExpenseBuilder().build();
        MatchedTransaction matchedTransaction = new MatchedTransaction(creditCardActivity, expenseToMatch);

        Expense expenseToCompare = new ExpenseBuilder()
                .setBigDecimalAmount(expenseToMatch.getAmount())
                .setTimestamp(expenseToMatch.getTimestamp())
                .setMerchant(expenseToMatch.getMerchant())
                .build();

        boolean containsExpense = matchedTransaction.contains(expenseToCompare);
        assertThat(containsExpense, is(true));
    }

    @Test
    public void shouldIndicateWhenAMatchedTransactionContainsAnCreditCardActivity() {
        CreditCardActivity creditCardActivity = new CreditCardActivityBuilder().build();
        Expense expenseToMatch = new ExpenseBuilder().build();
        MatchedTransaction matchedTransaction = new MatchedTransaction(creditCardActivity, expenseToMatch);

        CreditCardActivity creditCardActivityToCompare = new CreditCardActivityBuilder()
                .setTransactionDate(creditCardActivity.getTransactionDate())
                .setDescription(creditCardActivity.getDescription())
                .setBigDecimalAmount(creditCardActivity.getAmount())
                .build();

        boolean containsCreditCardActivity = matchedTransaction.contains(creditCardActivityToCompare);
        assertThat(containsCreditCardActivity, is(true));
    }
}