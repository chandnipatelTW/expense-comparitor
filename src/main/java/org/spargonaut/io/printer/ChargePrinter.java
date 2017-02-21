package org.spargonaut.io.printer;

import org.spargonaut.datamodels.ActivityType;
import org.spargonaut.datamodels.CreditCardActivity;

import java.util.Set;
import java.util.stream.Collectors;

public class ChargePrinter {

    // FIXME - separate out the Fees from the charges

    public static void printChargesAsHumanReadable(Set<CreditCardActivity> activities) {
        Set<CreditCardActivity> payments = activities.stream()
                .filter(activity -> activity.getType() == ActivityType.PAYMENT)
                .collect(Collectors.toSet());

        Set<CreditCardActivity> charges = activities.stream()
                .filter(activity -> {
                    ActivityType activityType = activity.getType();
                    return (activityType == ActivityType.SALE || activityType == ActivityType.FEE);
                })
                .collect(Collectors.toSet());

        printHeaderString("charges", charges.size());
        printActivities(charges);

        System.out.println();

        printHeaderString("payments", payments.size());
        printActivities(payments);
    }

    private static void printActivities(Set<CreditCardActivity> charges) {
        charges.forEach(charge -> {
            System.out.format("%-10s %-15s %-15s %-30s %10s\n",
                    charge.getType().getValue(),
                    charge.getTransactionDate().toLocalDate(),
                    charge.getPostDate().toLocalDate(),
                    charge.getDescription(),
                    charge.getAmount());
        });
    }

    private static void printHeaderString(String headerType, int size) {
        System.out.format(headerType + " (%d) -----------------------------------------------------------------------\n", size);
    }
}
