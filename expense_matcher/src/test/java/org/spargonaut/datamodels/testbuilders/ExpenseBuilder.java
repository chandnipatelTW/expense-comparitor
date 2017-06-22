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
    private String fileLocation;

    public Expense build() {
        return new Expense(timestamp, merchant, amount, mcc, category, tag, comment, reimbursable, originalCurrency, originalAmount, receiptURL, fileLocation);
    }

    public ExpenseBuilder setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public ExpenseBuilder setMerchant(String merchant) {
        this.merchant = merchant;
        return this;
    }

    public ExpenseBuilder setAmount(double amount) {
        BigDecimal formattedBigDecimal = new BigDecimal(amount);
        this.amount = formattedBigDecimal.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return this;
    }

    public ExpenseBuilder setBigDecimalAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public ExpenseBuilder setMcc(String mcc) {
        this.mcc = mcc;
        return this;
    }

    public ExpenseBuilder setCategory(String category) {
        this.category = category;
        return this;
    }

    public ExpenseBuilder setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public ExpenseBuilder setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public ExpenseBuilder setReimbursable(boolean reimbursable) {
        this.reimbursable = reimbursable;
        return this;
    }

    public ExpenseBuilder setOriginalCurrency(String originalCurrency) {
        this.originalCurrency = originalCurrency;
        return this;
    }

    public ExpenseBuilder setOriginalAmount(double originalAmmount) {
        this.originalAmount = new BigDecimal(originalAmmount);
        return this;
    }

    public ExpenseBuilder setReceiptURL(String receiptUrl) {
        this.receiptURL = receiptUrl;
        return this;
    }

    public ExpenseBuilder setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
        return this;
    }
}
