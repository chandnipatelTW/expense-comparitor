package org.spargonaut.io;

import org.spargonaut.MatchedTransaction;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;

import java.util.List;

public class MatchedTransactionPrinter {
    public void printMatchedTransactions (List<MatchedTransaction> matchedTransactions) {
        for (MatchedTransaction matchedTransaction : matchedTransactions) {
            printMatchedTransaction(matchedTransaction.getMatchedCreditCardActivity(),
                    matchedTransaction.getMatchedExpense());
        }
    }

    private void printMatchedTransaction (CreditCardActivity creditCardActivity, Expense expense) {
        System.out.format("%-50s %-15s %-10s\n matched with: %-50s %-15s %-10s\n\n",
                creditCardActivity.getDescription(),
                creditCardActivity.getTransactionDate().toLocalDate(),
                creditCardActivity.getAmount(),
                expense.getMerchant(),
                expense.getTimestamp().toLocalDate(),
                expense.getAmount());

    }
}
