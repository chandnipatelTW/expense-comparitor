package org.spargonaut.io.parser;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.testbuilders.ExpenseBuilder;
import org.spargonaut.io.CSVFileReader;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExpenseParserTest {

    private final String headerString = "Timestamp,Merchant,Amount,MCC,Category,Tag,Comment,Reimbursable,\"Original Currency\",\"Original Amount\",Receipt";
    private final String expenseString = "\"2016-12-12 00:00:00\",Uber,18.68,0,\"Local Transportation\",\"Neiman Marcus Group Inc:NMG P4G Design/Deliver Phase:NMG P4G Design/Deliver Phase:Delivery Assurance\",\"Uber DFW to office\",yes,USD,18.68,https://salesforce.expensify.com/verifyReceipt?action=verifyreceipt&transactionID=7871304959002483&amount=-1868&created=2016-12-12";
    private final String commentString = "# this is a comment line";
    private final String expenseStringWithExtraCommas = "\"2016-12-09 00:00:00\",\"Taco Diner\",97.19,0,\"Business Meals\",\"Neiman Marcus Group Inc:NMG P4G Design/Deliver Phase:NMG P4G Design/Deliver Phase:Delivery Assurance\",\"Lunch for myself, Max, Blake, Charith, Timothy, Bryan\",yes,USD,97.19,https://salesforce.expensify.com/verifyReceipt?action=verifyreceipt&transactionID=7871304959002493&amount=-9719&created=2016-12-09";
    private File mockFile;
    private CSVFileReader mockCSVFileReader;
    private Set<String> expenseStrings;

    @Before
    public void setUp() {
        mockFile = mock(File.class);
        mockCSVFileReader = mock(CSVFileReader.class);
        expenseStrings = new HashSet<>(Arrays.asList(headerString, expenseString, commentString, expenseStringWithExtraCommas));
    }

    @Test
    public void shouldParseAnExpenseLineIntoAnExpense() {
        when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(expenseStrings);

        DateTime expectedTimeStamp = new DateTime(2016, 12, 12, 0, 0);
        double expectedAmount = 18.68;

        Expense expectedExpense = new ExpenseBuilder()
                .setTimestamp(expectedTimeStamp)
                .setMerchant("Uber")
                .setAmount(expectedAmount)
                .setMcc("0")
                .setCategory("Local Transportation")
                .setTag("Neiman Marcus Group Inc:NMG P4G Design/Deliver Phase:NMG P4G Design/Deliver Phase:Delivery Assurance")
                .setComment("Uber DFW to office")
                .setReimbursable(true)
                .setOriginalCurrency("USD")
                .setOriginalAmount(expectedAmount)
                .setReceiptURL("https://salesforce.expensify.com/verifyReceipt?action=verifyreceipt&transactionID=7871304959002483&amount=-1868&created=2016-12-12")
                .build();

        ExpenseParser expenseParser = new ExpenseParser(mockCSVFileReader);
        Set<Expense> expenses = expenseParser.parseFile(mockFile);

        assertThat(expenses.contains(expectedExpense), is(true));
    }

    @Test
    public void shouldIgnoreTheHeaderLineInTheExpenseFile() {
        when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(expenseStrings);

        ExpenseParser expenseParser = new ExpenseParser(mockCSVFileReader);
        Set<Expense> actualExpenses = expenseParser.parseFile(mockFile);
        
        assertThat(actualExpenses.size(), is(2));
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenReimbursableIsNotParsable() {
        String expenseString = "\"2016-12-12 00:00:00\",Uber,18.68,0,\"Local Transportation\",\"Neiman Marcus Group Inc:NMG P4G Design/Deliver Phase:NMG P4G Design/Deliver Phase:Delivery Assurance\",\"Uber DFW to office\",foo,USD,18.68,https://salesforce.expensify.com/verifyReceipt?action=verifyreceipt&transactionID=7871304959002483&amount=-1868&created=2016-12-12";
        Set<String> expenseStrings = new HashSet<>(Collections.singletonList(expenseString));

        when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(expenseStrings);

        ExpenseParser expenseParser = new ExpenseParser(mockCSVFileReader);
        expenseParser.parseFile(mockFile);
    }

    @Test
    public void shouldKeepCommentsTogetherWhenTheyContainCommas() {
        Set<String> expenseStrings = new HashSet<>(Collections.singletonList(expenseStringWithExtraCommas));
        when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(expenseStrings);

        ExpenseParser expenseParser = new ExpenseParser(mockCSVFileReader);
        Set<Expense> actualExpenses = expenseParser.parseFile(mockFile);

        Expense actualExpense = actualExpenses.iterator().next();
        assertThat(actualExpense.getComment() , is("Lunch for myself, Max, Blake, Charith, Timothy, Bryan"));
    }

    @Test
    public void shouldParseDollarAmountsOverOneThousandDollars() {
        String expenseOverOneThousandDollars = "\"2017-02-27 12:00:00\",\"Courtyard By Marriott\",\"1,210.23\",0,Hotel,\"Neiman Marcus Group Inc:NMG P4G Design/Deliver Phase:NMG P4G Design/Deliver Phase:Delivery Assurance\",Hotel,yes,USD,\"1,210.23\",https://some-url.com";
        Set<String> expenseStrings = new HashSet<>(Arrays.asList(headerString, expenseOverOneThousandDollars));
        when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(expenseStrings);

        DateTime expectedTimeStamp = new DateTime(2017, 2, 27, 0, 0);
        double expectedAmount = 1210.23;

        Expense expectedExpense = new ExpenseBuilder()
                .setTimestamp(expectedTimeStamp)
                .setMerchant("Courtyard By Marriott")
                .setAmount(expectedAmount)
                .setMcc("0")
                .setCategory("Hotel")
                .setTag("Neiman Marcus Group Inc:NMG P4G Design/Deliver Phase:NMG P4G Design/Deliver Phase:Delivery Assurance")
                .setComment("Uber DFW to office")
                .setReimbursable(true)
                .setOriginalCurrency("USD")
                .setOriginalAmount(expectedAmount)
                .setReceiptURL("https://salesforce.expensify.com/verifyReceipt?action=verifyreceipt&transactionID=7871304959002483&amount=-1868&created=2016-12-12")
                .build();

        ExpenseParser expenseParser = new ExpenseParser(mockCSVFileReader);
        Set<Expense> expenses = expenseParser.parseFile(mockFile);

        assertThat(expenses.contains(expectedExpense), is(true));
    }

    @Test
    public void shouldIgnoreLinesThatStartWithHashSymbol() {
        when(mockCSVFileReader.readCsvFile(mockFile)).thenReturn(expenseStrings);

        ExpenseParser expenseParser = new ExpenseParser(mockCSVFileReader);
        Set<Expense> actualExpenses = expenseParser.parseFile(mockFile);

        assertThat(actualExpenses.size(), is(2));
    }
}
