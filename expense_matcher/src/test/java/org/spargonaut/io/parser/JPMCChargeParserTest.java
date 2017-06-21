package org.spargonaut.io.parser;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.io.CSVFileReader;

import java.io.File;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(HierarchicalContextRunner.class)
public class JPMCChargeParserTest extends BaseChargeParserTest {

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
            when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(aHashSetOf(headerLine, chargeLine, commentLine));
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
            when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(aHashSetOf(chargeLineWithACommaInTheDescription, blankLine));
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
}
