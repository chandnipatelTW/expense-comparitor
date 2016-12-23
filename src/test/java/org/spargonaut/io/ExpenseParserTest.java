package org.spargonaut.io;

import org.junit.Test;
import org.spargonaut.datamodels.Expense;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExpenseParserTest {

    @Test
    public void shouldParseAnExpenseLineIntoAnExpense() {
        String headerString = "Timestamp,Merchant,Amount,MCC,Category,Tag,Comment,Reimbursable,\"Original Currency\",\"Original Amount\",Receipt";
        String expenseString = "\"2016-12-12 00:00:00\",Uber,18.68,0,\"Local Transportation\",\"Neiman Marcus Group Inc:NMG P4G Design/Deliver Phase:NMG P4G Design/Deliver Phase:Delivery Assurance\",\"Uber DFW to office\",yes,USD,18.68,https://salesforce.expensify.com/verifyReceipt?action=verifyreceipt&transactionID=7871304959002483&amount=-1868&created=2016-12-12";

        List<String> expenseStrings = Arrays.asList(headerString, expenseString);

        File mockFile = mock(File.class);
        CSVFileReader mockCSVFileReader = mock(CSVFileReader.class);
        when(mockCSVFileReader.readCreditCardFile(mockFile)).thenReturn(expenseStrings);

        ExpenseParser expenseParser = new ExpenseParser(mockCSVFileReader);

        List<Expense> expenses = expenseParser.parseExpenses(mockFile);
        Expense actualExpense = expenses.get(0);

        assertThat(actualExpense.getTimestamp(), is("2016-12-12 00:00:00"));
        assertThat(actualExpense.getMerchant() , is("Uber"));
        assertThat(actualExpense.getAmount() , is("18.68"));
        assertThat(actualExpense.getMCC() , is("0"));
        assertThat(actualExpense.getCategory() , is("Local Transportation"));
        assertThat(actualExpense.getTag() , is("Neiman Marcus Group Inc:NMG P4G Design/Deliver Phase:NMG P4G Design/Deliver Phase:Delivery Assurance"));
        assertThat(actualExpense.getComment() , is("Uber DFW to office"));
        assertThat(actualExpense.isReimbursable() , is("yes"));
        assertThat(actualExpense.getOriginalCurrency() , is("USD"));
        assertThat(actualExpense.getOriginalAmount() , is("18.68"));
        assertThat(actualExpense.getRecieptURL() , is("https://salesforce.expensify.com/verifyReceipt?action=verifyreceipt&transactionID=7871304959002483&amount=-1868&created=2016-12-12"));
    }

    @Test
    public void shouldIgnoreTheHeaderLineInTheExpenseFile() {
        String headerString = "Timestamp,Merchant,Amount,MCC,Category,Tag,Comment,Reimbursable,\"Original Currency\",\"Original Amount\",Receipt";
        String expenseString = "\"2016-12-12 00:00:00\",Uber,18.68,0,\"Local Transportation\",\"Neiman Marcus Group Inc:NMG P4G Design/Deliver Phase:NMG P4G Design/Deliver Phase:Delivery Assurance\",\"Uber DFW to office\",yes,USD,18.68,https://salesforce.expensify.com/verifyReceipt?action=verifyreceipt&transactionID=7871304959002483&amount=-1868&created=2016-12-12";

        List<String> expenseStrings = Arrays.asList(headerString, expenseString);

        File mockFile = mock(File.class);
        CSVFileReader mockCSVFileReader = mock(CSVFileReader.class);
        when(mockCSVFileReader.readCreditCardFile(mockFile)).thenReturn(expenseStrings);

        ExpenseParser expenseParser = new ExpenseParser(mockCSVFileReader);
        List<Expense> actualExpenses = expenseParser.parseExpenses(mockFile);
        
        assertThat(actualExpenses.size(), is(1));
    }
}
