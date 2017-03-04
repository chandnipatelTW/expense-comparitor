package org.spargonaut.io.parser;

import org.joda.time.DateTime;
import org.junit.Test;
import org.spargonaut.datamodels.ActivityType;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.testbuilders.CreditCardActivityBuilder;
import org.spargonaut.io.CSVFileReader;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BankOfAmericaChargeParserTest {

    @Test
    public void shouldParseBankOfAmericaChargeLineIntoACreditCardActivity() {
        String chargeLine = "02/07/2017,24492157037637005325047,\"DOORDASH-MCALISTERS DE 650-681-9470 CA\",\"650-681-9470  CA \",-75.99";

        File mockFile = mock(File.class);
        CSVFileReader mockCSVFileReader = mock(CSVFileReader.class);
        Set<String> chargeStrings = new HashSet<>(Arrays.asList(chargeLine));
        when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(chargeStrings);

        BankOfAmericaChargeParser bankOfAmericaChargeParser = new BankOfAmericaChargeParser(mockCSVFileReader);

        DateTime expectedPostDate = new DateTime(2017, 02, 07, 0, 0, 0);
        DateTime expectedTransactionDate = new DateTime(1999, 12, 23, 0, 0, 0);
        String expectedDescription = "DOORDASH-MCALISTERS DE 650-681-9470 CA";
        BigDecimal expectedAmount = new BigDecimal(-75.99);
        expectedAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);

        CreditCardActivity expectedCreditCardActivity = new CreditCardActivityBuilder()
                .setType(ActivityType.SALE)
                .setAmount(expectedAmount.doubleValue())
                .setDescription(expectedDescription)
                .setPostDate(expectedPostDate)
                .setTransactionDate(expectedTransactionDate)
                .build();

        Set<CreditCardActivity> creditCardActivitySet = bankOfAmericaChargeParser.parseFile(mockFile);

        assertThat(creditCardActivitySet.contains(expectedCreditCardActivity), is(true));
    }

    @Test
    public void shouldIgnoreBlankLines() {
        String blankLine = "";

        File mockFile = mock(File.class);
        CSVFileReader mockCSVFileReader = mock(CSVFileReader.class);
        Set<String> chargeStrings = new HashSet<>(Arrays.asList(blankLine));
        when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(chargeStrings);
        BankOfAmericaChargeParser bankOfAmericaChargeParser = new BankOfAmericaChargeParser(mockCSVFileReader);

        Set<CreditCardActivity> creditCardActivitySet = bankOfAmericaChargeParser.parseFile(mockFile);

        assertThat(creditCardActivitySet.isEmpty(), is(true));
    }
}
