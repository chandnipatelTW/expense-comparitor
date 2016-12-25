package org.spargonaut.datamodels;

import lombok.Getter;
import org.joda.time.DateTime;

import java.math.BigDecimal;

@Getter
public class Expense {
    private final DateTime timestamp;
    private final String merchant;
    private final BigDecimal amount;
    private final String mcc;
    private final String category;
    private final String tag;
    private final String comment;
    private final boolean reimbursable;
    private final String originalCurrency;
    private final BigDecimal originalAmount;
    private final String receiptURL;

    public Expense(DateTime timestamp, String merchant, BigDecimal amount, String mcc, String category, String tag, String comment, boolean reimbursable, String originalCurrency, BigDecimal originalAmount, String receiptURL) {
        this.timestamp = timestamp;
        this.merchant = merchant;
        this.amount = amount;
        this.mcc = mcc;
        this.category = category;
        this.tag = tag;
        this.comment = comment;
        this.reimbursable = reimbursable;
        this.originalCurrency = originalCurrency;
        this.originalAmount = originalAmount;
        this.receiptURL = receiptURL;
    }
}
