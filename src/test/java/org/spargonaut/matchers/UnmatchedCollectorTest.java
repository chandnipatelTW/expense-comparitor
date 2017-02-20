package org.spargonaut.matchers;

import org.junit.Test;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.datamodels.testbuilders.CreditCardActivityBuilder;
import org.spargonaut.datamodels.testbuilders.ExpenseBuilder;
import org.spargonaut.datamodels.testbuilders.MatchedTransactionBuilder;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UnmatchedCollectorTest {

    private final CreditCardActivity matchedCreditCardActivityOne = new CreditCardActivityBuilder().build();
    private final CreditCardActivity matchedCreditCardActivityTwo = new CreditCardActivityBuilder().build();
    private final CreditCardActivity matchedCreditCardActivityThree = new CreditCardActivityBuilder().build();
    private final CreditCardActivity unmatchedCreditCardActivityFour = new CreditCardActivityBuilder().build();
    private final CreditCardActivity unmatchedCreditCardActivityFive = new CreditCardActivityBuilder().build();
    private final CreditCardActivity unmatchedCreditCardActivitySix = new CreditCardActivityBuilder().build();
    private final Expense matchedExpenseOne = new ExpenseBuilder().build();
    private final Expense matchedExpenseTwo = new ExpenseBuilder().build();
    private final Expense matchedExpenseThree = new ExpenseBuilder().build();
    private final Expense unmatchedExpenseFour = new ExpenseBuilder().build();
    private final Expense unmatchedExpenseFive = new ExpenseBuilder().build();
    private final Expense unmatchedExpenseSix = new ExpenseBuilder().build();

    private final List<CreditCardActivity> creditCardActivityList = createCreditCardActivitiesList();
    private final Map<String, List<MatchedTransaction>> matchedTransactionMap = createMatchedTransactionMap();
    private final List<Expense> expenseList = createExpensesList();

    @Test
    public void shouldCollectUnmatchedCreditCardactivities() {

        UnmatchedCollector<CreditCardActivity> unmatchedCollector = new UnmatchedCollector<>();
        Set<CreditCardActivity> actualUnmatchedCreditCardActivities = unmatchedCollector.collect(creditCardActivityList, matchedTransactionMap.values());

        assertThat(actualUnmatchedCreditCardActivities.size(), is(3));
        assertThat(actualUnmatchedCreditCardActivities.contains(unmatchedCreditCardActivityFour), is(true));
        assertThat(actualUnmatchedCreditCardActivities.contains(unmatchedCreditCardActivityFive), is(true));
        assertThat(actualUnmatchedCreditCardActivities.contains(unmatchedCreditCardActivitySix), is(true));
    }

    @Test
    public void shouldCollectUnmatchedExpenses() {

        UnmatchedCollector<Expense> unmatchedCollector = new UnmatchedCollector<>();
        Set<Expense> actualUnmatchedExpenses = unmatchedCollector.collect(expenseList, matchedTransactionMap.values());

        assertThat(actualUnmatchedExpenses.size(), is(3));
        assertThat(actualUnmatchedExpenses.contains(unmatchedExpenseFour), is(true));
        assertThat(actualUnmatchedExpenses.contains(unmatchedExpenseFive), is(true));
        assertThat(actualUnmatchedExpenses.contains(unmatchedExpenseSix), is(true));
    }

    private Map<String, List<MatchedTransaction>> createMatchedTransactionMap() {
        MatchedTransaction matchedTransactionOne = new MatchedTransactionBuilder()
                .withCreditCardActivity(matchedCreditCardActivityOne)
                .withExpense(matchedExpenseOne)
                .build();
        MatchedTransaction matchedTransactionTwo = new MatchedTransactionBuilder()
                .withCreditCardActivity(matchedCreditCardActivityTwo)
                .withExpense(matchedExpenseTwo)
                .build();
        MatchedTransaction matchedTransactionThree = new MatchedTransactionBuilder()
                .withCreditCardActivity(matchedCreditCardActivityThree)
                .withExpense(matchedExpenseThree)
                .build();
        List<MatchedTransaction> matchedTransactionList = Arrays.asList(matchedTransactionOne, matchedTransactionTwo, matchedTransactionThree);
        Map<String, List<MatchedTransaction>> matchedTransactionMap = new HashMap<>();
        matchedTransactionMap.put("someMatcher", matchedTransactionList);
        return matchedTransactionMap;
    }

    private List<CreditCardActivity> createCreditCardActivitiesList() {
        return Arrays.asList(matchedCreditCardActivityOne,
                matchedCreditCardActivityTwo,
                matchedCreditCardActivityThree,
                unmatchedCreditCardActivityFour,
                unmatchedCreditCardActivityFive,
                unmatchedCreditCardActivitySix);
    }

    private List<Expense> createExpensesList() {
        return Arrays.asList(matchedExpenseOne,
                matchedExpenseTwo,
                matchedExpenseThree,
                unmatchedExpenseFour,
                unmatchedExpenseFive,
                unmatchedExpenseSix);
    }
}