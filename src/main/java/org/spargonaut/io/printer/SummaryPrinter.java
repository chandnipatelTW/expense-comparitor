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

        System.out.println("");

        System.out.format("\nTotal Matches plus unmatched CCAs:          %d", (totalMatchesCount + unmatchedCreditCardActivity.size()));
        System.out.format("\nCredit Card Activities parsed:      %10s", creditCardActivities.size());
        System.out.format("\nTotal Matches plus unmatched Expenses:      %d", (totalMatchesCount + unmatchedExpenses.size()));
        System.out.format("\nExpenses parsed:                    %10s", expenses.size());

    }
}
