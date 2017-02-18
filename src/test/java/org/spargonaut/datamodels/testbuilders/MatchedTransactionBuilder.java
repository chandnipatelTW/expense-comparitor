package org.spargonaut.datamodels.testbuilders;

import org.spargonaut.datamodels.ActivityType;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;

public class MatchedTransactionBuilder {

    private CreditCardActivity matchedCreditCardActivity = null;
    private Expense matchedExpense = null;

   public MatchedTransactionBuilder fromExpense(Expense expense) {
        this.matchedExpense = expense;
        matchedCreditCardActivity = new CreditCardActivityBuilder()
                .setType(ActivityType.SALE)
                .setTransactionDate(expense.getTimestamp())
                .setDescription(expense.getMerchant())
                .setAmount(matchedExpense.getAmount().doubleValue())
                .build();
        return this;
    }

    public MatchedTransactionBuilder fromCreditCardActivity(CreditCardActivity creditCardActivity) {
       this.matchedCreditCardActivity = creditCardActivity;
       matchedExpense = new ExpenseBuilder().build();
       return this;
    }

    public MatchedTransaction build() {
        return new MatchedTransaction(matchedCreditCardActivity, matchedExpense);
    }
}
