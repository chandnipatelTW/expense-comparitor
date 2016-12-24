package org.spargonaut.datamodels;

import lombok.Getter;

@Getter
public class Expense {
    private final String timestamp;
    private final String merchant;
    private final String amount;
    private final String mcc;
    private final String category;
    private final String tag;
    private final String comment;
    private final boolean reimbursable;
    private final String originalCurrency;
    private final String originalAmount;
    private final String receiptURL;
    private String MCC;

    public Expense(String timestamp, String merchant, String amount, String mcc, String category, String tag, String comment, boolean reimbursable, String originalCurrency, String originalAmount, String receiptURL) {
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
