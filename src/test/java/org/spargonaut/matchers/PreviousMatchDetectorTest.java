package org.spargonaut.matchers;

import org.junit.Test;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.datamodels.testbuilders.CreditCardActivityBuilder;
import org.spargonaut.datamodels.testbuilders.ExpenseBuilder;
import org.spargonaut.datamodels.testbuilders.MatchedTransactionBuilder;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PreviousMatchDetectorTest {
    @Test
    public void shouldIndicateACreditCardActivityIsContainedInACollectionOfMatchedTransactions() {
        CreditCardActivity creditCardActivityOne = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityTwo = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityThree = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityFour = new CreditCardActivityBuilder().build();

        MatchedTransaction matchedTransactionOne = new MatchedTransactionBuilder().fromCreditCardActivity(creditCardActivityOne).build();
        MatchedTransaction matchedTransactionTwo = new MatchedTransactionBuilder().fromCreditCardActivity(creditCardActivityTwo).build();
        MatchedTransaction matchedTransactionThree = new MatchedTransactionBuilder().fromCreditCardActivity(creditCardActivityThree).build();

        List<MatchedTransaction> matchedTransactionList = Arrays.asList(matchedTransactionOne, matchedTransactionTwo, matchedTransactionThree);

        PreviousMatchDetector previousMatchDetector = new PreviousMatchDetector();
        boolean isMatched = previousMatchDetector.isPreviouslyMatched(creditCardActivityOne, matchedTransactionList);
        assertThat(isMatched, is(true));
    }

    @Test
    public void shouldIndicateACreditCardActivityIsMissingFromTheCollectionOfMatchedTransactions() {
        CreditCardActivity creditCardActivityOne = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityTwo = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityThree = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityFour = new CreditCardActivityBuilder().build();

        MatchedTransaction matchedTransactionOne = new MatchedTransactionBuilder().fromCreditCardActivity(creditCardActivityOne).build();
        MatchedTransaction matchedTransactionTwo = new MatchedTransactionBuilder().fromCreditCardActivity(creditCardActivityTwo).build();
        MatchedTransaction matchedTransactionThree = new MatchedTransactionBuilder().fromCreditCardActivity(creditCardActivityThree).build();

        List<MatchedTransaction> matchedTransactionList = Arrays.asList(matchedTransactionOne, matchedTransactionTwo, matchedTransactionThree);

        PreviousMatchDetector previousMatchDetector = new PreviousMatchDetector();
        boolean isMatched = previousMatchDetector.isPreviouslyMatched(creditCardActivityFour, matchedTransactionList);
        assertThat(isMatched, is(false));
    }

    @Test
    public void shouldIndicateAnExpenseIsContainedInACollectionOfMatchedTransactions() {
        Expense expenseOne = new ExpenseBuilder().build();
        Expense expenseTwo = new ExpenseBuilder().build();
        Expense expenseThree = new ExpenseBuilder().build();
        Expense expenseFour = new ExpenseBuilder().build();

        MatchedTransaction matchedTransactionOne = new MatchedTransactionBuilder().fromExpense(expenseOne).build();
        MatchedTransaction matchedTransactionTwo = new MatchedTransactionBuilder().fromExpense(expenseTwo).build();
        MatchedTransaction matchedTransactionThree = new MatchedTransactionBuilder().fromExpense(expenseThree).build();

        List<MatchedTransaction> matchedTransactionList = Arrays.asList(matchedTransactionOne, matchedTransactionTwo, matchedTransactionThree);

        PreviousMatchDetector previousMatchDetector = new PreviousMatchDetector();
        boolean isMatched = previousMatchDetector.isPreviouslyMatched(expenseOne, matchedTransactionList);
        assertThat(isMatched, is(true));
    }

    @Test
    public void shouldIndicateAnExpenseIsMissingFromTheCollectionOfMatchedTransactions() {
        Expense expenseOne = new ExpenseBuilder().build();
        Expense expenseTwo = new ExpenseBuilder().build();
        Expense expenseThree = new ExpenseBuilder().build();
        Expense expenseFour = new ExpenseBuilder().build();

        MatchedTransaction matchedTransactionOne = new MatchedTransactionBuilder().fromExpense(expenseOne).build();
        MatchedTransaction matchedTransactionTwo = new MatchedTransactionBuilder().fromExpense(expenseTwo).build();
        MatchedTransaction matchedTransactionThree = new MatchedTransactionBuilder().fromExpense(expenseThree).build();

        List<MatchedTransaction> matchedTransactionList = Arrays.asList(matchedTransactionOne, matchedTransactionTwo, matchedTransactionThree);

        PreviousMatchDetector previousMatchDetector = new PreviousMatchDetector();
        boolean isMatched = previousMatchDetector.isPreviouslyMatched(expenseFour, matchedTransactionList);
        assertThat(isMatched, is(false));
    }
}
