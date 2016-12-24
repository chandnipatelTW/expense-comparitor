package org.spargonaut.io;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.spargonaut.datamodels.ActivityType;
import org.spargonaut.datamodels.CreditCardActivity;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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
        List<String> chargeStrings = Arrays.asList(headerLine, chargeLine);

        mockFile = mock(File.class);
        mockCSVFileReader = mock(CSVFileReader.class);
        when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(chargeStrings);
    }

    @Test
    public void shouldParseAChargeLineIntoACreditCardActivity() {
        ChargeParser chargeParser = new ChargeParser(mockCSVFileReader);

        DateTime expectedTransactionDate = new DateTime(2016, 12, 10, 0, 0);
        DateTime expectedPostDate = new DateTime(2016, 12, 10, 0, 0);
        BigDecimal expectedAmount = new BigDecimal(-18.09);
        expectedAmount = expectedAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);

        List<CreditCardActivity> creditCardActivityList = chargeParser.parseFile(mockFile);
        CreditCardActivity actualCreditCardActivity = creditCardActivityList.get(0);

        assertThat(actualCreditCardActivity.getType(), is(ActivityType.SALE));
        assertThat(actualCreditCardActivity.getTransactionDate(), is(expectedTransactionDate));
        assertThat(actualCreditCardActivity.getPostDate(), is(expectedPostDate));
        assertThat(actualCreditCardActivity.getDescription(), is("UBER   *US DEC09 DFMHE"));
        assertThat(actualCreditCardActivity.getAmount(), is(expectedAmount));
    }

    @Test
    public void shouldIgnoreTheHeaderLineInTheCreditCardActivityFile() {
        ChargeParser chargeParser = new ChargeParser(mockCSVFileReader);
        List<CreditCardActivity> creditCardActivityList = chargeParser.parseFile(mockFile);
        assertThat(creditCardActivityList.size(), is(1));
    }
}
