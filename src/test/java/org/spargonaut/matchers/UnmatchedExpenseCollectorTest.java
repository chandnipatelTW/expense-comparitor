package org.spargonaut.matchers;

import org.junit.Test;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.datamodels.testbuilders.ExpenseBuilder;
import org.spargonaut.datamodels.testbuilders.MatchedTransactionBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UnmatchedExpenseCollectorTest {

    @Test
    public void shouldCollectUnmatchedCreditCardactivities() {
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

        UnmatchedExpenseCollector unmatchedExpenseCollector = new UnmatchedExpenseCollector();
        List<Expense> actualUnmatchedExpenses = unmatchedExpenseCollector.collect(expenseList, matchedTransactionMap.values());

        assertThat(actualUnmatchedExpenses.size(), is(3));
        assertThat(actualUnmatchedExpenses.contains(expenseFour), is(true));
        assertThat(actualUnmatchedExpenses.contains(expenseFive), is(true));
        assertThat(actualUnmatchedExpenses.contains(expenseSix), is(true));
    }
}
