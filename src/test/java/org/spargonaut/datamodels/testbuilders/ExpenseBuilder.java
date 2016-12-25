package org.spargonaut.datamodels.testbuilders;

import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.DateTime;
import org.spargonaut.datamodels.Expense;

import java.math.BigDecimal;
import java.util.Random;

public class ExpenseBuilder {

    private Random randomizer = new Random();

    private DateTime timestamp = new DateTime(2016, 12, 25, 0, 0);
    private String merchant = RandomStringUtils.random(10);
    private BigDecimal amount = new BigDecimal(randomizer.nextDouble());
    private String mcc = randomizer.nextInt() + "";
    private String category = RandomStringUtils.random(15);
    private String tag = RandomStringUtils.random(15);
    private String comment = RandomStringUtils.random(15);
    private boolean reimbursable = randomizer.nextBoolean();
    private String originalCurrency = RandomStringUtils.random(3);
    private BigDecimal originalAmount = new BigDecimal(randomizer.nextDouble());
    private String receiptURL = RandomStringUtils.random(50);

    public Expense build() {
        return new Expense(timestamp, merchant, amount, mcc, category, tag, comment, reimbursable, originalCurrency, originalAmount, receiptURL);
    }
}
