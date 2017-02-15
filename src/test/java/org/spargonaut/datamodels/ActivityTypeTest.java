package org.spargonaut.datamodels;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ActivityTypeTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldParseTheActivityTypeFromAStringForValidActivityTypes() throws Exception {
        assertThat(ActivityType.fromString("sale"), is(ActivityType.SALE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnException_whenTheActivityCannotBeParsedIntoAKnownType() {
        String typeStringOne = "foo";
        ActivityType.fromString(typeStringOne);
    }

    @Test
    public void shouldThrownAnException_whenTheActivityStringIsNull() {
        expectedException.expect(IllegalArgumentException.class);
        ActivityType.fromString(null);
    }

    @Test
    public void shouldIndicateTheInvalidActivityType_whenThrowingInvalidArgumentException() {
        String invalidActivityType = "Foo";
        expectedException.expectMessage(invalidActivityType);
        ActivityType.fromString(invalidActivityType);
    }
}