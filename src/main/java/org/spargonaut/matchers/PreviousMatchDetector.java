package org.spargonaut.matchers;

import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.datamodels.Transaction;

import java.util.List;

public class PreviousMatchDetector {
    public boolean isPreviouslyMatched(Transaction transaction, List<MatchedTransaction> matchedTransactionList) {
        for (MatchedTransaction matchedTransaction : matchedTransactionList) {
            if (matchedTransaction.contains(transaction)) {
                return true;
            };
        }
        return false;
    }
}
