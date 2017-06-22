package org.spargonaut.io.parser;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.io.CSVFileReader;

import java.io.File;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(HierarchicalContextRunner.class)
public class BankOfAmericaChargeParserTest extends BaseChargeParserTest {

    private final File mockFile = mock(File.class);
    private final CSVFileReader mockCSVFileReader = mock(CSVFileReader.class);
    private String fileLocation;

    @Before
    public void setUp() {
        fileLocation = "boa_statement.csv";
        when(mockCSVFileReader.getFileNameOfLastReadFile()).thenReturn(fileLocation);
    }

    public class BasicBankOfAmericaChargeFile {

        private BankOfAmericaChargeParser bankOfAmericaChargeParser;

        @Before
        public void setUp() {
            String headerLine = "Posted Date,Reference Number,Payee,Address,Amount";
            String chargeLine = "02/07/2017,24492157037637005325047,\"DOORDASH-MCALISTERS DE 650-681-9470 CA\",\"650-681-9470  CA \",-75.99";
            String commentLine = "# this line is a comment because it starts with a hash";
            Set<String> chargeStrings = aHashSetOf(headerLine, chargeLine, commentLine);
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
            when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(aHashSetOf(blankLine));

            BankOfAmericaChargeParser bankOfAmericaChargeParser = new BankOfAmericaChargeParser(mockCSVFileReader);

            Set<CreditCardActivity> creditCardActivitySet = bankOfAmericaChargeParser.parseFile(mockFile);

            assertThat(creditCardActivitySet.isEmpty(), is(true));
        }
    }

    private CreditCardActivity mccalistersCreditCardActivity() {
        return createExpectedCreditCardActivity(
                date( 23, 12, 1999),
                date(7, 2, 2017),
                chargeAmount(-75.99),
                "DOORDASH-MCALISTERS DE 650-681-9470 CA",
                fileLocation);
    }
}
