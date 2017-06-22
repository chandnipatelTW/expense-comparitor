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
        printParsedTransactions(creditCardActivities, expenses);
        int totalMatchesCount = printMatchedTransactions(matchedTransactionMap);
        printUnmatchedTransactions(unmatchedCreditCardActivity, unmatchedExpenses);
        printMainSummary(creditCardActivities, ignoredCreditCardActivities, expenses, unmatchedCreditCardActivity, unmatchedExpenses, totalMatchesCount);

    }

    private static void printMainSummary(Set<CreditCardActivity> creditCardActivities, Set<CreditCardActivity> ignoredCreditCardActivities, Set<Expense> expenses, Set<CreditCardActivity> unmatchedCreditCardActivity, Set<Expense> unmatchedExpenses, int totalMatchesCount) {
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

    private static void printUnmatchedTransactions(Set<CreditCardActivity> unmatchedCreditCardActivity, Set<Expense> unmatchedExpenses) {
        String unmatchedHeaderLine = "Unmatched ";
        printCharges(unmatchedHeaderLine, unmatchedCreditCardActivity);
        printExpenses(unmatchedHeaderLine, unmatchedExpenses);
    }

    private static void printParsedTransactions(Set<CreditCardActivity> creditCardActivities, Set<Expense> expenses) {
        String parsedHeaderLine = "Parsed ";
        printCharges(parsedHeaderLine, creditCardActivities);
        printExpenses(parsedHeaderLine, expenses);
    }

    private static void printExpenses(String headerLine, Set<Expense> expenses) {
        System.out.print("\n\n\n");
        System.out.print(headerLine);
        ExpensePrinter.printExpensesAsHumanReadable(expenses);
    }

    private static void printCharges(String headerLine, Set<CreditCardActivity> creditCardActivities) {
        System.out.print("\n\n\n");
        System.out.print(headerLine);
        ChargePrinter.printChargesAsHumanReadable(creditCardActivities);
    }

    private static int printMatchedTransactions(Map<String, Set<MatchedTransaction>> matchedTransactionMap) {
        Set<String> matcherTypes = matchedTransactionMap.keySet();
        int totalMatchesCount = 0;
        for (String matcherType : matcherTypes) {
            totalMatchesCount += matchedTransactionMap.get(matcherType).size();
            System.out.print("\n\n\n" + matcherType + " matched");
            Set<MatchedTransaction> matchedTransactions = matchedTransactionMap.get(matcherType);
            MatchedTransactionPrinter.printMatchedTransactions(new HashSet<>(matchedTransactions));
        }
        return totalMatchesCount;
    }
}
