package org.spargonaut.io;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class CSVFileReaderTest {

    private CSVFileReader csvFileReader;
    private File creditCardFile;

    @Before
    public void setUp() {
        csvFileReader = new CSVFileReader();
        String testFileName = "src/integrationTest/resources/test_charges.csv";
        creditCardFile = new File(testFileName);
    }

    @Test
    public void shouldReadTheCreditCardFile() {
        Set<String> charges = csvFileReader.readCsvFile(creditCardFile);
        assertThat(charges.size(), is(6));
    }

    @Test
    public void shouldRetrieveTheNameOfTheFileBeingRead() {
        csvFileReader.readCsvFile(creditCardFile);
        assertThat(csvFileReader.getFileNameOfLastReadFile(), is("test_charges.csv"));
    }
}
