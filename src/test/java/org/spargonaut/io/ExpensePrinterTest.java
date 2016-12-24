package org.spargonaut.io;

import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.spargonaut.datamodels.Expense;
import org.springframework.boot.test.OutputCapture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ExpensePrinterTest {

    @Rule
    public OutputCapture outputCapture = new OutputCapture();

    @Test
    public void shouldPrintTheFormattedExpenses() {

        DateTime dateTimeForExpenseOne = new DateTime(2016, 12, 12, 0, 0);
        BigDecimal amountForExpenseOne = new BigDecimal(18.68);
        amountForExpenseOne = amountForExpenseOne.setScale(2, BigDecimal.ROUND_HALF_EVEN);
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
                "18.68",
                "someURL"
                );
        DateTime dateTimeForExpenseTwo = new DateTime(2016, 12, 12, 0, 0);
        BigDecimal amountForExpenseTwo = new BigDecimal(747.20);
        amountForExpenseTwo = amountForExpenseTwo.setScale(2, BigDecimal.ROUND_HALF_EVEN);
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
                "747.20",
                "anotherURL"
        );

        List<Expense> expenses = Arrays.asList(expenseOne, expenseTwo);
        ExpensePrinter.printExpensesAsHumanReadable(expenses);

        String expectedOutput = "2016-12-12T00:00:00.000-06:00  Uber                           18.68     \n" +
                                "2016-12-12T00:00:00.000-06:00  American Airlines              747.20    \n";

        assertThat(outputCapture.toString(), is(expectedOutput));
    }
}