package org.spargonaut.datamodels;

import org.junit.Before;
import org.junit.Test;
import org.spargonaut.datamodels.testbuilders.CreditCardActivityBuilder;
import org.spargonaut.datamodels.testbuilders.ExpenseBuilder;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MatchedTransactionTest {

    private CreditCardActivity creditCardActivity;
    private Expense expense;
    private MatchedTransaction matchedTransaction;

    @Before
    public void setUp() {
        creditCardActivity = new CreditCardActivityBuilder().build();
        expense = new ExpenseBuilder().build();
        matchedTransaction = new MatchedTransaction(creditCardActivity, expense);
    }

    @Test
    public void shouldIndicateWhenAMatchedTransactionContainsAnExpense() {
        Expense expenseToCompare = new ExpenseBuilder()
                .setBigDecimalAmount(expense.getAmount())
                .setTimestamp(expense.getTimestamp())
                .setMerchant(expense.getMerchant())
                .build();

        boolean containsExpense = matchedTransaction.contains(expenseToCompare);
        assertThat(containsExpense, is(true));
    }

    @Test
    public void shouldIndicateWhenAMatchedTransactionContainsAnCreditCardActivity() {
        CreditCardActivity creditCardActivityToCompare = new CreditCardActivityBuilder()
                .setTransactionDate(creditCardActivity.getTransactionDate())
                .setDescription(creditCardActivity.getDescription())
                .setBigDecimalAmount(creditCardActivity.getAmount())
                .build();

        boolean containsCreditCardActivity = matchedTransaction.contains(creditCardActivityToCompare);
        assertThat(containsCreditCardActivity, is(true));
    }

    @Test
    public void shouldRetrieveTheMatchedCreditCardActivityWhenRequestedByClassType() {
        assertThat(matchedTransaction.getItemOfType(creditCardActivity.getClass()), is(creditCardActivity));
    }

    @Test
    public void shouldRetrieveTheMatchedExpenseWhenRequestedByClassType() {
        assertThat(matchedTransaction.getItemOfType(expense.getClass()), is(expense));
    }
}