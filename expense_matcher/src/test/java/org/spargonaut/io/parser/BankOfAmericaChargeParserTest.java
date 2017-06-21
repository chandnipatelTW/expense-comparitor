package org.spargonaut.io.parser;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.hamcrest.MatcherAssert;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(HierarchicalContextRunner.class)
public class BankOfAmericaChargeParserTest {

    private final File mockFile = mock(File.class);
    private final CSVFileReader mockCSVFileReader = mock(CSVFileReader.class);

    public class BasicBankOfAmericaChargeFile {

        private BankOfAmericaChargeParser bankOfAmericaChargeParser;

        @Before
        public void setUp() {
            String headerLine = "Posted Date,Reference Number,Payee,Address,Amount";
            String chargeLine = "02/07/2017,24492157037637005325047,\"DOORDASH-MCALISTERS DE 650-681-9470 CA\",\"650-681-9470  CA \",-75.99";
            String commentLine = "# this line is a comment because it starts with a hash";
            Set<String> chargeStrings = new HashSet<>(Arrays.asList(headerLine, chargeLine, commentLine));
            when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(chargeStrings);
            bankOfAmericaChargeParser = new BankOfAmericaChargeParser(mockCSVFileReader);
        }

        @Test
        public void shouldParseBankOfAmericaChargeLineIntoACreditCardActivity() {
            assertParsedFileContainsActivity(bankOfAmericaChargeParser.parseFile(mockFile), mccalistersCreditCardActivity());
        }

        @Test
        public void shouldIgnoreTheHeaderLine() {
            assertParsedSetIsSize(bankOfAmericaChargeParser.parseFile(mockFile), 1);
        }

        @Test
        public void shouldIgnoreLinesThatStartWithHashSymbol() {
            assertParsedSetIsSize(bankOfAmericaChargeParser.parseFile(mockFile), 1);
        }
    }

    public class ADirtyBankOfAmericaChargeFile {
        @Test
        public void shouldIgnoreBlankLines() {
            String blankLine = "";
            Set<String> chargeStrings = new HashSet<>(Arrays.asList(blankLine));
            when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(chargeStrings);

            BankOfAmericaChargeParser bankOfAmericaChargeParser = new BankOfAmericaChargeParser(mockCSVFileReader);

            Set<CreditCardActivity> creditCardActivitySet = bankOfAmericaChargeParser.parseFile(mockFile);

            assertThat(creditCardActivitySet.isEmpty(), is(true));
        }
    }

    private void assertParsedFileContainsActivity(Set<CreditCardActivity> creditCardActivitySet, CreditCardActivity expectedCreditCardActivity) {
        assertThat(creditCardActivitySet.contains(expectedCreditCardActivity), is(true));
    }

    private void assertParsedSetIsSize(Set<CreditCardActivity> creditCardActivityList, int size) {
        MatcherAssert.assertThat(creditCardActivityList.size(), is(size));
    }

    private CreditCardActivity mccalistersCreditCardActivity() {
        return createExpectedCreditCardActivity(
                date(1999, 12, 23),
                date(2017, 02, 07),
                chargeAmount(-75.99),
                "DOORDASH-MCALISTERS DE 650-681-9470 CA");
    }

    private CreditCardActivity createExpectedCreditCardActivity(DateTime transactionDate, DateTime postDate, BigDecimal amount, String description) {
        return new CreditCardActivityBuilder()
                .setType(ActivityType.SALE)
                .setAmount(amount.doubleValue())
                .setDescription(description)
                .setPostDate(postDate)
                .setTransactionDate(transactionDate)
                .build();
    }

    private BigDecimal chargeAmount(double chargeAmount) {
        BigDecimal expectedAmount = new BigDecimal(chargeAmount);
        expectedAmount = expectedAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return expectedAmount;
    }

    private DateTime date(int year, int monthOfYear, int dayOfMonth) {
        return new DateTime(year, monthOfYear, dayOfMonth, 0, 0, 0);
    }
}
