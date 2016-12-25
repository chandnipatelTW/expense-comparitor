package org.spargonaut.io;

import org.junit.Test;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.testbuilders.ExpenseBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataLoaderTest {

    @Test
    public void shouldLoadTheExpenseFiles() {

        String testDirectoryName = "./some-test-directory";

        File mockFileOne = mock(File.class);
        File mockFileTwo = mock(File.class);
        List<File> mockFiles = Arrays.asList(mockFileOne, mockFileTwo);

        CSVFileLoader mockCsvFileLoader = mock(CSVFileLoader.class);
        when(mockCsvFileLoader.getFileNamesIn(testDirectoryName)).thenReturn(mockFiles);

        Expense expenseOneFromMockFileOne = new ExpenseBuilder().build();
        Expense expenseTwoFromMockFileOne = new ExpenseBuilder().build();
        List<Expense> expensesFromMockFileOne = Arrays.asList(expenseOneFromMockFileOne, expenseTwoFromMockFileOne);
        Expense expenseOneFromMockFileTwo = new ExpenseBuilder().build();
        Expense expenseTwoFromMockFileTwo = new ExpenseBuilder().build();
        List<Expense> expensesFromMockFileTwo = Arrays.asList(expenseOneFromMockFileTwo, expenseTwoFromMockFileTwo);
        Parser mockParser = mock(Parser.class);
        when(mockParser.parseFile(mockFileOne)).thenReturn(expensesFromMockFileOne);
        when(mockParser.parseFile(mockFileTwo)).thenReturn(expensesFromMockFileTwo);

        DataLoader dataLoader = new DataLoader(mockCsvFileLoader);
        List<Expense> actualExpenseList = dataLoader.loadExpenses(testDirectoryName, mockParser);

        assertThat(actualExpenseList.size(), is(4));
    }
}