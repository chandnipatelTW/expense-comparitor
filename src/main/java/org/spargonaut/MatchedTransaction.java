package org.spargonaut;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;

@AllArgsConstructor
@Getter
public class MatchedTransaction {
    private CreditCardActivity matchedCreditCardActivity;
    private Expense matchedExpense;
}
