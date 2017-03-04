package org.spargonaut.io.printer;

import org.spargonaut.datamodels.ActivityType;
import org.spargonaut.datamodels.CreditCardActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChargePrinter {

    // FIXME - separate out the Fees from the charges

    public static void printChargesAsHumanReadable(Set<CreditCardActivity> activities) {
        Map<String, Set<CreditCardActivity>> filteredActivities = new HashMap<>();
        filteredActivities.put("payments",
                activities.stream()
                .filter(activity1 -> activity1.getType() == ActivityType.PAYMENT)
                .collect(Collectors.toSet()));

        filteredActivities.put("charges",
                activities.stream()
                .filter(activity -> {
                    ActivityType activityType1 = activity.getType();
                    return (activityType1 == ActivityType.SALE || activityType1 == ActivityType.FEE);
                })
                .collect(Collectors.toSet()));

        filteredActivities.forEach( (activityType, activitySet) -> {
            printHeaderString(activityType, activitySet.size());
            printActivities(activitySet);
            System.out.println();
        });
    }

    private static void printActivities(Set<CreditCardActivity> charges) {
        charges.forEach(charge -> {
            System.out.format("%-10s %-15s %-15s %-40s %10s\n",
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
