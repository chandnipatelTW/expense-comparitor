package org.spargonaut.datamodels;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MatchedTransaction {
    private CreditCardActivity matchedCreditCardActivity;
    private Expense matchedExpense;
}
