package org.spargonaut.io.printer;

import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SummaryPrinter {

    public static void printSummary(Map<String, List<MatchedTransaction>> matchedTransactionMap,
                                    List<CreditCardActivity> creditCardActivities,
                                    List<CreditCardActivity> ignoredCreditCardActivities,
                                    List<Expense> expenses,
                                    List<CreditCardActivity> unmatchedCreditCardActivity,
                                    List<Expense> unmatchedExpenses) {

        System.out.print("\n\n\nParsed ");
        ChargePrinter.printChargesAsHumanReadable(creditCardActivities);

        System.out.print("\n\n\nParsed ");
        ExpensePrinter.printExpensesAsHumanReadable(expenses);

        Set<String> matcherTypes = matchedTransactionMap.keySet();
        StringBuilder summaryBuilder = new StringBuilder();
        int totalMatchesCount = 0;
        for (String matcherType : matcherTypes) {
            System.out.print("\n\n\n" + matcherType + " matched");
            List<MatchedTransaction> matchedTransactions = matchedTransactionMap.get(matcherType);
            MatchedTransactionPrinter.printMatchedTransactions(matchedTransactions);

            int matchCount = matchedTransactions.size();
            totalMatchesCount += matchCount;

            summaryBuilder.append(matcherType);
            summaryBuilder.append("es found:                ");
            summaryBuilder.append(matchCount);
            summaryBuilder.append("\n");
        }
        summaryBuilder.append("Total Matches:                              ");
        summaryBuilder.append(totalMatchesCount);
        summaryBuilder.append("\n\n");

        System.out.print("\n\n\nUnmatched ");
        ChargePrinter.printChargesAsHumanReadable(unmatchedCreditCardActivity);

        System.out.print("\n\n\nUnmatched ");
        ExpensePrinter.printExpensesAsHumanReadable(unmatchedExpenses);

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
