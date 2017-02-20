package org.spargonaut.io.parser;

import org.joda.time.DateTime;
import org.spargonaut.datamodels.ActivityType;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.io.CSVFileReader;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ChargeParser implements Parser<CreditCardActivity> {

    CSVFileReader csvFileReader;

    public ChargeParser(CSVFileReader csvFileReader) {
        this.csvFileReader = csvFileReader;
    }

    public Set<CreditCardActivity> parseFile(File chargeFile) {
        String chargeDelimiter = ",";

        Set<String> chargeLines = csvFileReader.readCsvFile(chargeFile);
        Set<CreditCardActivity> creditCardActivities = new HashSet<>();

        for (String chargeLine : chargeLines) {
            if (isHeaderLine(chargeLine) || isCommentLine(chargeLine)) { continue; }
            String[] chargeTokens = chargeLine.split(chargeDelimiter);
            DateTime transactionDate = createDateTimeFrom(chargeTokens[1]);
            DateTime postDate = createDateTimeFrom(chargeTokens[2]);

            BigDecimal amount = new BigDecimal(chargeTokens[4]);
            amount = amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);

            CreditCardActivity creditCardActivity = new CreditCardActivity(
                    ActivityType.fromString(chargeTokens[0]),
                    transactionDate,
                    postDate,
                    chargeTokens[3],
                    amount);
            creditCardActivities.add(creditCardActivity);
        }

        return creditCardActivities;
    }

    private boolean isCommentLine(String chargeLine) {
        return chargeLine.startsWith("#");
    }

    private DateTime createDateTimeFrom(String chargeToken) {
        String[] transactionDateChunks = chargeToken.split("/");
        int transactionYearString = Integer.parseInt(transactionDateChunks[2]);
        int transactionMonthString = Integer.parseInt(transactionDateChunks[0]);
        int transactionDayString = Integer.parseInt(transactionDateChunks[1]);
        return new DateTime(transactionYearString, transactionMonthString, transactionDayString, 0, 0);
    }

    private boolean isHeaderLine(String chargeLine) {
        return chargeLine.equals("Type,Trans Date,Post Date,Description,Amount");
    }
}
