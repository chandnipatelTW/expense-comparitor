package org.spargonaut.io;

import org.spargonaut.datamodels.CreditCardActivity;

import java.util.List;

public class ChargePrinter {
    public static void printChargesAsHumanReadable(List<CreditCardActivity> activities) {
        for (CreditCardActivity activity : activities) {
            System.out.format("%-10s %-15s %-15s %-30s %-10s\n",
                    activity.getType(),
                    activity.getTransactionDate(),
                    activity.getPostDate(),
                    activity.getDescription(),
                    activity.getAmount());
        }
    }
}
