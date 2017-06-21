package org.spargonaut.io.parser;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.spargonaut.datamodels.ActivityType;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.io.CSVFileReader;

import java.io.File;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BankOfAmericaChargeParser implements Parser<CreditCardActivity> {
    private final CSVFileReader csvFileReader;

    public BankOfAmericaChargeParser(CSVFileReader csvFileReader) {
        this.csvFileReader = csvFileReader;
    }

    public Set<CreditCardActivity> parseFile(File chargeFile) {
        String chargeDelimiter = ",";

        return csvFileReader.readCsvFile(chargeFile).stream()
                .filter(this::isParsable)
                .map(chargeLine -> parseCreditCardActivity(chargeDelimiter, chargeLine))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private CreditCardActivity parseCreditCardActivity(String chargeDelimiter, String chargeLine) {
        CreditCardActivity creditCardActivity = null;
        if (!StringUtils.isBlank(chargeLine)) {
            ActivityType defaultActivityType = ActivityType.SALE;
            DateTime defaultTransactionDate = new DateTime(1999, 12, 23, 0, 0, 0);


            String[] chargeTokens = chargeLine.split(chargeDelimiter);

            String postDateString = chargeTokens[0];
            String[] postDateTokens = postDateString.split("/");
            int postMonth = Integer.parseInt(postDateTokens[0]);
            int postDay = Integer.parseInt(postDateTokens[1]);
            int postYear = Integer.parseInt(postDateTokens[2]);
            DateTime postDate = new DateTime(postYear, postMonth, postDay, 0, 0, 0);

            String desctription = chargeTokens[2].replace("\"", "");

            BigDecimal amount = new BigDecimal(chargeTokens[4]);
            amount = amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);

            creditCardActivity = new CreditCardActivity(
                    defaultActivityType,
                    defaultTransactionDate,
                    postDate,
                    desctription,
                    amount);
        }
        return creditCardActivity;
    }

    private boolean isParsable(String chargeLine) {
        return !(isHeaderLine(chargeLine) || isCommentLine(chargeLine));
    }

    private boolean isCommentLine(String chargeLine) {
        return chargeLine.startsWith("#");
    }

    private boolean isHeaderLine(String chargeLine) {
        return chargeLine.equals("Posted Date,Reference Number,Payee,Address,Amount");
    }
}
