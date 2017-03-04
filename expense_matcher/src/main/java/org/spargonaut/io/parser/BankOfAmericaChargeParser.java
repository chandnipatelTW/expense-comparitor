package org.spargonaut.io.parser;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.spargonaut.datamodels.ActivityType;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.io.CSVFileReader;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class BankOfAmericaChargeParser {
    private final CSVFileReader csvFileReader;

    public BankOfAmericaChargeParser(CSVFileReader csvFileReader) {
        this.csvFileReader = csvFileReader;
    }

    public Set<CreditCardActivity> parseFile(File mockFile) {

        Set<CreditCardActivity> creditCardActivities = new HashSet<>();

        Set<String> chargeLines = csvFileReader.readCsvFile(mockFile);
        for (String chargeLine : chargeLines) {
            if (!StringUtils.isBlank(chargeLine) &&
                    !isHeaderLine(chargeLine)) {
                ActivityType defaultActivityType = ActivityType.SALE;
                DateTime defaultTransactionDate = new DateTime(1999, 12, 23, 0, 0, 0);


                String[] chargeTokens = chargeLine.split(",");

                String postDateString = chargeTokens[0];
                String[] postDateTokens = postDateString.split("/");
                int postMonth = Integer.parseInt(postDateTokens[0]);
                int postDay = Integer.parseInt(postDateTokens[1]);
                int postYear = Integer.parseInt(postDateTokens[2]);
                DateTime postDate = new DateTime(postYear, postMonth, postDay, 0, 0, 0);

                String desctription = chargeTokens[2].replace("\"", "");


                BigDecimal amount = new BigDecimal(chargeTokens[4]);
                amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);

                CreditCardActivity creditCardActivity = new CreditCardActivity(
                        defaultActivityType,
                        defaultTransactionDate,
                        postDate,
                        desctription,
                        amount);
                creditCardActivities.add(creditCardActivity);
            }
        }

        return creditCardActivities;
    }

    private boolean isHeaderLine(String chargeLine) {
        return chargeLine.equals("Posted Date,Reference Number,Payee,Address,Amount");
    }
}
