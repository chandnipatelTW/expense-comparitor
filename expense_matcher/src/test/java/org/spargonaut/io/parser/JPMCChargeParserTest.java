package org.spargonaut.io.parser;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(HierarchicalContextRunner.class)
public class JPMCChargeParserTest {

    private CSVFileReader mockCSVFileReader;
    private File mockFile;
    private JPMCChargeParser jpmcChargeParser;

    @Before
    public void setUp() {
        mockFile = mock(File.class);
        mockCSVFileReader = mock(CSVFileReader.class);
        jpmcChargeParser = new JPMCChargeParser(mockCSVFileReader);
    }

    public class BasicJPMCChargeFile {
        @Before
        public void setUp() {
            String headerLine = "Type,Trans Date,Post Date,Description,Amount";
            String chargeLine = "Sale,12/10/2016,12/11/2016,UBER   *US DEC09 DFMHE,-18.09";
            String commentLine = "# this line is a comment because it starts with a hash";
            Set<String> chargeStrings = new HashSet<>(Arrays.asList(headerLine, chargeLine, commentLine));
            when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(chargeStrings);
        }

        @Test
        public void shouldParseAJPMCChargeLineIntoACreditCardActivity() {
            assertParsedFileContainsActivity(jpmcChargeParser.parseFile(mockFile), createUberCreditCardActivity());
        }

        @Test
        public void shouldIgnoreTheHeaderLineInTheJPMCCreditCardActivityFile() {
            assertParsedSetIsSize(jpmcChargeParser.parseFile(mockFile), 1);
        }

        @Test
        public void shouldIgnoreLinesThatStartWithHashSymbol() {
            assertParsedSetIsSize(jpmcChargeParser.parseFile(mockFile), 1);
        }
    }

    public class JPMCChargeFileWithDirtyChargeLines {
        @Before
        public void setUp() {
            String chargeLineWithACommaInTheDescription = "Sale,10/12/2015,10/13/2015,King, Schools, Inc.,-43.26";
            String blankLine = "";
            when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(new HashSet<>(Arrays.asList(chargeLineWithACommaInTheDescription, blankLine)));
        }

        @Test
        public void shouldRemoveCommasFromTheDescriptionField() {
            assertParsedFileContainsActivity(jpmcChargeParser.parseFile(mockFile), kingSchoolsCreditCardActivity());
        }

        @Test
        public void shouldIgnoreBlankLines() {
            Set<CreditCardActivity> creditCardActivitySet = jpmcChargeParser.parseFile(mockFile);
            assertParsedSetIsSize(creditCardActivitySet, 2);
            assertParsedFileContainsActivity(creditCardActivitySet, kingSchoolsCreditCardActivity());
        }

    }

    private void assertParsedFileContainsActivity(Set<CreditCardActivity> creditCardActivitySet, CreditCardActivity creditCardActivity) {
        assertThat(creditCardActivitySet.contains(creditCardActivity), is(true));
    }

    private void assertParsedSetIsSize(Set<CreditCardActivity> creditCardActivitySet, int size) {
        assertThat(creditCardActivitySet.size(), is(size));
    }

    private CreditCardActivity createUberCreditCardActivity() {
        return createExpectedCreditCardActivity(
                date(10, 12, 2016),
                date(11, 12, 2016),
                chargeAmount(-18.09),
                "UBER   *US DEC09 DFMHE");
    }

    private CreditCardActivity kingSchoolsCreditCardActivity() {
        return createExpectedCreditCardActivity(
                date(12, 10, 2015),
                date(13, 10, 2015),
                chargeAmount(-43.26),
                "King Schools Inc.");
    }

    private CreditCardActivity createExpectedCreditCardActivity(DateTime expectedTransactionDate, DateTime expectedPostDate, double expectedAmount, String expectedDescription) {
        return new CreditCardActivityBuilder()
                .setType(ActivityType.SALE)
                .setAmount(expectedAmount)
                .setDescription(expectedDescription)
                .setPostDate(expectedPostDate)
                .setTransactionDate(expectedTransactionDate)
                .build();
    }

    private double chargeAmount(double amount) {
        BigDecimal expectedAmount = new BigDecimal(amount);
        expectedAmount = expectedAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return expectedAmount.doubleValue();
    }

    private DateTime date(int dayOfMonth, int monthOfYear, int year) {
        return new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
    }
}
