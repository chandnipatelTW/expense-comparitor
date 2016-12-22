package org.spargonaut;

import org.junit.Test;
import org.junit.Rule;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import org.springframework.boot.test.OutputCapture;

public class MyJavaApplicationTest {

    @Rule
    public OutputCapture capture = new OutputCapture();

    @Test
    public void shouldpass() {
        MyJavaApplication.main(null);

        assertThat(capture.toString(), is("Hello World!\n"));
    }
}
