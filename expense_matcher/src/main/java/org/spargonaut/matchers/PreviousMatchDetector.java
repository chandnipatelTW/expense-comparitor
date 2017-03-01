package org.spargonaut.matchers;

import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.datamodels.Transaction;

import java.util.Set;

public class PreviousMatchDetector {
    public boolean isPreviouslyMatched(Transaction transaction, Set<MatchedTransaction> matchedTransactionList) {
        boolean transactionIsDetectedAsMatched = matchedTransactionList
                .stream()
                .filter(matchedTransaction -> matchedTransaction.contains(transaction))
                .findAny()
                .orElse(null)
                != null;
        return transactionIsDetectedAsMatched;
    }
}
