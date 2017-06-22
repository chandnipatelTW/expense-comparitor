package org.spargonaut.datamodels;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.joda.time.DateTime;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode (of = {"timestamp", "merchant", "amount"})
@AllArgsConstructor
public class Expense implements Transaction {
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
    private final String fileLocation;
}
