package org.spargonaut.io.parser;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.spargonaut.datamodels.ActivityType;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.io.CSVFileReader;

import java.math.BigDecimal;

public class JPMCChargeParser extends ChargeParser<CreditCardActivity> {

    public JPMCChargeParser(CSVFileReader csvFileReader) {
        super(csvFileReader);
    }

    CreditCardActivity parseCreditCardActivity(String chargeDelimiter, String chargeLine) {
        CreditCardActivity creditCardActivity = null;
        if (!StringUtils.isBlank(chargeLine)) {
            String[] chargeTokens = chargeLine.split(chargeDelimiter);
            DateTime transactionDate = createDateTimeFrom(chargeTokens[1]);
            DateTime postDate = createDateTimeFrom(chargeTokens[2]);

            int lastChunkIndex = chargeTokens.length - 1;
            BigDecimal amount = new BigDecimal(chargeTokens[lastChunkIndex]);
            amount = amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);

            StringBuilder description = new StringBuilder();
            int descriptionStartIndex = 3;
            for (int i = descriptionStartIndex; i < lastChunkIndex; i++) {
                description.append(chargeTokens[i]);
            }

            creditCardActivity = new CreditCardActivity(
                    ActivityType.fromString(chargeTokens[0]),
                    transactionDate,
                    postDate,
                    description.toString(),
                    amount);
        }

        return creditCardActivity;
    }

    private DateTime createDateTimeFrom(String chargeToken) {
        String[] transactionDateChunks = chargeToken.split("/");
        int transactionYearString = Integer.parseInt(transactionDateChunks[2]);
        int transactionMonthString = Integer.parseInt(transactionDateChunks[0]);
        int transactionDayString = Integer.parseInt(transactionDateChunks[1]);
        return new DateTime(transactionYearString, transactionMonthString, transactionDayString, 0, 0);
    }

    boolean isHeaderLine(String chargeLine) {
        return chargeLine.equals("Type,Trans Date,Post Date,Description,Amount");
    }
}
