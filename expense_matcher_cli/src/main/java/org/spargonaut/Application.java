package org.spargonaut;

import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.io.CSVFileLoader;
import org.spargonaut.io.CSVFileReader;
import org.spargonaut.io.DataLoader;
import org.spargonaut.io.parser.ExpenseParser;
import org.spargonaut.io.parser.JPMCChargeParser;
import org.spargonaut.io.parser.ParsableDirectory;
import org.spargonaut.io.printer.ReportPrinter;
import org.spargonaut.matchers.CloseDateMatcher;
import org.spargonaut.matchers.ExactMatcher;
import org.spargonaut.matchers.FuzzyMerchantExactAmountMatcher;
import org.spargonaut.matchers.TransactionMatcher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Application {

    private final String dataDirectoryName = "data";
    private final String fileSeparator = "/";
    private final String chargeFileDirectoryName = "charge_files";
    private final String expenseFileDirectoryName = "expense_files";

    public static void main(String[] args) throws Exception {
        Application application = new Application();
        application.run();
    }

    private void run() {
        String boaChargeDirectoryName = createChargeFileDirectoryPath() + "bank_of_america";

        CSVFileReader csvFileReader = new CSVFileReader();

        String jpmcDirectoryPath = createChargeFileDirectoryPath() + "jpmc";
        ParsableDirectory<CreditCardActivity> jpmcDirectory = new ParsableDirectory<>(jpmcDirectoryPath, new JPMCChargeParser(csvFileReader));
        DataLoader<CreditCardActivity> creditCardactivityDataLoader = new DataLoader<>(new CSVFileLoader(), jpmcDirectory);
        Set<CreditCardActivity> creditCardActivities = creditCardactivityDataLoader.loadTransactions();

        String expenseDirectoryName = expenseFileDirectoryPath() + "expensify";
        ParsableDirectory<Expense> expensifyExpensesDirectory = new ParsableDirectory<>(expenseDirectoryName, new ExpenseParser(csvFileReader));
        DataLoader<Expense> expenseDataLoader = new DataLoader<>(new CSVFileLoader(), expensifyExpensesDirectory);
        Set<Expense> expenses = expenseDataLoader.loadTransactions();

        TransactionProcessor transactionProcessor = new TransactionProcessor(creditCardActivities, expenses);
        Set<TransactionMatcher> matchers = aSetOfMatchers(new ExactMatcher(), new CloseDateMatcher(), new FuzzyMerchantExactAmountMatcher());
        transactionProcessor.processTransactions(matchers);

        Map<String, Set<MatchedTransaction>> matchedTransactionsMap = transactionProcessor.getMatchedTransactions();
        Set<CreditCardActivity> unmatchedCreditCardActivity = transactionProcessor.getUnmatchedCreditCardActivies();
        Set<Expense> unmatchedExpenses = transactionProcessor.getUnmatchedExpenses();

        ReportPrinter.printSummary(matchedTransactionsMap,
                                    creditCardActivities,
                                    creditCardactivityDataLoader.getIgnoredData(),
                                    expenses,
                                    unmatchedCreditCardActivity,
                                    unmatchedExpenses);
    }

    private HashSet<TransactionMatcher> aSetOfMatchers(TransactionMatcher... transactionMatchers) {
        return new HashSet<>(Arrays.asList(transactionMatchers));
    }

    private String expenseFileDirectoryPath() {
        return dataDirectoryName + fileSeparator + expenseFileDirectoryName + fileSeparator;
    }

    private String createChargeFileDirectoryPath() {
        return dataDirectoryName + fileSeparator + chargeFileDirectoryName + fileSeparator;
    }
}
