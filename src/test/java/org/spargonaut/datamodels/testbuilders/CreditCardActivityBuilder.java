package org.spargonaut.datamodels.testbuilders;

import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.spargonaut.datamodels.ActivityType;
import org.spargonaut.datamodels.CreditCardActivity;

import java.math.BigDecimal;
import java.util.Random;

public class CreditCardActivityBuilder {

    private Random randomizer = new Random();

    private ActivityType type = ActivityType.SALE;
    private DateTime transactionDate = DateTime.now();
    private DateTime postDate = DateTime.now().minusDays(randomizer.nextInt());
    private String description = RandomStringUtils.random(20);
    private BigDecimal amount = new BigDecimal(randomizer.nextDouble());

    public CreditCardActivity build() {
        return new CreditCardActivity(type, transactionDate, postDate, description, amount);
    }

    public CreditCardActivityBuilder setType(ActivityType type) {
        this.type = type;
        return this;
    }

    public CreditCardActivityBuilder setTransactionDate(DateTime transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public CreditCardActivityBuilder setPostDate(DateTime postDate) {
        this.postDate = postDate;
        return this;
    }

    public CreditCardActivityBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public CreditCardActivityBuilder setAmount(double amount) {
        BigDecimal formattedBigDecimal = new BigDecimal(amount);
        this.amount = formattedBigDecimal.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return this;
    }
}
