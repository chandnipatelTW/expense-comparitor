package org.spargonaut.datamodels;

import org.joda.time.DateTime;
import org.junit.Test;
import org.spargonaut.datamodels.testbuilders.CreditCardActivityBuilder;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CreditCardActivityTest {

    @Test
    public void shouldIndicateTwoCreditCardActivitiesAreEqual_whenTheyHaveTheSameValues() {
        ActivityType activityType = ActivityType.SALE;
        int transactionYear = 2016;
        int transactionMonth = 12;
        int transactionDay = 29;
        int hourOfDay = 0;
        int minuteOfHour = 0;
        String description = "some sort of description";
        String fileLocation = "a_test_file_name.csv";
        double amount = 2.25;

        CreditCardActivity creditCardActivityOne = new CreditCardActivityBuilder()
                .setType(activityType)
                .setTransactionDate(new DateTime(transactionYear, transactionMonth, transactionDay, hourOfDay, minuteOfHour))
                .setDescription(description)
                .setAmount(amount)
                .setFileLocation(fileLocation)
                .build();


        CreditCardActivity creditCardActivityTwo = new CreditCardActivityBuilder()
                .setType(activityType)
                .setTransactionDate(new DateTime(transactionYear, transactionMonth, transactionDay, hourOfDay, minuteOfHour))
                .setDescription(description)
                .setAmount(amount)
                .setFileLocation(fileLocation)
                .build();

        assertThat(creditCardActivityOne.equals(creditCardActivityTwo), is(true));
    }

}