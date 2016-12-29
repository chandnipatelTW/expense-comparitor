package org.spargonaut.datamodels;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.joda.time.DateTime;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode
public class CreditCardActivity {

    private final ActivityType type;
    private final DateTime transactionDate;
    private final DateTime postDate;
    private final String description;
    private final BigDecimal amount;

    public CreditCardActivity(String type, DateTime transactionDate, DateTime postDate, String description, BigDecimal amount) {
        this.type = ActivityType.fromString(type);
        this.transactionDate = transactionDate;
        this.postDate = postDate;
        this.description = description;
        this.amount = amount;
    }
}
