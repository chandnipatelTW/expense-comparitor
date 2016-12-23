package org.spargonaut.io;

import org.junit.Rule;
import org.junit.Test;
import org.spargonaut.datamodels.CreditCardActivity;
import org.springframework.boot.test.OutputCapture;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ChargePrinterTest {

    @Rule
    public OutputCapture capture = new OutputCapture();

    @Test
    public void shouldPrintTheFormattedCharges() {
        CreditCardActivity saleOne = new CreditCardActivity("Sale", "11/28/2016", "11/29/2016", "UBER   *US NOV27 CQ6IT", "-27.67");
        CreditCardActivity saleTwo = new CreditCardActivity("Sale", "11/28/2016", "11/30/2016", "SALATA - LAS COLINAS", "-12.00");
        CreditCardActivity paymentOne = new CreditCardActivity("Payment", "11/26/2016", "11/27/2016", "Payment Thank You - Web", "3004.37");

        List<CreditCardActivity> activities = Arrays.asList(saleOne, saleTwo, paymentOne);
        ChargePrinter.printChargesAsHumanReadable(activities);

        String expectedOutput = "Sale       11/28/2016      11/29/2016      UBER   *US NOV27 CQ6IT         -27.67    \n" +
                                "Sale       11/28/2016      11/30/2016      SALATA - LAS COLINAS           -12.00    \n" +
                                "Payment    11/26/2016      11/27/2016      Payment Thank You - Web        3004.37   \n";

        assertThat(capture.toString(), is(expectedOutput));
    }
}
