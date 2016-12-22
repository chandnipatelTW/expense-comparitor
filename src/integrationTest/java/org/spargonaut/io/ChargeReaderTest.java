package org.spargonaut.io;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ChargeReaderTest {

    @Test
    public void shouldReadTheCreditCardFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        File creditCardFile = new File(classLoader.getResource("test_charges.csv").getFile());

        ChargeReader chargeReader = new ChargeReader();
        List<String> charges = chargeReader.readCreditCardFile(creditCardFile);

        assertThat(charges.size(), is(6));
    }
}
