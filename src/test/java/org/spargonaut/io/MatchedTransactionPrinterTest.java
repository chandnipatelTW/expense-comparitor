package org.spargonaut.io;

import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.spargonaut.MatchedTransaction;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.testbuilders.CreditCardActivityBuilder;
import org.spargonaut.datamodels.testbuilders.ExpenseBuilder;
import org.springframework.boot.test.OutputCapture;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class MatchedTransactionPrinterTest {

    @Rule
    public OutputCapture outputCapture = new OutputCapture ();

    @Test
    public void shouldPrintAFormattedMatchedTransaction () {
        double matchedAmount = 3.49;

        int transactionYear = 2017;
        int transactionMonth = 1;
        int transactionDay = 1;
        DateTime activityTransactionDate = new DateTime (transactionYear, transactionMonth, transactionDay, 0, 0);

        String transactionDescription = "A credit card transaction description";
        CreditCardActivity creditCardActivity = new CreditCardActivityBuilder ()
                .setAmount (matchedAmount)
                .setTransactionDate (activityTransactionDate)
                .setDescription (transactionDescription)
                .build ();


        int expenseMonth = 2;
        int expenseDay = 3;
        DateTime expenseDate = new DateTime (transactionYear, expenseMonth, expenseDay, 0, 0);

        String merchant = "An expense merchant";
        Expense expense = new ExpenseBuilder ()
                .setAmount (matchedAmount)
                .setTimestamp (expenseDate)
                .setMerchant (merchant)
                .build ();

        MatchedTransaction matchedTransaction = new MatchedTransaction (creditCardActivity, expense);
        List<MatchedTransaction> matchedTransactions = Arrays.asList (matchedTransaction);

        MatchedTransactionPrinter matchedTransactionPrinter = new MatchedTransactionPrinter ();
        matchedTransactionPrinter.printMatchedTransactions (matchedTransactions);

        String expectedOutput = "A credit card transaction description              2017-01-01      3.49      \n" +
                " matched with: An expense merchant                                2017-02-03      3.49      \n";

        assertThat (outputCapture.toString(), is(expectedOutput));
    }
}
