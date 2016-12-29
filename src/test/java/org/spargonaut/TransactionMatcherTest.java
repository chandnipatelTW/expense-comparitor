package org.spargonaut;

import org.junit.Test;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.testbuilders.CreditCardActivityBuilder;
import org.spargonaut.datamodels.testbuilders.ExpenseBuilder;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TransactionMatcherTest {

    @Test
    public void shouldCreateATransactionMatch_whenACreditCardActivityHasTheSameAmountAsAnExpenseEntry() {
        double amountToMatchOn = 3.38;
        CreditCardActivity creditCardActivityOne = new CreditCardActivityBuilder()
                .setAmount(amountToMatchOn)
                .build();

        double amountForCreditCardActivityTwo = 5.56;
        CreditCardActivity creditCardActivityTwo = new CreditCardActivityBuilder()
                .setAmount(amountForCreditCardActivityTwo)
                .build();

        Expense expense = new ExpenseBuilder()
                .setAmount(amountToMatchOn)
                .build();

        TransactionMatcher transactionMatcher = new TransactionMatcher(Arrays.asList(creditCardActivityTwo, creditCardActivityOne));
        List<MatchedTransaction> matchedTransactions = transactionMatcher.createMatchedTransactionsWithExpense(expense);

        MatchedTransaction matchedTransaction = matchedTransactions.get(0);

        CreditCardActivity matchedCreditCardActivity = matchedTransaction.getMatchedCreditCardActivity();
        assertThat(matchedCreditCardActivity.equals(creditCardActivityOne), is(true));

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
        List<MatchedTransaction> matchedTransactions = transactionMatcher.createMatchedTransactionsWithExpense(expense);

        assertThat(matchedTransactions.size(), is(0));
    }

}