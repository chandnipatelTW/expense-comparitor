package org.spargonaut.datamodels;

public class CreditCardActivity {

    private final ActivityType type;
    private final String transactionDate;
    private final String postDate;
    private final String description;
    private final String amount;

    public CreditCardActivity(String type, String transactionDate, String postDate, String description, String amount) {
        this.type = ActivityType.fromString(type);
        this.transactionDate = transactionDate;
        this.postDate = postDate;
        this.description = description;
        this.amount = amount;
    }

    public String getType() {
        return type.getValue();
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getPostDate() {
        return postDate;
    }

    public String getDescription() {
        return description;
    }

    public String getAmount() {
        return amount;
    }
}
