package org.spargonaut.matchers;

import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.datamodels.Transaction;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class UnmatchedCollector<T extends Transaction> {

    public Set<T> collect(Set<T> items, Collection<Set<MatchedTransaction>> matchedTransactionLists) {
        Class itemClassType = items.iterator().next().getClass();
        Set<Transaction> matchedItems = matchedTransactionLists.stream()
                .flatMap(Set::stream)
                .map( matchedTransaction -> matchedTransaction.getItemOfType(itemClassType) )
                .collect(Collectors.toSet());

        return items.stream()
                .filter( item -> !matchedItems.contains(item) )
                .collect(Collectors.toSet());
    }
}