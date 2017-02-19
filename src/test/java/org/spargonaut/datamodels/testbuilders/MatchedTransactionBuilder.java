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

    public MatchedTransactionBuilder withCreditCardActivity(CreditCardActivity creditCardActivity) {
       this.matchedCreditCardActivity = creditCardActivity;

       return this;
    }

    public MatchedTransactionBuilder withExpense(Expense matchedExpense) {
       this.matchedExpense = matchedExpense;
        return this;
    }

    public MatchedTransaction build() {
        this.matchedExpense = this.matchedExpense == null ? new ExpenseBuilder().build() : this.matchedExpense;
        this.matchedCreditCardActivity = this.matchedCreditCardActivity == null ? new CreditCardActivityBuilder().build() : this.matchedCreditCardActivity;
        return new MatchedTransaction(matchedCreditCardActivity, matchedExpense);
    }
}
