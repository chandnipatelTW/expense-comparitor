package org.spargonaut.matchers;

import org.junit.Test;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.datamodels.testbuilders.CreditCardActivityBuilder;
import org.spargonaut.datamodels.testbuilders.ExpenseBuilder;
import org.spargonaut.datamodels.testbuilders.MatchedTransactionBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UnmatchedCollectorTest {
    @Test
    public void shouldCollectUnmatchedCreditCardactivities() {
        CreditCardActivity creditCardActivityOne = new CreditCardActivityBuilder().build();
        MatchedTransaction matchedTransactionOne = new MatchedTransactionBuilder()
                .fromCreditCardActivity(creditCardActivityOne)
                .build();

        CreditCardActivity creditCardActivityTwo = new CreditCardActivityBuilder().build();
        MatchedTransaction matchedTransactionTwo = new MatchedTransactionBuilder()
                .fromCreditCardActivity(creditCardActivityTwo)
                .build();

        CreditCardActivity creditCardActivityThree = new CreditCardActivityBuilder().build();
        MatchedTransaction matchedTransactionThree = new MatchedTransactionBuilder()
                .fromCreditCardActivity(creditCardActivityThree)
                .build();

        CreditCardActivity creditCardActivityFour = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityFive = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivitySix = new CreditCardActivityBuilder().build();

        List<CreditCardActivity> creditCardActivityList = Arrays.asList(creditCardActivityOne,
                creditCardActivityTwo,
                creditCardActivityThree,
                creditCardActivityFour,
                creditCardActivityFive,
                creditCardActivitySix);
        List<MatchedTransaction> matchedTransactionList = Arrays.asList(matchedTransactionOne, matchedTransactionTwo, matchedTransactionThree);

        Map<String, List<MatchedTransaction>> matchedTransactionMap = new HashMap<>();
        matchedTransactionMap.put("someMatcher", matchedTransactionList);

        UnmatchedCollector<CreditCardActivity> unmatchedCollector = new UnmatchedCollector<>();
        List<CreditCardActivity> actualUnmatchedCreditCardActivities = unmatchedCollector.collect(creditCardActivityList, matchedTransactionMap.values());

        assertThat(actualUnmatchedCreditCardActivities.size(), is(3));
        assertThat(actualUnmatchedCreditCardActivities.contains(creditCardActivityFour), is(true));
        assertThat(actualUnmatchedCreditCardActivities.contains(creditCardActivityFive), is(true));
        assertThat(actualUnmatchedCreditCardActivities.contains(creditCardActivitySix), is(true));
    }

    @Test
    public void shouldCollectUnmatchedExpenses() {
        Expense expenseOne = new ExpenseBuilder().build();
        Expense expenseTwo = new ExpenseBuilder().build();
        Expense expenseThree = new ExpenseBuilder().build();
        Expense expenseFour = new ExpenseBuilder().build();
        Expense expenseFive = new ExpenseBuilder().build();
        Expense expenseSix = new ExpenseBuilder().build();

        List<Expense> expenseList = Arrays.asList(expenseOne, expenseTwo, expenseThree, expenseFour, expenseFive, expenseSix);

        MatchedTransaction matchedTransactionOne = new MatchedTransactionBuilder().fromExpense(expenseOne).build();
        MatchedTransaction matchedTransactionTwo = new MatchedTransactionBuilder().fromExpense(expenseTwo).build();
        MatchedTransaction matchedTransactionThree = new MatchedTransactionBuilder().fromExpense(expenseThree).build();
        List<MatchedTransaction> matchedTransactionList = Arrays.asList(matchedTransactionOne, matchedTransactionTwo, matchedTransactionThree);
        Map<String, List<MatchedTransaction>> matchedTransactionMap = new HashMap<>();
        matchedTransactionMap.put("some matcher", matchedTransactionList);

        UnmatchedCollector<Expense> unmatchedCollector = new UnmatchedCollector<>();
        List<Expense> actualUnmatchedExpenses = unmatchedCollector.collect(expenseList, matchedTransactionMap.values());

        assertThat(actualUnmatchedExpenses.size(), is(3));
        assertThat(actualUnmatchedExpenses.contains(expenseFour), is(true));
        assertThat(actualUnmatchedExpenses.contains(expenseFive), is(true));
        assertThat(actualUnmatchedExpenses.contains(expenseSix), is(true));
    }
}