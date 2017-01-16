package org.spargonaut.io.printer;

import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;

import java.util.List;

public class SummaryPrinter {

    public static void printSummary(List<MatchedTransaction> matchedTransactions,
                                    List<MatchedTransaction> closelyMatchedTransactions,
                                    List<CreditCardActivity> creditCardActivities,
                                    List<Expense> expenses,
                                    List<CreditCardActivity> unmatchedCreditCardActivity,
                                    List<Expense> unmatchedExpenses) {

        System.out.print("\n\n\nParsed ");
        ChargePrinter.printChargesAsHumanReadable(creditCardActivities);

        System.out.print("\n\n\nParsed ");
        ExpensePrinter.printExpensesAsHumanReadable(expenses);

        System.out.print("\n\n\nExaclty matched ");
        MatchedTransactionPrinter.printMatchedTransactions(matchedTransactions);

        System.out.print("\n\n\nClosely matched ");
        MatchedTransactionPrinter.printMatchedTransactions(closelyMatchedTransactions);

        System.out.print("\n\n\nUnmatched ");
        ChargePrinter.printChargesAsHumanReadable(unmatchedCreditCardActivity);

        System.out.print("\n\n\nUnmatched ");
        ExpensePrinter.printExpensesAsHumanReadable(unmatchedExpenses);

        System.out.print("\n\nSUMMARY\n----------------------------------------------");
        System.out.format("\nExact Matches found:                %10s", matchedTransactions.size());
        System.out.format("\nClose Matches found:                %10s", closelyMatchedTransactions.size());
        System.out.format("\nTotal Matches:                              %d", (matchedTransactions.size() + closelyMatchedTransactions.size()));

        System.out.println("");

        System.out.format("\nUnmatched Credit Card Activities:   %10s", unmatchedCreditCardActivity.size());
        System.out.format("\nUnmatched Expenses:                 %10s", unmatchedExpenses.size());

        System.out.println("");

        System.out.format("\nTotal Matches plus unmatched CCAs:          %d", (matchedTransactions.size() + closelyMatchedTransactions.size() + unmatchedCreditCardActivity.size()));
        System.out.format("\nCredit Card Activities parsed:      %10s", creditCardActivities.size());
        System.out.format("\nTotal Matches plus unmatched Expenses:      %d", (matchedTransactions.size() + closelyMatchedTransactions.size() + unmatchedExpenses.size()));
        System.out.format("\nExpenses parsed:                    %10s", expenses.size());

    }
}
