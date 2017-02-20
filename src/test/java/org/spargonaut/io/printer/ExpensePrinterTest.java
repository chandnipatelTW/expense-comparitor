package org.spargonaut.io.printer;

import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.spargonaut.datamodels.Expense;
import org.springframework.boot.test.OutputCapture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ExpensePrinterTest {

    @Rule
    public OutputCapture outputCapture = new OutputCapture();

    @Test
    public void shouldPrintTheFormattedExpenses() {
        Set<Expense> expenses = createExpenseSet();
        ExpensePrinter.printExpensesAsHumanReadable(expenses);

        String formattedExpenseOne = "2016-12-12      American Airlines                  747.20\n";
        String formattedExpenseTwo = "2016-12-12      Uber                                18.68\n";

        assertThat(outputCapture.toString().contains(formattedExpenseOne), is(true));
        assertThat(outputCapture.toString().contains(formattedExpenseTwo), is(true));
    }

    @Test
    public void shouldPrintOutTheExpenseHeaderStringWithTheCountOfExpenses() {
        Set<Expense> expenses = createExpenseSet();
        ExpensePrinter.printExpensesAsHumanReadable(expenses);
        String expectedOutput = "expenses (2) -------------------------------------------\n";
        assertThat(outputCapture.toString().contains(expectedOutput), is(true));
    }

    private Set<Expense> createExpenseSet() {
        DateTime dateTimeForExpenseOne = new DateTime(2016, 12, 12, 0, 0);
        double amountOne = 18.68;
        BigDecimal amountForExpenseOne = createBigDecimalFrom(amountOne);
        BigDecimal originalAmountForExpenseOne = createBigDecimalFrom(amountOne);
        Expense expenseOne = new Expense(
                dateTimeForExpenseOne,
                "Uber",
                amountForExpenseOne,
                "0",
                "Local Transportation",
                "Neiman Marcus Group Inc:NMG P4G Design/Deliver Phase:NMG P4G Design/Deliver Phase:Delivery Assurance",
                "Uber DFW to office",
                true,
                "USD",
                originalAmountForExpenseOne,
                "someURL"
        );
        DateTime dateTimeForExpenseTwo = new DateTime(2016, 12, 12, 0, 0);
        double amountTwo = 747.20;
        BigDecimal amountForExpenseTwo = createBigDecimalFrom(amountTwo);
        BigDecimal originalAmountForExpenseTwo = createBigDecimalFrom(amountTwo);
        Expense expenseTwo = new Expense(
                dateTimeForExpenseTwo,
                "American Airlines",
                amountForExpenseTwo,
                "0",
                "Airfare & Upgrades",
                "Neiman Marcus Group Inc:NMG P4G Design/Deliver Phase:NMG P4G Design/Deliver Phase:Travel",
                "Flight",
                true,
                "USD",
                originalAmountForExpenseTwo,
                "anotherURL"
        );

        return new HashSet<>(Arrays.asList(expenseOne, expenseTwo));
    }

    private BigDecimal createBigDecimalFrom(double value) {
        BigDecimal amountForExpenseOne = new BigDecimal(value);
        amountForExpenseOne = amountForExpenseOne.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return amountForExpenseOne;
    }
}