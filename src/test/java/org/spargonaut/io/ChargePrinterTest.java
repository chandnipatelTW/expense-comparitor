package org.spargonaut.io;

import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.spargonaut.datamodels.CreditCardActivity;
import org.springframework.boot.test.OutputCapture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ChargePrinterTest {

    @Rule
    public OutputCapture capture = new OutputCapture();

    @Test
    public void shouldPrintTheFormattedCharges() {
        BigDecimal saleOneAmount = createBigDecimalFrom(-27.67);
        BigDecimal saleTwoAmount = createBigDecimalFrom(-12.00);
        BigDecimal paymentOneAmount = createBigDecimalFrom(3004.37);

        CreditCardActivity saleOne = new CreditCardActivity("Sale", new DateTime(2016, 11, 28, 0, 0), new DateTime(2016, 11, 29, 0, 0), "UBER   *US NOV27 CQ6IT", saleOneAmount);
        CreditCardActivity saleTwo = new CreditCardActivity("Sale", new DateTime(2016, 11, 28, 0, 0), new DateTime(2016, 11, 30, 0, 0), "SALATA - LAS COLINAS", saleTwoAmount);
        CreditCardActivity paymentOne = new CreditCardActivity("Payment", new DateTime(2016, 11, 26, 0, 0), new DateTime(2016, 11, 27, 0, 0), "Payment Thank You - Web", paymentOneAmount);

        List<CreditCardActivity> activities = Arrays.asList(saleOne, saleTwo, paymentOne);
        ChargePrinter.printChargesAsHumanReadable(activities);

        String expectedOutput = "Sale       2016-11-28T00:00:00.000-06:00 2016-11-29T00:00:00.000-06:00 UBER   *US NOV27 CQ6IT         -27.67    \n" +
                                "Sale       2016-11-28T00:00:00.000-06:00 2016-11-30T00:00:00.000-06:00 SALATA - LAS COLINAS           -12.00    \n" +
                                "Payment    2016-11-26T00:00:00.000-06:00 2016-11-27T00:00:00.000-06:00 Payment Thank You - Web        3004.37   \n";

        assertThat(capture.toString(), is(expectedOutput));
    }

    private BigDecimal createBigDecimalFrom(double value) {
        BigDecimal saleOneAmount = new BigDecimal(value);
        saleOneAmount = saleOneAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return saleOneAmount;
    }
}
