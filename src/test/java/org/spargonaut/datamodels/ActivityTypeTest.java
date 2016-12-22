package org.spargonaut.datamodels;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ActivityTypeTest {

    @Test
    public void shouldParseTheActivityTypeFromAString() throws Exception {
        String typeStringOne = "sale";
        ActivityType activityType = ActivityType.fromString(typeStringOne);
        assertThat(activityType, is(ActivityType.SALE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnException_whenTheActivityCannotBeParsedIntoAKnownType() {
        String typeStringOne = "foo";
        ActivityType.fromString(typeStringOne);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrownAnException_whenTheActivityStringIsNull() {
        ActivityType.fromString(null);
    }
}