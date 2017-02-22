package org.spargonaut.io.printer;

import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SummaryPrinter {

    public static void printSummary(Map<String, Set<MatchedTransaction>> matchedTransactionMap,
                                    Set<CreditCardActivity> creditCardActivities,
                                    Set<CreditCardActivity> ignoredCreditCardActivities,
                                    Set<Expense> expenses,
                                    Set<CreditCardActivity> unmatchedCreditCardActivity,
                                    Set<Expense> unmatchedExpenses) {

        System.out.print("\n\n\nParsed ");
        ChargePrinter.printChargesAsHumanReadable(creditCardActivities);

        System.out.print("\n\n\nParsed ");
        ExpensePrinter.printExpensesAsHumanReadable(expenses);

        Set<String> matcherTypes = matchedTransactionMap.keySet();
        int totalMatchesCount = 0;
        for (String matcherType : matcherTypes) {
            System.out.print("\n\n\n" + matcherType + " matched");
            Set<MatchedTransaction> matchedTransactions = matchedTransactionMap.get(matcherType);
            MatchedTransactionPrinter.printMatchedTransactions(new HashSet<>(matchedTransactions));

            int matchCount = matchedTransactions.size();
            totalMatchesCount += matchCount;
        }

        System.out.print("\n\n\nUnmatched ");
        ChargePrinter.printChargesAsHumanReadable(new HashSet<>(unmatchedCreditCardActivity));

        System.out.print("\n\n\nUnmatched ");
        ExpensePrinter.printExpensesAsHumanReadable(new HashSet<>(unmatchedExpenses));

        System.out.print("\n\nSUMMARY\n----------------------------------------------");
        System.out.format("\nTotal Matches:                              %d", totalMatchesCount);
        System.out.format("\nUnmatched Credit Card Activities:   %10s", unmatchedCreditCardActivity.size());
        System.out.format("\nUnmatched Expenses:                 %10s", unmatchedExpenses.size());
        System.out.format("\nIgnored Credit Card Activities:     %10s", ignoredCreditCardActivities.size());

        System.out.println("");

        System.out.println("\n--(These numbers should match)--");
        System.out.format("Total Matches plus unmatched CCAs:          %d\n", (totalMatchesCount + unmatchedCreditCardActivity.size()));
        System.out.format("Credit Card Activities parsed:       %10s\n", creditCardActivities.size());
        System.out.println("\n--(These numbers should match)--");
        System.out.format("Total Matches plus unmatched Expenses:      %d\n", (totalMatchesCount + unmatchedExpenses.size()));
        System.out.format("Expenses parsed:                     %10s\n", expenses.size());

    }
}
