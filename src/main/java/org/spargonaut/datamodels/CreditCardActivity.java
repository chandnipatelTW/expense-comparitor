package org.spargonaut.datamodels;

import lombok.Getter;

@Getter
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
}
