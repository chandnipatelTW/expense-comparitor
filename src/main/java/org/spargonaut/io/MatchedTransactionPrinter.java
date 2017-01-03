package org.spargonaut.io;

import org.spargonaut.MatchedTransaction;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;

import java.util.List;

public class MatchedTransactionPrinter {
    public void printMatchedTransactions(List<MatchedTransaction> matchedTransactions) {
        MatchedTransaction matchedTransaction = matchedTransactions.get(0);
        CreditCardActivity creditCardActivity = matchedTransaction.getMatchedCreditCardActivity();
        Expense expense = matchedTransaction.getMatchedExpense();

        System.out.format("%-50s %-15s %-10s\n matched with: %-50s %-15s %-10s\n",
                creditCardActivity.getDescription(),
                creditCardActivity.getTransactionDate().toLocalDate(),
                creditCardActivity.getAmount(),
                expense.getMerchant(),
                expense.getTimestamp().toLocalDate(),
                expense.getAmount());

    }
}
