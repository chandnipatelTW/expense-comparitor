package org.spargonaut.datamodels;

public class Expense {
    private final String timestamp;
    private final String merchant;
    private final String amount;
    private final String mcc;
    private final String category;
    private final String tag;
    private final String comment;
    private final String reimbursable;
    private final String originalCurrency;
    private final String originalAmount;
    private final String receiptURL;
    private String MCC;

    public Expense(String timestamp, String merchant, String amount, String mcc, String category, String tag, String comment, String reimbursable, String originalCurrency, String originalAmount, String receiptURL) {
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

    public String getTimestamp() {
        return this.timestamp;
    }

    public String getMerchant() {
        return this.merchant;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getMCC() {
        return this.mcc;
    }

    public String getCategory() {
        return this.category;
    }

    public String getTag() {
        return this.tag;
    }

    public String getComment() {
        return this.comment;
    }

    public String isReimbursable() {
        return this.reimbursable;
    }

    public String getOriginalCurrency() {
        return this.originalCurrency;
    }

    public String getOriginalAmount() {
        return this.originalAmount;
    }

    public String getRecieptURL() {
        return this.receiptURL;
    }
}
