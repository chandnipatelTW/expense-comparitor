package org.spargonaut;

import org.junit.Test;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.testbuilders.CreditCardActivityBuilder;
import org.spargonaut.datamodels.testbuilders.ExpenseBuilder;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class TransactionMatcherTest {

    @Test
    public void shouldCreateATransactionMatch_whenACreditCardActivityHasTheSameAmountAsAnExpenseEntry() {
        double amountToMatchOn = 3.38;
        CreditCardActivity creditCardActivity = new CreditCardActivityBuilder()
                .setAmount(amountToMatchOn)
                .build();

        Expense expense = new ExpenseBuilder()
                .setAmount(amountToMatchOn)
                .build();

        TransactionMatcher transactionMatcher = new TransactionMatcher(Arrays.asList(creditCardActivity));
        MatchedTransaction matchedTransaction = transactionMatcher.createMatchedTransactionsWithExpense(expense);

        CreditCardActivity matchedCreditCardActivity = matchedTransaction.getMatchedCreditCardActivity();
        assertThat(matchedCreditCardActivity.equals(creditCardActivity), is(true));

        Expense matchedExpense = matchedTransaction.getMatchedExpense();
        assertThat(matchedExpense.equals(expense), is(true));
    }

    @Test
    public void shouldRejectATransactionMatch_whenACreditCardActivityHasADifferentAmountAsAnExpenseEntry() {
        double amountForCreditCardActivity = 5.56;
        CreditCardActivity creditCardActivity = new CreditCardActivityBuilder()
                .setAmount(amountForCreditCardActivity)
                .build();

        double amountForExpense = 3.38;
        Expense expense = new ExpenseBuilder()
                .setAmount(amountForExpense)
                .build();

        TransactionMatcher transactionMatcher = new TransactionMatcher(Arrays.asList(creditCardActivity));
        MatchedTransaction matchedTransaction = transactionMatcher.createMatchedTransactionsWithExpense(expense);

        assertNull(matchedTransaction);
    }

}