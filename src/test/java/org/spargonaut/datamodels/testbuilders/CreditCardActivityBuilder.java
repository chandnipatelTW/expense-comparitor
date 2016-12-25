package org.spargonaut.datamodels.testbuilders;

import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.spargonaut.datamodels.ActivityType;
import org.spargonaut.datamodels.CreditCardActivity;

import java.math.BigDecimal;
import java.util.Random;

public class CreditCardActivityBuilder {

    Random randomizer = new Random();

    private ActivityType type = ActivityType.SALE;
    private DateTime transactionDate = new DateTime(2016, 12, 15, 0, 0);
    private DateTime postDate = new DateTime(2017, 1, 1, 0, 0);
    private String description = RandomStringUtils.random(20);
    private BigDecimal amount = new BigDecimal(randomizer.nextDouble());

    public CreditCardActivity build() {
        return new CreditCardActivity(type.getValue(), transactionDate, postDate, description, amount);
    }
}
