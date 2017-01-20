package org.spargonaut.datamodels;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MatchedTransaction {
    private CreditCardActivity matchedCreditCardActivity;
    private Expense matchedExpense;

    public boolean containsExpense(Expense expenseToCompare) {
        return matchedExpense.equals(expenseToCompare);
    }

    public boolean containsCreditCardActivity(CreditCardActivity creditCardActivityToCompare) {
        return matchedCreditCardActivity.equals(creditCardActivityToCompare);
    }
}
