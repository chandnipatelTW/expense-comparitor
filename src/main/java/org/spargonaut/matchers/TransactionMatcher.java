package org.spargonaut.matchers;

import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;

public interface TransactionMatcher {
    boolean isMatch(Expense expense, CreditCardActivity creditCardActivity);
    String getType();
}
