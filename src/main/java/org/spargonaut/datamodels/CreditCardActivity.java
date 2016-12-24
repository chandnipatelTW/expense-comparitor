package org.spargonaut.datamodels;

import lombok.Getter;
import org.joda.time.DateTime;

@Getter
public class CreditCardActivity {

    private final ActivityType type;
    private final DateTime transactionDate;
    private final DateTime postDate;
    private final String description;
    private final String amount;

    public CreditCardActivity(String type, DateTime transactionDate, DateTime postDate, String description, String amount) {
        this.type = ActivityType.fromString(type);
        this.transactionDate = transactionDate;
        this.postDate = postDate;
        this.description = description;
        this.amount = amount;
    }
}
