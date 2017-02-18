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
            for (MatchedTransaction matchedTransaction : matchedTransactions) {
                CreditCardActivity matchedCreditCardActivity = matchedTransaction.getMatchedCreditCardActivity();
                if (unmatchedCreditCardActivities.contains(matchedCreditCardActivity)) {
                    unmatchedCreditCardActivities.remove(matchedCreditCardActivity);
                }
            }
        }
        return unmatchedCreditCardActivities;
    }
}

