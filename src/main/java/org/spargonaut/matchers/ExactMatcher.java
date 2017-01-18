package org.spargonaut.matchers;

import org.joda.time.DateTime;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;

import java.math.BigDecimal;

public class ExactMatcher implements TransactionMatcher {
    public boolean isMatch(Expense expense, CreditCardActivity creditCardActivity) {
        BigDecimal creditCardActivityAmount = creditCardActivity.getAmount();
        double positiveCreditCardActivityAmount = Math.abs(creditCardActivityAmount.doubleValue());
        double expenseAmount = expense.getAmount().doubleValue();

        DateTime expenseDate = expense.getTimestamp();

        DateTime creditCardActivityTransactionDate = creditCardActivity.getTransactionDate();
        return expenseAmount == positiveCreditCardActivityAmount && expenseDate.equals(creditCardActivityTransactionDate);
    }

    public String getType() {
        return "Exact Amount, Exact Date";
    }
}
