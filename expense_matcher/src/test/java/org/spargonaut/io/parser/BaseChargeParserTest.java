package org.spargonaut.io.parser;

import org.joda.time.DateTime;
import org.spargonaut.datamodels.ActivityType;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.testbuilders.CreditCardActivityBuilder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

abstract class BaseChargeParserTest {

    void assertParsedFileContainsActivity(Set<CreditCardActivity> creditCardActivitySet, CreditCardActivity creditCardActivity) {
        assertThat(creditCardActivitySet.contains(creditCardActivity), is(true));
    }

    void assertParsedSetIsSize(Set<CreditCardActivity> creditCardActivitySet, int size) {
        assertThat(creditCardActivitySet.size(), is(size));
    }

    CreditCardActivity createExpectedCreditCardActivity(DateTime transactionDate, DateTime postDate, double amount, String description) {
        return new CreditCardActivityBuilder()
                .setType(ActivityType.SALE)
                .setAmount(amount)
                .setDescription(description)
                .setPostDate(postDate)
                .setTransactionDate(transactionDate)
                .build();
    }

    double chargeAmount(double amount) {
        BigDecimal expectedAmount = new BigDecimal(amount);
        expectedAmount = expectedAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return expectedAmount.doubleValue();
    }

    DateTime date(int dayOfMonth, int monthOfYear, int year) {
        return new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
    }

    HashSet<String> aHashSetOf(String... chargeLines) {
        return new HashSet<>(Arrays.asList(chargeLines));
    }
}