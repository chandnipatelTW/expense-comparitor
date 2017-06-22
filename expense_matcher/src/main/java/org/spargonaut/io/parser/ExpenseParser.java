package org.spargonaut.io.parser;

import org.joda.time.DateTime;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.io.CSVFileReader;

import java.io.File;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public class ExpenseParser implements Parser<Expense> {

    private final CSVFileReader csvFileReader;

    public ExpenseParser(CSVFileReader csvFileReader) {
        this.csvFileReader = csvFileReader;
    }

    public Set<Expense> parseFile(File expenseFile) {
        String expenseDelimiter = "\\|";
        return csvFileReader.readCsvFile(expenseFile).stream()
                .filter(this::isParsable)
                .map(expenseLine -> {
                    String pipedExpenseString = createPipedExpenseString(expenseLine);
                    return createExpense(expenseDelimiter, pipedExpenseString);
                })
                .collect(Collectors.toSet());
    }

    public String fileLocation() {
        return csvFileReader.getFileNameOfLastReadFile();
    }

    private Expense createExpense(String expenseDelimiter, String pipedExpenseString) {
        String[] expenseTokens = pipedExpenseString.split(expenseDelimiter, 11);
        boolean reimbursable = parseReimbursableness(expenseTokens[7]);
        DateTime dateTimeTimeStamp = parseTheTimeStamp(expenseTokens[0]);
        BigDecimal amount = parseDollarValue(expenseTokens[2]);
        BigDecimal originalAmount = parseDollarValue(expenseTokens[9]);

        return new Expense(
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
                cleanOffQuotes(expenseTokens[10]),
                fileLocation()
        );
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
        if (expenseToken.contains("\"")) {
            expenseToken = expenseToken.replace("\"", "");
        }

        if (expenseToken.contains(",")) {
            expenseToken = expenseToken.replace(",", "");
        }

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

        int hourOfDay = 0;
        int minutesOfHour = 0;

        return new DateTime(year, month, day, hourOfDay, minutesOfHour);
    }

    private boolean parseReimbursableness(String expenseToken) {
        String reimbursableString = cleanOffQuotes(expenseToken);
        boolean reimbursable;

        if ("yes".equalsIgnoreCase(reimbursableString)) {
            reimbursable = true;
        } else if ("no".equalsIgnoreCase(reimbursableString)) {
            reimbursable = false;
        } else {
            String errorMessage = "Unable to parse the string for Reimbursable" +
                    "\n" +
                    "Tried parsing the value ->" + reimbursableString + "<-";
            throw new IllegalArgumentException(errorMessage);
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

    private boolean isParsable(String expenseLine) {
        return !(isHeaderLine(expenseLine) || isCommentLine(expenseLine));
    }

    private boolean isCommentLine(String expenseLine) {
        return expenseLine.startsWith("#");
    }

    private boolean isHeaderLine(String expenseLine) {
        return "Timestamp,Merchant,Amount,MCC,Category,Tag,Comment,Reimbursable,\"Original Currency\",\"Original Amount\",Receipt".equals(expenseLine);
    }
}
