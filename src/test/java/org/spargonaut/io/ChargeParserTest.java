package org.spargonaut.io;

import org.junit.Before;
import org.junit.Test;
import org.spargonaut.datamodels.ActivityType;
import org.spargonaut.datamodels.CreditCardActivity;

import java.io.File;
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
        when(mockCSVFileReader.readCreditCardFile(mockFile)).thenReturn(chargeStrings);
    }

    @Test
    public void shouldParseAChargeLineIntoACreditCardActivity() {
        ChargeParser chargeParser = new ChargeParser(mockCSVFileReader);
        List<CreditCardActivity> creditCardActivityList = chargeParser.parseCharges(mockFile);
        CreditCardActivity actualCreditCardActivity = creditCardActivityList.get(0);

        assertThat(actualCreditCardActivity.getType(), is(ActivityType.SALE));
        assertThat(actualCreditCardActivity.getTransactionDate(), is("12/10/2016"));
        assertThat(actualCreditCardActivity.getPostDate(), is("12/11/2016"));
        assertThat(actualCreditCardActivity.getDescription(), is("UBER   *US DEC09 DFMHE"));
        assertThat(actualCreditCardActivity.getAmount(), is("-18.09"));
    }

    @Test
    public void shouldIgnoreTheHeaderLineInTheCreditCardActivityFile() {
        ChargeParser chargeParser = new ChargeParser(mockCSVFileReader);
        List<CreditCardActivity> creditCardActivityList = chargeParser.parseCharges(mockFile);
        assertThat(creditCardActivityList.size(), is(1));
    }
}
