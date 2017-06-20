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

        CreditCardActivity expectedCreditCardActivity =
                createExpectedCreditCardActivity(
                        date(10, 12, 2016),
                        date(11, 12, 2016),
                        chargeAmount(-18.09),
                        "UBER   *US DEC09 DFMHE");

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

    @Test
    public void shouldRemoveCommasFromTheDescriptionField() {
        CSVFileReader anotherMockCSVFileReader = mock(CSVFileReader.class);
        String chargeLineWithACommaInTheDescription = "Sale,10/12/2015,10/13/2015,King, Schools, Inc.,-43.26";
        when(anotherMockCSVFileReader.readCsvFile(mockFile)).thenReturn(new HashSet<>(Arrays.asList(chargeLineWithACommaInTheDescription)));

        ChargeParser chargeParser = new ChargeParser(anotherMockCSVFileReader);

        CreditCardActivity expectedCreditCardActivity =
                createExpectedCreditCardActivity(
                        date(12, 10, 2015),
                        date(13, 10, 2015),
                        chargeAmount(-43.26),
                        "King Schools Inc.");

        Set<CreditCardActivity> creditCardActivityList = chargeParser.parseFile(mockFile);
        assertThat(creditCardActivityList.contains(expectedCreditCardActivity), is(true));
    }

    @Test
    public void shouldIgnoreBlankLines() {
        CSVFileReader anotherMockCSVFileReader = mock(CSVFileReader.class);
        String chargeLineWithACommaInTheDescription = "Sale,10/12/2015,10/13/2015,King, Schools, Inc.,-43.26";
        String blankLine = "";
        when(anotherMockCSVFileReader.readCsvFile(mockFile)).thenReturn(new HashSet<>(Arrays.asList(chargeLineWithACommaInTheDescription, blankLine)));

        ChargeParser chargeParser = new ChargeParser(anotherMockCSVFileReader);

        CreditCardActivity expectedCreditCardActivity =
                createExpectedCreditCardActivity(
                        date(12, 10, 2015),
                        date(13, 10, 2015),
                        chargeAmount(-43.26),
                        "King Schools Inc.");

        Set<CreditCardActivity> creditCardActivityList = chargeParser.parseFile(mockFile);
        assertThat(creditCardActivityList.size(), is(2));
        assertThat(creditCardActivityList.contains(expectedCreditCardActivity), is(true));
    }

    private CreditCardActivity createExpectedCreditCardActivity(DateTime expectedTransactionDate, DateTime expectedPostDate, BigDecimal expectedAmount, String expectedDescription) {
        return new CreditCardActivityBuilder()
                .setType(ActivityType.SALE)
                .setAmount(expectedAmount.doubleValue())
                .setDescription(expectedDescription)
                .setPostDate(expectedPostDate)
                .setTransactionDate(expectedTransactionDate)
                .build();
    }

    private BigDecimal chargeAmount(double amount) {
        BigDecimal expectedAmount = new BigDecimal(amount);
        expectedAmount = expectedAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return expectedAmount;
    }

    private DateTime date(int dayOfMonth, int monthOfYear, int year) {
        return new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
    }
}
