package org.spargonaut.io.parser;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.spargonaut.datamodels.ActivityType;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.io.CSVFileReader;

import java.io.File;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public class JPMCChargeParser implements Parser<CreditCardActivity> {

    private CSVFileReader csvFileReader;

    public JPMCChargeParser(CSVFileReader csvFileReader) {
        this.csvFileReader = csvFileReader;
    }

    public Set<CreditCardActivity> parseFile(File chargeFile) {
        String chargeDelimiter = ",";
        return csvFileReader.readCsvFile(chargeFile).stream()
                .filter(this::isParsable)
                .map(chargeLine -> parseCreditCardActivity(chargeDelimiter, chargeLine))
                .collect(Collectors.toSet());
    }

    private boolean isParsable(String line) {
        return !(isHeaderLine(line) || isCommentLine(line));
    }

    private CreditCardActivity parseCreditCardActivity(String chargeDelimiter, String chargeLine) {
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
