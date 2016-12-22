package org.spargonaut.io;

import org.spargonaut.datamodels.CreditCardActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ChargeReader {

    public List<String> readCreditCardFile(File creditCardFile) {
        List<String> chargeLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(creditCardFile))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                chargeLines.add(currentLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return chargeLines;
    }
}
