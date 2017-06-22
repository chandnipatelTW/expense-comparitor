package org.spargonaut.io.parser;

import org.junit.Before;
import org.junit.Test;
import org.spargonaut.datamodels.CreditCardActivity;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ParsableDirectoryTest {

    private Parser mockParser;
    private ParsableDirectory parsableDirectory;

    @Before
    public void setUp() throws Exception {
        String directoryPath = "some/relative/path/directory";
        mockParser = mock(Parser.class);
        parsableDirectory = new ParsableDirectory<CreditCardActivity>(directoryPath, mockParser);
    }

    @Test
    public void shouldRetrieveTheDirectoryPath() {
        assertThat(parsableDirectory.getDirectory(), is("some/relative/path/directory"));
    }

    @Test
    public void shouldRetrieveTheParser() {
        assertThat(parsableDirectory.getParser(), is(mockParser));
    }

    @Test
    public void shouldRetrieveTheDirectoryPathOfFilesToIgnore() {
        assertThat(parsableDirectory.getDirectoryToIgnore(), is("some/relative/path/directory/manually_ignored"));
    }
}
