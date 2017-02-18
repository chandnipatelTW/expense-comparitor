package org.spargonaut;

import org.junit.Test;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.datamodels.testbuilders.CreditCardActivityBuilder;
import org.spargonaut.datamodels.testbuilders.MatchedTransactionBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UnmatchedCreditCardActivityCollectorTest {
    @Test
    public void shouldCollectUnmatchedCreditCardactivities() {
        CreditCardActivity creditCardActivityOne = new CreditCardActivityBuilder().build();
        MatchedTransaction matchedTransactionOne = new MatchedTransactionBuilder()
                .fromCreditCardActivity(creditCardActivityOne)
                .build();

        CreditCardActivity creditCardActivityTwo = new CreditCardActivityBuilder().build();
        MatchedTransaction matchedTransactionTwo = new MatchedTransactionBuilder()
                .fromCreditCardActivity(creditCardActivityTwo)
                .build();

        CreditCardActivity creditCardActivityThree = new CreditCardActivityBuilder().build();
        MatchedTransaction matchedTransactionThree = new MatchedTransactionBuilder()
                .fromCreditCardActivity(creditCardActivityThree)
                .build();

        CreditCardActivity creditCardActivityFour = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityFive = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivitySix = new CreditCardActivityBuilder().build();

        List<CreditCardActivity> creditCardActivityList = Arrays.asList(creditCardActivityOne,
                creditCardActivityTwo,
                creditCardActivityThree,
                creditCardActivityFour,
                creditCardActivityFive,
                creditCardActivitySix);
        List<MatchedTransaction> matchedTransactionList = Arrays.asList(matchedTransactionOne, matchedTransactionTwo, matchedTransactionThree);

        Map<String, List<MatchedTransaction>> matchedTransactionMap = new HashMap<>();
        matchedTransactionMap.put("someMatcher", matchedTransactionList);

        UnmatchedCreditCardActivityCollector unmatchedCreditCardActivityCollector = new UnmatchedCreditCardActivityCollector();
        List<CreditCardActivity> actualUnmatchedCreditCardActivities = unmatchedCreditCardActivityCollector.collect(creditCardActivityList, matchedTransactionMap.values());

        assertThat(actualUnmatchedCreditCardActivities.size(), is(3));
        assertThat(actualUnmatchedCreditCardActivities.contains(creditCardActivityFour), is(true));
        assertThat(actualUnmatchedCreditCardActivities.contains(creditCardActivityFive), is(true));
        assertThat(actualUnmatchedCreditCardActivities.contains(creditCardActivitySix), is(true));
    }
}
