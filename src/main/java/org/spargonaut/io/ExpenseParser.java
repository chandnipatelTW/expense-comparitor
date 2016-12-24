package org.spargonaut.io;

import org.joda.time.DateTime;
import org.spargonaut.datamodels.Expense;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExpenseParser implements Parser {

    private final CSVFileReader csvFileReader;

    public ExpenseParser(CSVFileReader csvFileReader) {
        this.csvFileReader = csvFileReader;
    }

    public List<Expense> parseFile(File expenseFile) {
        String expenseDelimiter = "\\|";

        List<String> expenseLines = csvFileReader.readCsvFile(expenseFile);
        List<Expense> expenses = new ArrayList<>();

        for (String expenseLine : expenseLines) {
            if (isHeaderLine(expenseLine)) { continue; }
            String pipedExpenseString = createPipedExpenseString(expenseLine);
            String[] expenseTokens = pipedExpenseString.split(expenseDelimiter);


            boolean reimbursable = parseReimbursableness(expenseTokens[7]);
            DateTime dateTimeTimeStamp = parseTheTimeStamp(expenseTokens[0]);

            BigDecimal amount = parseDollarValue(expenseTokens[2]);
            BigDecimal originalAmount = parseDollarValue(expenseTokens[9]);

            Expense expense = new Expense(
                    dateTimeTimeStamp,
                    cleanOffQuotes(expenseTokens[1]),
                    amount,
                    cleanOffQuotes(expenseTokens[3]),
                    cleanOffQuotes(expenseTokens[4]),
                    cleanOffQuotes(expenseTokens[5]),
                    cleanOffQuotes(expenseTokens[6]),
                    reimbursable,
                    cleanOffQuotes(expenseTokens[8]),
                    originalAmount,
                    cleanOffQuotes(expenseTokens[10])
            );
            expenses.add(expense);

        }

        return expenses;
    }

    private String createPipedExpenseString(String expenseLine) {
        int quoteCount = 0;
        for (int i = 0; i < expenseLine.length(); i++) {

            if ('"' == expenseLine.charAt(i)) {
                quoteCount++;
            }

            if (',' == expenseLine.charAt(i) && quoteCount % 2 == 0) {
                expenseLine = expenseLine.substring(0, i) + "|" + expenseLine.substring(i + 1, expenseLine.length());
            }
        }
        return expenseLine;
    }

    private BigDecimal parseDollarValue(String expenseToken) {
        BigDecimal amount = new BigDecimal(Float.parseFloat(expenseToken));
        amount = amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return amount;
    }

    private DateTime parseTheTimeStamp(String expenseToken) {
        String cleanedTimeStamp = cleanOffQuotes(expenseToken);
        String[] dateTimeTokens = cleanedTimeStamp.split(" ");
        String dateChunk = dateTimeTokens[0];
        String[] dateTokens = dateChunk.split("-");
        int year = Integer.parseInt(dateTokens[0]);
        int month = Integer.parseInt(dateTokens[1]);
        int day = Integer.parseInt(dateTokens[2]);

        String timeChunk = dateTimeTokens[1];
        String[] timeTokens = timeChunk.split(":");
        int hourOfDay = Integer.parseInt(timeTokens[0]);
        int minutesOfHour = Integer.parseInt(timeTokens[1]);

        return new DateTime(year, month, day, hourOfDay, minutesOfHour);
    }

    private boolean parseReimbursableness(String expenseToken) {
        String reimbursableString = cleanOffQuotes(expenseToken);
        boolean reimbursable = false;

        if ("yes".equalsIgnoreCase(reimbursableString)) {
            reimbursable = true;
        } else if ("no".equalsIgnoreCase(reimbursableString)) {
            reimbursable = false;
        } else {
            String errorMessage = "Unable to parse the string for Reimbursable" +
                    "\n" +
                    "Tried parsing the value ->" + reimbursableString + "<-";
            throw new IllegalArgumentException(errorMessage.toString());
        }
        return reimbursable;
    }

    private String cleanOffQuotes(String token) {
        String cleanedString = token;
        if (token.startsWith("\"") && token.endsWith("\"")) {
            cleanedString = token.substring(1, token.lastIndexOf("\""));
        }
        return cleanedString;
    }

    private boolean isHeaderLine(String expenseLine) {
        return "Timestamp,Merchant,Amount,MCC,Category,Tag,Comment,Reimbursable,\"Original Currency\",\"Original Amount\",Receipt".equals(expenseLine);
    }
}
