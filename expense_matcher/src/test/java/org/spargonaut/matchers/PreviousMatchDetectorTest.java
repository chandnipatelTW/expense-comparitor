package org.spargonaut.matchers;

import org.junit.Test;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.datamodels.testbuilders.CreditCardActivityBuilder;
import org.spargonaut.datamodels.testbuilders.ExpenseBuilder;
import org.spargonaut.datamodels.testbuilders.MatchedTransactionBuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PreviousMatchDetectorTest {

    private final CreditCardActivity matchedCreditCardActivityOne = new CreditCardActivityBuilder().build();
    private final CreditCardActivity matchedCreditCardActivityTwo = new CreditCardActivityBuilder().build();
    private final CreditCardActivity matchedCreditCardActivityThree = new CreditCardActivityBuilder().build();
    private final CreditCardActivity unmatchedCreditCardActivity = new CreditCardActivityBuilder().build();
    private final Expense expenseOne = new ExpenseBuilder().build();
    private final Expense expenseTwo = new ExpenseBuilder().build();
    private final Expense expenseThree = new ExpenseBuilder().build();
    private final Expense expenseFour = new ExpenseBuilder().build();
    private final Set<MatchedTransaction> matchedTransactionList = createMatchedTransactions();
    private final PreviousMatchDetector previousMatchDetectorUnderTest = new PreviousMatchDetector();

    @Test
    public void shouldIndicateACreditCardActivityIsContainedInACollectionOfMatchedTransactions() {
        boolean isMatched = previousMatchDetectorUnderTest.isPreviouslyMatched(matchedCreditCardActivityOne, matchedTransactionList);
        assertThat(isMatched, is(true));
    }

    @Test
    public void shouldIndicateACreditCardActivityIsMissingFromTheCollectionOfMatchedTransactions() {
        boolean isMatched = previousMatchDetectorUnderTest.isPreviouslyMatched(unmatchedCreditCardActivity, matchedTransactionList);
        assertThat(isMatched, is(false));
    }

    @Test
    public void shouldIndicateAnExpenseIsContainedInACollectionOfMatchedTransactions() {
        boolean isMatched = previousMatchDetectorUnderTest.isPreviouslyMatched(expenseOne, matchedTransactionList);
        assertThat(isMatched, is(true));
    }

    @Test
    public void shouldIndicateAnExpenseIsMissingFromTheCollectionOfMatchedTransactions() {
        boolean isMatched = previousMatchDetectorUnderTest.isPreviouslyMatched(expenseFour, matchedTransactionList);
        assertThat(isMatched, is(false));
    }

    private Set<MatchedTransaction> createMatchedTransactions() {
        MatchedTransaction matchedTransactionOne = new MatchedTransactionBuilder()
                .withCreditCardActivity(matchedCreditCardActivityOne)
                .withExpense(expenseOne)
                .build();
        MatchedTransaction matchedTransactionTwo = new MatchedTransactionBuilder()
                .withCreditCardActivity(matchedCreditCardActivityTwo)
                .withExpense(expenseTwo)
                .build();
        MatchedTransaction matchedTransactionThree = new MatchedTransactionBuilder()
                .withCreditCardActivity(matchedCreditCardActivityThree)
                .withExpense(expenseThree)
                .build();
        return new HashSet<>(Arrays.asList(matchedTransactionOne, matchedTransactionTwo, matchedTransactionThree));
    }
}
