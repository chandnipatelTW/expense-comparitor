package org.spargonaut.datamodels;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.joda.time.DateTime;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode(exclude = {"postDate", "fileLocation"})
@AllArgsConstructor
public class CreditCardActivity implements Transaction {

    private final ActivityType type;
    private final DateTime transactionDate;
    private final DateTime postDate;
    private final String description;
    private final BigDecimal amount;
    private final String fileLocation;
}
