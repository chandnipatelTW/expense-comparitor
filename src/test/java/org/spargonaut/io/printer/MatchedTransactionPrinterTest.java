package org.spargonaut.io.printer;

import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;
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
    public void shouldPrintAFormattedSetOfMatchedTransaction () {
        double matchedAmountOne = 3.49;

        int transactionYear = 2017;
        int transactionMonthOne = 1;
        int transactionDayOne = 1;
        int postDayOne = 2;
        DateTime activityTransactionDateOne = new DateTime (transactionYear, transactionMonthOne, transactionDayOne, 0, 0);
        DateTime activityPostDateOne = new DateTime (transactionYear, transactionMonthOne, postDayOne, 0, 0);

        String transactionDescriptionOne = "A credit card transaction description";
        double matchedAmountOneForCreditCardActivityOne = matchedAmountOne * -1;
        CreditCardActivity creditCardActivityOne = new CreditCardActivityBuilder ()
                .setAmount (matchedAmountOneForCreditCardActivityOne)
                .setTransactionDate (activityTransactionDateOne)
                .setPostDate(activityPostDateOne)
                .setDescription (transactionDescriptionOne)
                .build ();


        int expenseMonthOne = 2;
        int expenseDayOne = 3;
        DateTime expenseDateOne = new DateTime (transactionYear, expenseMonthOne, expenseDayOne, 0, 0);

        String merchantOne = "An expense merchant";
        Expense expenseOne = new ExpenseBuilder ()
                .setAmount (matchedAmountOne)
                .setTimestamp (expenseDateOne)
                .setMerchant (merchantOne)
                .build ();

        MatchedTransaction matchedTransactionOne = new MatchedTransaction (creditCardActivityOne, expenseOne);


        double matchedAmountTwo = 5.68;
        int transactionMonthTwo = 4;
        int transactionDayTwo = 5;
        int postDayTwo = 6;
        DateTime activityTransactionDateTwo = new DateTime (transactionYear, transactionMonthTwo, transactionDayTwo, 0, 0);
        DateTime activityPostDateTwo = new DateTime (transactionYear, transactionMonthTwo, postDayTwo, 0, 0);

        String transactionDescriptionTwo = "Another credit card transaction description";
        double matchedAmountTwoForCreditCardActivityTwo = matchedAmountTwo * -1;
        CreditCardActivity creditCardActivityTwo = new CreditCardActivityBuilder ()
                .setAmount (matchedAmountTwoForCreditCardActivityTwo)
                .setTransactionDate (activityTransactionDateTwo)
                .setPostDate(activityPostDateTwo)
                .setDescription (transactionDescriptionTwo)
                .build ();


        int expenseMonthTwo = 6;
        int expenseDayTwo = 7;
        DateTime expenseDateTwo = new DateTime (transactionYear, expenseMonthTwo, expenseDayTwo, 0, 0);

        String merchantTwo = "Another expense merchant";
        Expense expenseTwo = new ExpenseBuilder ()
                .setAmount (matchedAmountTwo)
                .setTimestamp (expenseDateTwo)
                .setMerchant (merchantTwo)
                .build ();

        MatchedTransaction matchedTransactionTwo = new MatchedTransaction (creditCardActivityTwo, expenseTwo);


        List<MatchedTransaction> matchedTransactions = Arrays.asList (matchedTransactionOne, matchedTransactionTwo);

        MatchedTransactionPrinter.printMatchedTransactions (matchedTransactions);

        String expectedOutput = "transactions (2) ------------------------------------------------------------------\n" +
                                "               A credit card transaction description              2017-01-01      2017-01-02           -3.49\n" +
                                "matched with:  An expense merchant                                2017-02-03                            3.49\n\n" +
                                "               Another credit card transaction description        2017-04-05      2017-04-06           -5.68\n" +
                                "matched with:  Another expense merchant                           2017-06-07                            5.68\n\n";

        assertThat (outputCapture.toString(), is(expectedOutput));
    }
}
