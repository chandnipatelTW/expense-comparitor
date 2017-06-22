package org.spargonaut.io.printer;

import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;

import java.util.Set;

public class MatchedTransactionPrinter {
    public static void printMatchedTransactions (Set<MatchedTransaction> matchedTransactions) {
        System.out.format(" transactions (%d) ------------------------------------------------------------------\n", matchedTransactions.size());
        matchedTransactions.forEach( matchedTransaction -> {
            CreditCardActivity creditCardActivity = matchedTransaction.getMatchedCreditCardActivity();
            Expense expense = matchedTransaction.getMatchedExpense();
            System.out.format("               %-50s %-15s %-15s %10s %50s\nmatched with:  %-50s %-31s %10s %50s\n\n",
                    creditCardActivity.getDescription(),
                    creditCardActivity.getTransactionDate().toLocalDate(),
                    creditCardActivity.getPostDate().toLocalDate(),
                    creditCardActivity.getAmount(),
                    creditCardActivity.getFileLocation(),
                    expense.getMerchant(),
                    expense.getTimestamp().toLocalDate(),
                    expense.getAmount(),
                    expense.getFileLocation());
        });
    }
}
