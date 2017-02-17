package org.spargonaut.io.printer;

import org.spargonaut.datamodels.ActivityType;
import org.spargonaut.datamodels.CreditCardActivity;

import java.util.ArrayList;
import java.util.List;

public class ChargePrinter {

    // FIXME - separate out the payments from the charges

    public static void printChargesAsHumanReadable(List<CreditCardActivity> activities) {
        List<CreditCardActivity> payments = new ArrayList<>();
        List<CreditCardActivity> charges = new ArrayList<>();

        separateChargesAndPayments(activities, payments, charges);

        String chargesString = "charges";
        printActivities(charges, chargesString);

        System.out.println();

        String paymentString = "payments";
        printActivities(payments, paymentString);
    }

    private static void separateChargesAndPayments(List<CreditCardActivity> activities, List<CreditCardActivity> payments, List<CreditCardActivity> charges) {
        for (CreditCardActivity activity : activities) {
            if (activity.getType() == ActivityType.PAYMENT) {
                payments.add(activity);
            } else {
                charges.add(activity);
            }
        }
    }

    private static void printActivities(List<CreditCardActivity> charges, String chargesString) {
        System.out.format(chargesString + " (%d) -----------------------------------------------------------------------\n", charges.size());
        for (CreditCardActivity charge : charges) {
            printActivity(charge);
        }
    }

    private static void printActivity(CreditCardActivity activity) {
        System.out.format("%-10s %-15s %-15s %-30s %10s\n",
                activity.getType().getValue(),
                activity.getTransactionDate().toLocalDate(),
                activity.getPostDate().toLocalDate(),
                activity.getDescription(),
                activity.getAmount());
    }
}
