package org.spargonaut.io;

import org.junit.Before;
import org.junit.Test;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.testbuilders.CreditCardActivityBuilder;
import org.spargonaut.datamodels.testbuilders.ExpenseBuilder;
import org.spargonaut.io.parser.ChargeParser;
import org.spargonaut.io.parser.Parser;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataLoaderTest {

    private final String testDirectoryName = "./some-test-directory";
    private final File mockFileOne = mock(File.class);
    private final File mockFileTwo = mock(File.class);
    private final List<File> mockFiles = Arrays.asList(mockFileOne, mockFileTwo);
    private final CSVFileLoader mockCsvFileLoader = mock(CSVFileLoader.class);

    @Before
    public void setUp() {
        when(mockCsvFileLoader.getFileNamesIn(testDirectoryName)).thenReturn(mockFiles);
    }

    @Test
    public void shouldLoadTheExpenseFiles() {
        Expense expenseOneFromMockFileOne = new ExpenseBuilder().build();
        Expense expenseTwoFromMockFileOne = new ExpenseBuilder().build();
        List<Expense> expensesFromMockFileOne = Arrays.asList(expenseOneFromMockFileOne, expenseTwoFromMockFileOne);

        Expense expenseOneFromMockFileTwo = new ExpenseBuilder().build();
        Expense expenseTwoFromMockFileTwo = new ExpenseBuilder().build();
        List<Expense> expensesFromMockFileTwo = Arrays.asList(expenseOneFromMockFileTwo, expenseTwoFromMockFileTwo);

        Parser mockParser = mock(Parser.class);
        when(mockParser.parseFile(mockFileOne)).thenReturn(expensesFromMockFileOne);
        when(mockParser.parseFile(mockFileTwo)).thenReturn(expensesFromMockFileTwo);

        DataLoader<Expense> dataLoader = new DataLoader<>(mockCsvFileLoader);
        List<Expense> actualExpenseList = dataLoader.load(testDirectoryName, mockParser);

        assertThat(actualExpenseList.size(), is(4));
    }

    @Test
    public void shouldLoadTheChargeFiles() {
        CreditCardActivity creditCardActivityOneFromMockFileOne = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityTwoFromMockFileOne = new CreditCardActivityBuilder().build();
        List<CreditCardActivity> creditCardActivitiesFromMockFileOne = Arrays.asList(creditCardActivityOneFromMockFileOne, creditCardActivityTwoFromMockFileOne);

        CreditCardActivity creditCardActivityOneFromMockFileTwo = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityTwoFromMockFileTwo = new CreditCardActivityBuilder().build();
        List<CreditCardActivity> creditCardActivitiesFromMockFileTwo = Arrays.asList(creditCardActivityOneFromMockFileTwo, creditCardActivityTwoFromMockFileTwo);

        ChargeParser mockParser = mock(ChargeParser.class);
        when(mockParser.parseFile(mockFileOne)).thenReturn(creditCardActivitiesFromMockFileOne);
        when(mockParser.parseFile(mockFileTwo)).thenReturn(creditCardActivitiesFromMockFileTwo);

        DataLoader<CreditCardActivity> dataLoader = new DataLoader<>(mockCsvFileLoader);
        List<CreditCardActivity> actualExpenseList = dataLoader.load(testDirectoryName, mockParser);

        assertThat(actualExpenseList.size(), is(4));
    }

    @Test
    public void shouldOnlyLoadUniqueChargesFromSeparateFiles() {
        CreditCardActivity creditCardActivityOne = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityTwo = new CreditCardActivityBuilder().build();
        List<CreditCardActivity> creditCardActivitiesFromMockFileOne = Arrays.asList(creditCardActivityOne, creditCardActivityTwo);

        CreditCardActivity creditCardActivityThree = new CreditCardActivityBuilder().build();
        List<CreditCardActivity> creditCardActivitiesFromMockFileTwo = Arrays.asList(creditCardActivityThree, creditCardActivityTwo);

        ChargeParser mockParser = mock(ChargeParser.class);
        when(mockParser.parseFile(mockFileOne)).thenReturn(creditCardActivitiesFromMockFileOne);
        when(mockParser.parseFile(mockFileTwo)).thenReturn(creditCardActivitiesFromMockFileTwo);

        DataLoader<CreditCardActivity> dataLoader = new DataLoader<>(mockCsvFileLoader);
        List<CreditCardActivity> actualExpenseList = dataLoader.load(testDirectoryName, mockParser);

        assertThat(actualExpenseList.size(), is(3));
    }
}