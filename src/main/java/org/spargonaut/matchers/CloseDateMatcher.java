package org.spargonaut.matchers;

import org.joda.time.DateTime;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;

import java.math.BigDecimal;

public class CloseDateMatcher {
    public static boolean isMatch(Expense expense, CreditCardActivity creditCardActivity) {
        BigDecimal creditCardActivityAmount = creditCardActivity.getAmount();
        double positiveCreditCardActivityAmount = Math.abs(creditCardActivityAmount.doubleValue());
        double expenseAmount = expense.getAmount().doubleValue();

        DateTime expenseDate = expense.getTimestamp();
        DateTime dayAfterTransactionDate = creditCardActivity.getTransactionDate().plusDays(1);

        boolean dateIsWithinTolerance = expenseDate.equals(dayAfterTransactionDate);

        return expenseAmount == positiveCreditCardActivityAmount && dateIsWithinTolerance;
    }
}
