package org.spargonaut.io.printer;

import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;

import java.util.List;

public class MatchedTransactionPrinter {
    public static void printMatchedTransactions (List<MatchedTransaction> matchedTransactions) {
        for (MatchedTransaction matchedTransaction : matchedTransactions) {
            printMatchedTransaction(matchedTransaction.getMatchedCreditCardActivity(),
                    matchedTransaction.getMatchedExpense());
        }
    }

    private static void printMatchedTransaction (CreditCardActivity creditCardActivity, Expense expense) {
        System.out.format("               %-50s %-15s %10s\nmatched with:  %-50s %-15s %10s\n\n",
                creditCardActivity.getDescription(),
                creditCardActivity.getTransactionDate().toLocalDate(),
                creditCardActivity.getAmount(),
                expense.getMerchant(),
                expense.getTimestamp().toLocalDate(),
                expense.getAmount());

    }
}
