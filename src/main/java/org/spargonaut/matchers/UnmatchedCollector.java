package org.spargonaut.matchers;

import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.datamodels.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UnmatchedCollector<T extends Transaction> {

    public List<T> collect(List<T> items, Collection<List<MatchedTransaction>> matchedTransactionLists) {
        List<T> unmatchedItems = new ArrayList<>(items);
        for (List<MatchedTransaction> matchedTransactions : matchedTransactionLists) {
            for (T item : items) {
                for (MatchedTransaction matchedTransaction : matchedTransactions) {
                    if (matchedTransaction.contains(item)) {
                        unmatchedItems.remove(item);
                    }
                }
            }
        }
        return unmatchedItems;
    };
}