package org.spargonaut.datamodels;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MatchedTransaction {
    private CreditCardActivity matchedCreditCardActivity;
    private Expense matchedExpense;

    public boolean contains(Transaction transactionToCompare) {
        if (transactionToCompare.getClass().equals(CreditCardActivity.class)) {
            return matchedCreditCardActivity.equals(transactionToCompare);
        } else {
            return matchedExpense.equals(transactionToCompare);
        }
    }
}
