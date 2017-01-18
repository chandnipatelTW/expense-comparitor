package org.spargonaut.matchers;

import org.apache.commons.text.similarity.FuzzyScore;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;

import java.math.BigDecimal;
import java.util.Locale;

public class FuzzyMerchantExactAmountMatcher implements TransactionMatcher {
    @Override
    public boolean isMatch(Expense expense, CreditCardActivity creditCardActivity) {

        BigDecimal creditCardActivityAmount = creditCardActivity.getAmount();
        double positiveCreditCardActivityAmount = Math.abs(creditCardActivityAmount.doubleValue());
        double expenseAmount = expense.getAmount().doubleValue();
        if (expenseAmount != positiveCreditCardActivityAmount) {
            return false;
        }

        String creditCardActivityDescription = creditCardActivity.getDescription().toUpperCase();
        String expenseMerchant = expense.getMerchant().toUpperCase();

        FuzzyScore fuzzyScorer = new FuzzyScore(Locale.US);
        Integer fuzzyScore = fuzzyScorer.fuzzyScore(creditCardActivityDescription, expenseMerchant);

        return fuzzyScore >= 10;
    }

    @Override
    public String getType() {
        return "Exact Amount Close Merchant";
    }
}
