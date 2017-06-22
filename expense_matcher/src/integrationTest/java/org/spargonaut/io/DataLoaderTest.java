package org.spargonaut.io;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.testbuilders.CreditCardActivityBuilder;
import org.spargonaut.datamodels.testbuilders.ExpenseBuilder;
import org.spargonaut.io.parser.ChargeParser;
import org.spargonaut.io.parser.ExpenseParser;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(HierarchicalContextRunner.class)
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

    public class ExpenseFiles {
        @Test
        public void shouldLoadTheExpenseFiles() {

            ExpenseParser mockParser = mock(ExpenseParser.class);
            when(mockParser.parseFile(mockFileOne)).thenReturn(aSetOfExpenses(aRandomExpense(), aRandomExpense()));
            when(mockParser.parseFile(mockFileTwo)).thenReturn(aSetOfExpenses(aRandomExpense(), aRandomExpense()));

            DataLoader<Expense> dataLoader = new DataLoader<>(mockCsvFileLoader);
            dataLoader.load(testDirectoryName, mockParser);
            Set<Expense> actualExpenseList = dataLoader.getLoadedFiles();

            assertThat(actualExpenseList.size(), is(4));
        }
    }

    public class ChargeFiles {
        private ChargeParser mockParser = mock(ChargeParser.class);
        private DataLoader<CreditCardActivity> dataLoader;

        @Before
        public void setUp() {
            dataLoader = new DataLoader<>(mockCsvFileLoader);
        }

        @Test
        public void shouldLoadTheChargeFiles() {
            when(mockParser.parseFile(mockFileOne)).thenReturn(aSetOfCreditCardActivities(aRandomCreditCardActivity(), aRandomCreditCardActivity()));
            when(mockParser.parseFile(mockFileTwo)).thenReturn(aSetOfCreditCardActivities(aRandomCreditCardActivity(), aRandomCreditCardActivity()));

            dataLoader.load(testDirectoryName, mockParser);
            Set<CreditCardActivity> actualExpenseList = dataLoader.getLoadedFiles();

            assertThat(actualExpenseList.size(), is(4));
        }

        @Test
        public void shouldOnlyLoadUniqueChargesFromSeparateFiles() {
            CreditCardActivity duplicateCreditCardActivity = aRandomCreditCardActivity();
            when(mockParser.parseFile(mockFileOne)).thenReturn(aSetOfCreditCardActivities(aRandomCreditCardActivity(), duplicateCreditCardActivity));
            when(mockParser.parseFile(mockFileTwo)).thenReturn(aSetOfCreditCardActivities(aRandomCreditCardActivity(), duplicateCreditCardActivity));

            dataLoader.load(testDirectoryName, mockParser);
            Set<CreditCardActivity> actualExpenseList = dataLoader.getLoadedFiles();

            assertThat(actualExpenseList.size(), is(3));
        }

        public class IgnoredChargeFiles {

            private final String directoryNameOfFilesToIgnore = "./another-test-directory";
            private File mockFileOfChargesToIgnore = mock(File.class);
            private ChargeParser mockParserForCreditCardActivitiesToIgnore = mock(ChargeParser.class);

            @Test
            public void shouldRemoveChargesThatHaveBeenSpefiedToBeIgnoredFromTheLoadedCharges() {
                CreditCardActivity creditCardActivityOneToKeep = aRandomCreditCardActivity();
                CreditCardActivity creditCardActivityTwoToKeep = aRandomCreditCardActivity();
                CreditCardActivity creditCardActivityOneToIgnore = aRandomCreditCardActivity();
                CreditCardActivity creditCardActivityTwoToIgnore = aRandomCreditCardActivity();
                when(mockParser.parseFile(mockFileOne)).thenReturn(aSetOfCreditCardActivities(creditCardActivityOneToKeep, creditCardActivityOneToIgnore, creditCardActivityTwoToIgnore, creditCardActivityTwoToKeep));
                when(mockCsvFileLoader.getFileNamesIn(directoryNameOfFilesToIgnore)).thenReturn(aSetOfFiles(mockFileOfChargesToIgnore));
                when(mockParserForCreditCardActivitiesToIgnore.parseFile(mockFileOfChargesToIgnore)).thenReturn(aSetOfCreditCardActivities(creditCardActivityOneToIgnore, creditCardActivityTwoToIgnore));

                dataLoader.load(testDirectoryName, mockParser);
                dataLoader.ignore(directoryNameOfFilesToIgnore, mockParserForCreditCardActivitiesToIgnore);
                Set<CreditCardActivity> actualExpenseList = dataLoader.getLoadedFiles();

                assertThat(actualExpenseList.size(), is(2));
                assertThat(actualExpenseList.contains(creditCardActivityOneToKeep), is(true));
                assertThat(actualExpenseList.contains(creditCardActivityTwoToKeep), is(true));
            }

            @Test
            public void shouldRetrieveTheChargesThatHaveBeenIgnored() {
                CreditCardActivity creditCardActivityOneToIgnore = aRandomCreditCardActivity();
                CreditCardActivity creditCardActivityTwoToIgnore = aRandomCreditCardActivity();
                when(mockParser.parseFile(mockFileOne)).thenReturn(aSetOfCreditCardActivities(aRandomCreditCardActivity(), creditCardActivityOneToIgnore, creditCardActivityTwoToIgnore, aRandomCreditCardActivity()));
                when(mockCsvFileLoader.getFileNamesIn(directoryNameOfFilesToIgnore)).thenReturn(aSetOfFiles(mockFileOfChargesToIgnore));
                when(mockParserForCreditCardActivitiesToIgnore.parseFile(mockFileOfChargesToIgnore)).thenReturn(aSetOfCreditCardActivities(creditCardActivityOneToIgnore, creditCardActivityTwoToIgnore));

                dataLoader.load(testDirectoryName, mockParser);
                dataLoader.ignore(directoryNameOfFilesToIgnore, mockParserForCreditCardActivitiesToIgnore);
                Set<CreditCardActivity> actualExpenseList = dataLoader.getIgnoredData();

                assertThat(actualExpenseList.size(), is(2));
                assertThat(actualExpenseList.contains(creditCardActivityOneToIgnore), is(true));
                assertThat(actualExpenseList.contains(creditCardActivityTwoToIgnore), is(true));
            }
        }
    }

    private HashSet<Expense> aSetOfExpenses(Expense... expenses) {
        return new HashSet<>(Arrays.asList(expenses));
    }

    private HashSet<CreditCardActivity> aSetOfCreditCardActivities(CreditCardActivity... activities) {
        return new HashSet<>(Arrays.asList(activities));
    }

    private HashSet<File> aSetOfFiles(File... files) {
        return new HashSet<>(Arrays.asList(files));
    }

    private Expense aRandomExpense() {
        return new ExpenseBuilder().build();
    }

    private CreditCardActivity aRandomCreditCardActivity() {
        return new CreditCardActivityBuilder().build();
    }
}