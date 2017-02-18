package org.spargonaut.matchers;

import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.datamodels.Transaction;

import java.util.List;

public class PreviousMatchDetector {
    public boolean isPreviouslyMatched(Transaction transaction, List<MatchedTransaction> matchedTransactionList) {
        boolean isMatched = false;
        for (MatchedTransaction matchedTransaction : matchedTransactionList) {
            isMatched = matchedTransaction.contains(transaction) || isMatched;
        }
        return isMatched;
    }
}
