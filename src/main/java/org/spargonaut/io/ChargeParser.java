package org.spargonaut.io;

import org.spargonaut.datamodels.CreditCardActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChargeParser {

    ChargeReader chargeReader;

    public ChargeParser(ChargeReader chargeReader) {
        this.chargeReader = chargeReader;
    }

    public List<CreditCardActivity> parseCharges(File chargeFile) {
        String chargeDelimiter = ",";

        List<String> chargeLines = chargeReader.readCreditCardFile(chargeFile);
        List<CreditCardActivity> creditCardActivities = new ArrayList<>();

        for (String chargeLine : chargeLines) {
            if (isHeaderLine(chargeLine)) { continue; }
            String[] chargeTokens = chargeLine.split(chargeDelimiter);
            CreditCardActivity creditCardActivity = new CreditCardActivity(
                    chargeTokens[0],
                    chargeTokens[1],
                    chargeTokens[2],
                    chargeTokens[3],
                    chargeTokens[4]);
            creditCardActivities.add(creditCardActivity);
        }

        return creditCardActivities;
    }

    private boolean isHeaderLine(String chargeLine) {
        return chargeLine.equals("Type,Trans Date,Post Date,Description,Amount");
    }
}
