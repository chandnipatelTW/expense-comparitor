package org.spargonaut.io.parser;

import org.joda.time.DateTime;
import org.junit.Test;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.io.CSVFileReader;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExpenseParserTest {

    @Test
    public void shouldParseAnExpenseLineIntoAnExpense() {
        String headerString = "Timestamp,Merchant,Amount,MCC,Category,Tag,Comment,Reimbursable,\"Original Currency\",\"Original Amount\",Receipt";
        String expenseString = "\"2016-12-12 00:00:00\",Uber,18.68,0,\"Local Transportation\",\"Neiman Marcus Group Inc:NMG P4G Design/Deliver Phase:NMG P4G Design/Deliver Phase:Delivery Assurance\",\"Uber DFW to office\",yes,USD,18.68,https://salesforce.expensify.com/verifyReceipt?action=verifyreceipt&transactionID=7871304959002483&amount=-1868&created=2016-12-12";

        Set<String> expenseStrings = new HashSet<>(Arrays.asList(headerString, expenseString));

        File mockFile = mock(File.class);
        CSVFileReader mockCSVFileReader = mock(CSVFileReader.class);
        when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(expenseStrings);

        DateTime expectedTimeStamp = new DateTime(2016, 12, 12, 0, 0);
        double expectedExpenseOneAmount = 18.68;
        BigDecimal expectedAmount = createBigDecimalFrom(expectedExpenseOneAmount);
        BigDecimal expectedOriginalAmount = createBigDecimalFrom(expectedExpenseOneAmount);

        ExpenseParser expenseParser = new ExpenseParser(mockCSVFileReader);
        Set<Expense> expenses = expenseParser.parseFile(mockFile);

        Expense actualExpense = expenses.iterator().next();

        assertThat(actualExpense.getTimestamp(), is(expectedTimeStamp));
        assertThat(actualExpense.getMerchant() , is("Uber"));
        assertThat(actualExpense.getAmount(), is(expectedAmount));
        assertThat(actualExpense.getMcc() , is("0"));
        assertThat(actualExpense.getCategory() , is("Local Transportation"));
        assertThat(actualExpense.getTag() , is("Neiman Marcus Group Inc:NMG P4G Design/Deliver Phase:NMG P4G Design/Deliver Phase:Delivery Assurance"));
        assertThat(actualExpense.getComment() , is("Uber DFW to office"));
        assertThat(actualExpense.isReimbursable() , is(true));
        assertThat(actualExpense.getOriginalCurrency() , is("USD"));
        assertThat(actualExpense.getOriginalAmount() , is(expectedOriginalAmount));
        assertThat(actualExpense.getReceiptURL() , is("https://salesforce.expensify.com/verifyReceipt?action=verifyreceipt&transactionID=7871304959002483&amount=-1868&created=2016-12-12"));
    }

    @Test
    public void shouldIgnoreTheHeaderLineInTheExpenseFile() {
        String headerString = "Timestamp,Merchant,Amount,MCC,Category,Tag,Comment,Reimbursable,\"Original Currency\",\"Original Amount\",Receipt";
        String expenseString = "\"2016-12-12 00:00:00\",Uber,18.68,0,\"Local Transportation\",\"Neiman Marcus Group Inc:NMG P4G Design/Deliver Phase:NMG P4G Design/Deliver Phase:Delivery Assurance\",\"Uber DFW to office\",yes,USD,18.68,https://salesforce.expensify.com/verifyReceipt?action=verifyreceipt&transactionID=7871304959002483&amount=-1868&created=2016-12-12";

        Set<String> expenseStrings = new HashSet<>(Arrays.asList(headerString, expenseString));

        File mockFile = mock(File.class);
        CSVFileReader mockCSVFileReader = mock(CSVFileReader.class);
        when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(expenseStrings);

        ExpenseParser expenseParser = new ExpenseParser(mockCSVFileReader);
        Set<Expense> actualExpenses = expenseParser.parseFile(mockFile);
        
        assertThat(actualExpenses.size(), is(1));
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenReimbursableIsNotParsable() {
        String headerString = "Timestamp,Merchant,Amount,MCC,Category,Tag,Comment,Reimbursable,\"Original Currency\",\"Original Amount\",Receipt";
        String expenseString = "\"2016-12-12 00:00:00\",Uber,18.68,0,\"Local Transportation\",\"Neiman Marcus Group Inc:NMG P4G Design/Deliver Phase:NMG P4G Design/Deliver Phase:Delivery Assurance\",\"Uber DFW to office\",foo,USD,18.68,https://salesforce.expensify.com/verifyReceipt?action=verifyreceipt&transactionID=7871304959002483&amount=-1868&created=2016-12-12";

        Set<String> expenseStrings = new HashSet<>(Arrays.asList(headerString, expenseString));

        File mockFile = mock(File.class);
        CSVFileReader mockCSVFileReader = mock(CSVFileReader.class);
        when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(expenseStrings);

        ExpenseParser expenseParser = new ExpenseParser(mockCSVFileReader);
        expenseParser.parseFile(mockFile);
    }

    @Test
    public void shouldKeepCommentsTogetherWhenTheyContainCommas() {
        String expenseString = "\"2016-12-09 00:00:00\",\"Taco Diner\",97.19,0,\"Business Meals\",\"Neiman Marcus Group Inc:NMG P4G Design/Deliver Phase:NMG P4G Design/Deliver Phase:Delivery Assurance\",\"Lunch for myself, Max, Blake, Charith, Timothy, Bryan\",yes,USD,97.19,https://salesforce.expensify.com/verifyReceipt?action=verifyreceipt&transactionID=7871304959002493&amount=-9719&created=2016-12-09";

        Set<String> expenseStrings = new HashSet<>(Arrays.asList(expenseString));
        File mockFile = mock(File.class);
        CSVFileReader mockCSVFileReader = mock(CSVFileReader.class);
        when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(expenseStrings);

        ExpenseParser expenseParser = new ExpenseParser(mockCSVFileReader);
        Set<Expense> actualExpenses = expenseParser.parseFile(mockFile);
        Expense actualExpense = actualExpenses.iterator().next();

        assertThat(actualExpense.getComment() , is("Lunch for myself, Max, Blake, Charith, Timothy, Bryan"));
    }

    private BigDecimal createBigDecimalFrom(double value) {
        BigDecimal expectedAmount = new BigDecimal(value);
        expectedAmount = expectedAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return expectedAmount;
    }
}
