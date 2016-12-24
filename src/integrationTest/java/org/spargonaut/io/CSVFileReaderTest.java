package org.spargonaut.io;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class CSVFileReaderTest {

    @Test
    public void shouldReadTheCreditCardFile() {
        String testFileName = "src/integrationTest/resources/test_charges.csv";
        File creditCardFile = new File(testFileName);

        CSVFileReader csvFileReader = new CSVFileReader();
        List<String> charges = csvFileReader.readCsvFile(creditCardFile);

        assertThat(charges.size(), is(6));
    }
}
