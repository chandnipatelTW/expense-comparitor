package org.spargonaut.io;

import org.junit.Test;
import org.spargonaut.datamodels.CreditCardActivity;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChargeParserTest {
    @Test
    public void shouldParseAChargeLineIntoACreditCardActivity() {
        String chargeLine = "Sale,12/10/2016,12/11/2016,UBER   *US DEC09 DFMHE,-18.09";
        List<String> chargeStrings = Arrays.asList(chargeLine);

        File mockFile = mock(File.class);
        ChargeReader mockChargeReader = mock(ChargeReader.class);
        when(mockChargeReader.readCreditCardFile(mockFile)).thenReturn(chargeStrings);

        ChargeParser chargeParser = new ChargeParser(mockChargeReader);

        List<CreditCardActivity> creditCardActivityList = chargeParser.parseCharges(mockFile);

        CreditCardActivity actualCreditCardActivity = creditCardActivityList.get(0);

        assertThat(actualCreditCardActivity.getType(), is("Sale"));
        assertThat(actualCreditCardActivity.getTransactionDate(), is("12/10/2016"));
        assertThat(actualCreditCardActivity.getPostDate(), is("12/11/2016"));
        assertThat(actualCreditCardActivity.getDescription(), is("UBER   *US DEC09 DFMHE"));
        assertThat(actualCreditCardActivity.getAmount(), is("-18.09"));
    }
}
