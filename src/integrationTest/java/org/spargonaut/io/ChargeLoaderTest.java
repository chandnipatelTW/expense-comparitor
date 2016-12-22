package org.spargonaut.io;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ChargeLoaderTest {

    @Test
    public void shouldGetTheListOfChargeFilesInADirectory() throws Exception {
        String testDirectoryName = "src/integrationTest/resources/test_charge_file_directory";

        ChargeLoader chargeLoader = new ChargeLoader();
        List<File> chargeFiles = chargeLoader.getFileNamesIn(testDirectoryName);

        assertThat(chargeFiles.size(), is(2));
    }
}