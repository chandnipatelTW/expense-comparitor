package org.spargonaut.io;

import org.junit.Rule;
import org.junit.Test;
import org.spargonaut.datamodels.Expense;
import org.springframework.boot.test.OutputCapture;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ExpensePrinterTest {

    @Rule
    public OutputCapture outputCapture = new OutputCapture();

    @Test
    public void shouldPrintTheFormattedExpenses() {
        Expense expenseOne = new Expense(
                "\"2016-12-12 00:00:00\"",
                "Uber",
                "18.68",
                "0",
                "\"Local Transportation\"",
                "\"Neiman Marcus Group Inc:NMG P4G Design/Deliver Phase:NMG P4G Design/Deliver Phase:Delivery Assurance\"",
                "\"Uber DFW to office\"",
                "yes",
                "USD",
                "18.68",
                "someURL"
                );
        Expense expenseTwo = new Expense(
                "\"2016-12-12 00:00:00\"",
                "\"American Airlines\"",
                "747.20",
                "0",
                "\"Airfare & Upgrades\"",
                "\"Neiman Marcus Group Inc:NMG P4G Design/Deliver Phase:NMG P4G Design/Deliver Phase:Travel\"",
                "Flight",
                "yes",
                "USD",
                "747.20",
                "anotherURL"
        );

        List<Expense> expenses = Arrays.asList(expenseOne, expenseTwo);
        ExpensePrinter expensePrinter = new ExpensePrinter();
        expensePrinter.printExpensesAsHumanReadable(expenses);

        String expectedOutput = "\"2016-12-12 00:00:00\"          Uber                           18.68     \n" +
                                "\"2016-12-12 00:00:00\"          \"American Airlines\"            747.20    \n";

        assertThat(outputCapture.toString(), is(expectedOutput));
    }
}