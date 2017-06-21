package org.spargonaut.io;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CSVFileLoaderTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    private String testDirectoryName;
    private CSVFileLoader csvFileLoader;

    @Before
    public void setUp() throws Exception {
        csvFileLoader = new CSVFileLoader();
    }

    @Test
    public void shouldGetTheListOfChargeFilesInADirectory() throws Exception {
        testDirectoryName = "src/integrationTest/resources/test_charge_file_directory";
        assertThat(csvFileLoader.getFileNamesIn(testDirectoryName).size(), is(2));
    }

    @Test
    public void shouldGiveAMessageIndicatingTheProblemFileWhenThrowingAnExceptionDuringFileLoading() throws Exception{
        String testDirectoryName = "src/bad_directory_name";
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Trouble loading the file: src/bad_directory_name");
        csvFileLoader.getFileNamesIn(testDirectoryName);
    }
}