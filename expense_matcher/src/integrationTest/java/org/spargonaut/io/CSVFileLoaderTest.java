package org.spargonaut.io;

import org.junit.Test;

import java.io.File;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CSVFileLoaderTest {

    @Test
    public void shouldGetTheListOfChargeFilesInADirectory() throws Exception {
        String testDirectoryName = "src/integrationTest/resources/test_charge_file_directory";

        CSVFileLoader csvFileLoader = new CSVFileLoader();
        Set<File> chargeFiles = csvFileLoader.getFileNamesIn(testDirectoryName);

        assertThat(chargeFiles.size(), is(2));
    }
}