package org.spargonaut.matchers;

import org.joda.time.DateTime;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;

import java.math.BigDecimal;

public class CloseDateMatcher implements TransactionMatcher {
    public boolean isMatch(Expense expense, CreditCardActivity creditCardActivity) {
        BigDecimal creditCardActivityAmount = creditCardActivity.getAmount();
        double positiveCreditCardActivityAmount = Math.abs(creditCardActivityAmount.doubleValue());
        double expenseAmount = expense.getAmount().doubleValue();

        DateTime expenseDate = expense.getTimestamp();
        DateTime transactionDate = creditCardActivity.getTransactionDate();
        DateTime dayAfterTransactionDate = transactionDate.plusDays(1);
        DateTime dayBeforeTransactionDate = transactionDate.minusDays(1);

        boolean dateIsWithinTolerance = expenseDate.equals(dayAfterTransactionDate) || expenseDate.equals(dayBeforeTransactionDate);

        return expenseAmount == positiveCreditCardActivityAmount && dateIsWithinTolerance;
    }

    public String getType() {
        return "Close Date match";
    }
}
