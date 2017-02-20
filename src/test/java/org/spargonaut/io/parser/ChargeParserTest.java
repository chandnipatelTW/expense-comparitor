package org.spargonaut.io.parser;

import org.joda.time.DateTime;
import org.junit.Before;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChargeParserTest {

    private CSVFileReader mockCSVFileReader;
    private File mockFile;

    @Before
    public void setUp() {
        String headerLine = "Type,Trans Date,Post Date,Description,Amount";
        String chargeLine = "Sale,12/10/2016,12/11/2016,UBER   *US DEC09 DFMHE,-18.09";
        String commentLine = "# this line is a comment because it starts with a hash";
        Set<String> chargeStrings = new HashSet<>(Arrays.asList(headerLine, chargeLine, commentLine));

        mockFile = mock(File.class);
        mockCSVFileReader = mock(CSVFileReader.class);
        when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(chargeStrings);
    }

    @Test
    public void shouldParseAChargeLineIntoACreditCardActivity() {
        ChargeParser chargeParser = new ChargeParser(mockCSVFileReader);

        DateTime expectedTransactionDate = new DateTime(2016, 12, 10, 0, 0);
        DateTime expectedPostDate = new DateTime(2016, 12, 11, 0, 0);
        BigDecimal expectedAmount = new BigDecimal(-18.09);
        expectedAmount = expectedAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        String expectedDescription = "UBER   *US DEC09 DFMHE";

        CreditCardActivity expectedCreditCardActivity = new CreditCardActivityBuilder()
                .setType(ActivityType.SALE)
                .setAmount(expectedAmount.doubleValue())
                .setDescription(expectedDescription)
                .setPostDate(expectedPostDate)
                .setTransactionDate(expectedTransactionDate)
                .build();

        Set<CreditCardActivity> creditCardActivityList = chargeParser.parseFile(mockFile);
        assertThat(creditCardActivityList.contains(expectedCreditCardActivity), is(true));
    }

    @Test
    public void shouldIgnoreTheHeaderLineInTheCreditCardActivityFile() {
        ChargeParser chargeParser = new ChargeParser(mockCSVFileReader);
        Set<CreditCardActivity> creditCardActivityList = chargeParser.parseFile(mockFile);
        assertThat(creditCardActivityList.size(), is(1));
    }

    @Test
    public void shouldIgnoreLinesThatStartWithHashSymbol() {
        ChargeParser chargeParser = new ChargeParser(mockCSVFileReader);
        Set<CreditCardActivity> creditCardActivityList = chargeParser.parseFile(mockFile);
        assertThat(creditCardActivityList.size(), is(1));
    }
}
