package org.spargonaut;

import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.MatchedTransaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UnmatchedCreditCardActivityCollector {

    public List<CreditCardActivity> collect(List<CreditCardActivity> creditCardActivities, Collection<List<MatchedTransaction>> matchedTransactionList) {
        List<CreditCardActivity> unmatchedCreditCardActivities = new ArrayList<>(creditCardActivities);
        for (List<MatchedTransaction> matchedTransactions : matchedTransactionList) {
            for (CreditCardActivity creditCardActivity : creditCardActivities) {
                for (MatchedTransaction matchedTransaction : matchedTransactions) {
                    if (matchedTransaction.containsCreditCardActivity(creditCardActivity)) {
                        unmatchedCreditCardActivities.remove(creditCardActivity);
                    }
                }
            }

        }
        return unmatchedCreditCardActivities;
    }
}

