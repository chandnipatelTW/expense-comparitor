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
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataLoaderTest {

    private final String testDirectoryName = "./some-test-directory";
    private final File mockFileOne = mock(File.class);
    private final File mockFileTwo = mock(File.class);
    private final Set<File> mockFiles = new HashSet<>(Arrays.asList(mockFileOne, mockFileTwo));
    private final CSVFileLoader mockCsvFileLoader = mock(CSVFileLoader.class);

    @Before
    public void setUp() {
        when(mockCsvFileLoader.getFileNamesIn(testDirectoryName)).thenReturn(mockFiles);
    }

    @Test
    public void shouldLoadTheExpenseFiles() {
        Expense expenseOneFromMockFileOne = new ExpenseBuilder().build();
        Expense expenseTwoFromMockFileOne = new ExpenseBuilder().build();
        Set<Expense> expensesFromMockFileOne = new HashSet<>(Arrays.asList(expenseOneFromMockFileOne, expenseTwoFromMockFileOne));

        Expense expenseOneFromMockFileTwo = new ExpenseBuilder().build();
        Expense expenseTwoFromMockFileTwo = new ExpenseBuilder().build();
        Set<Expense> expensesFromMockFileTwo = new HashSet<>(Arrays.asList(expenseOneFromMockFileTwo, expenseTwoFromMockFileTwo));

        Parser mockParser = mock(Parser.class);
        when(mockParser.parseFile(mockFileOne)).thenReturn(expensesFromMockFileOne);
        when(mockParser.parseFile(mockFileTwo)).thenReturn(expensesFromMockFileTwo);

        DataLoader<Expense> dataLoader = new DataLoader<>(mockCsvFileLoader);
        dataLoader.load(testDirectoryName, mockParser);
        Set<Expense> actualExpenseList = dataLoader.getLoadedFiles();

        assertThat(actualExpenseList.size(), is(4));
    }

    @Test
    public void shouldLoadTheChargeFiles() {
        CreditCardActivity creditCardActivityOneFromMockFileOne = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityTwoFromMockFileOne = new CreditCardActivityBuilder().build();
        Set<CreditCardActivity> creditCardActivitiesFromMockFileOne = new HashSet<>(Arrays.asList(creditCardActivityOneFromMockFileOne, creditCardActivityTwoFromMockFileOne));

        CreditCardActivity creditCardActivityOneFromMockFileTwo = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityTwoFromMockFileTwo = new CreditCardActivityBuilder().build();
        Set<CreditCardActivity> creditCardActivitiesFromMockFileTwo = new HashSet<>(Arrays.asList(creditCardActivityOneFromMockFileTwo, creditCardActivityTwoFromMockFileTwo));

        ChargeParser mockParser = mock(ChargeParser.class);
        when(mockParser.parseFile(mockFileOne)).thenReturn(creditCardActivitiesFromMockFileOne);
        when(mockParser.parseFile(mockFileTwo)).thenReturn(creditCardActivitiesFromMockFileTwo);

        DataLoader<CreditCardActivity> dataLoader = new DataLoader<>(mockCsvFileLoader);
        dataLoader.load(testDirectoryName, mockParser);
        Set<CreditCardActivity> actualExpenseList = dataLoader.getLoadedFiles();

        assertThat(actualExpenseList.size(), is(4));
    }

    @Test
    public void shouldOnlyLoadUniqueChargesFromSeparateFiles() {
        CreditCardActivity creditCardActivityOne = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityTwo = new CreditCardActivityBuilder().build();
        Set<CreditCardActivity> creditCardActivitiesFromMockFileOne = new HashSet<>(Arrays.asList(creditCardActivityOne, creditCardActivityTwo));

        CreditCardActivity creditCardActivityThree = new CreditCardActivityBuilder().build();
        Set<CreditCardActivity> creditCardActivitiesFromMockFileTwo = new HashSet<>(Arrays.asList(creditCardActivityThree, creditCardActivityTwo));

        ChargeParser mockParser = mock(ChargeParser.class);
        when(mockParser.parseFile(mockFileOne)).thenReturn(creditCardActivitiesFromMockFileOne);
        when(mockParser.parseFile(mockFileTwo)).thenReturn(creditCardActivitiesFromMockFileTwo);

        DataLoader<CreditCardActivity> dataLoader = new DataLoader<>(mockCsvFileLoader);
        dataLoader.load(testDirectoryName, mockParser);
        Set<CreditCardActivity> actualExpenseList = dataLoader.getLoadedFiles();

        assertThat(actualExpenseList.size(), is(3));
    }

    @Test
    public void shouldRemoveChargesThatHaveBeenSpefiedToBeIgnoredFromTheLoadedCharges() {
        CreditCardActivity creditCardActivityOne = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityTwo = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityThree = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityFour = new CreditCardActivityBuilder().build();
        Set<CreditCardActivity> creditCardActivities = new HashSet<>(Arrays.asList(creditCardActivityOne, creditCardActivityTwo, creditCardActivityThree, creditCardActivityFour));

        ChargeParser mockParserForCreditCardActivities = mock(ChargeParser.class);
        when(mockParserForCreditCardActivities.parseFile(mockFileOne)).thenReturn(creditCardActivities);

        DataLoader<CreditCardActivity> dataLoader = new DataLoader<>(mockCsvFileLoader);
        dataLoader.load(testDirectoryName, mockParserForCreditCardActivities);


        final String directoryNameOfFilesToIgnore = "./another-test-directory";
        Set<CreditCardActivity> creditCardActivitiesToIgnore = new HashSet<>(Arrays.asList(creditCardActivityTwo, creditCardActivityThree));

        File mockFileOfChargesToIgnore = mock(File.class);
        when(mockCsvFileLoader.getFileNamesIn(directoryNameOfFilesToIgnore)).thenReturn(new HashSet<>(Arrays.asList(mockFileOfChargesToIgnore)));
        ChargeParser mockParserForCreditCardActivitiesToIgnore = mock(ChargeParser.class);
        when(mockParserForCreditCardActivitiesToIgnore.parseFile(mockFileOfChargesToIgnore)).thenReturn(creditCardActivitiesToIgnore);

        dataLoader.ignore(directoryNameOfFilesToIgnore, mockParserForCreditCardActivitiesToIgnore);
        Set<CreditCardActivity> actualExpenseList = dataLoader.getLoadedFiles();

        assertThat(actualExpenseList.size(), is(2));
        assertThat(actualExpenseList.contains(creditCardActivityOne), is(true));
        assertThat(actualExpenseList.contains(creditCardActivityFour), is(true));
    }

    @Test
    public void shouldRetrieveTheChargesThatHaveBeenIgnored() {
        CreditCardActivity creditCardActivityOne = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityTwo = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityThree = new CreditCardActivityBuilder().build();
        CreditCardActivity creditCardActivityFour = new CreditCardActivityBuilder().build();
        Set<CreditCardActivity> creditCardActivities = new HashSet<>(Arrays.asList(creditCardActivityOne, creditCardActivityTwo, creditCardActivityThree, creditCardActivityFour));

        ChargeParser mockParserForCreditCardActivities = mock(ChargeParser.class);
        when(mockParserForCreditCardActivities.parseFile(mockFileOne)).thenReturn(creditCardActivities);

        DataLoader<CreditCardActivity> dataLoader = new DataLoader<>(mockCsvFileLoader);
        dataLoader.load(testDirectoryName, mockParserForCreditCardActivities);


        final String directoryNameOfFilesToIgnore = "./another-test-directory";
        Set<CreditCardActivity> creditCardActivitiesToIgnore = new HashSet<>(Arrays.asList(creditCardActivityTwo, creditCardActivityThree));

        File mockFileOfChargesToIgnore = mock(File.class);
        when(mockCsvFileLoader.getFileNamesIn(directoryNameOfFilesToIgnore)).thenReturn(new HashSet<>(Arrays.asList(mockFileOfChargesToIgnore)));
        ChargeParser mockParserForCreditCardActivitiesToIgnore = mock(ChargeParser.class);
        when(mockParserForCreditCardActivitiesToIgnore.parseFile(mockFileOfChargesToIgnore)).thenReturn(creditCardActivitiesToIgnore);

        dataLoader.ignore(directoryNameOfFilesToIgnore, mockParserForCreditCardActivitiesToIgnore);
        Set<CreditCardActivity> actualExpenseList = dataLoader.getIgnoredData();

        assertThat(actualExpenseList.size(), is(2));
        assertThat(actualExpenseList.contains(creditCardActivityTwo), is(true));
        assertThat(actualExpenseList.contains(creditCardActivityThree), is(true));
    }
}