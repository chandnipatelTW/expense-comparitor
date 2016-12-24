package org.spargonaut.io;

import org.joda.time.DateTime;
import org.spargonaut.datamodels.CreditCardActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChargeParser {

    CSVFileReader csvFileReader;

    public ChargeParser(CSVFileReader csvFileReader) {
        this.csvFileReader = csvFileReader;
    }

    public List<CreditCardActivity> parseCharges(File chargeFile) {
        String chargeDelimiter = ",";

        List<String> chargeLines = csvFileReader.readCreditCardFile(chargeFile);
        List<CreditCardActivity> creditCardActivities = new ArrayList<>();

        for (String chargeLine : chargeLines) {
            if (isHeaderLine(chargeLine)) { continue; }
            String[] chargeTokens = chargeLine.split(chargeDelimiter);
            DateTime transactionDate = createDateTimeFrom(chargeTokens[1]);
            DateTime postDate = createDateTimeFrom(chargeTokens[1]);

            CreditCardActivity creditCardActivity = new CreditCardActivity(
                    chargeTokens[0],
                    transactionDate,
                    postDate,
                    chargeTokens[3],
                    chargeTokens[4]);
            creditCardActivities.add(creditCardActivity);
        }

        return creditCardActivities;
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
