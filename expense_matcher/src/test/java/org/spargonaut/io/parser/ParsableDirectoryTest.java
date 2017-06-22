package org.spargonaut.io.parser;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ParsableDirectoryTest {

    private String directoryPath;
    private Parser mockParser;
    private ParsableDirectory parsableDirectory;

    @Before
    public void setUp() throws Exception {
        directoryPath = "some/relative/path/directory";
        mockParser = mock(Parser.class);
        parsableDirectory = new ParsableDirectory(directoryPath, mockParser);
    }

    @Test
    public void shouldRetrieveTheDirectoryPath() {
        assertThat(parsableDirectory.getDirectory(), is("some/relative/path/directory"));
    }

    @Test
    public void shouldREtrieveTheParser() {
        assertThat(parsableDirectory.getParser(), is(mockParser));
    }
}
