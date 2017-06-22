package org.spargonaut;

import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.io.CSVFileLoader;
import org.spargonaut.io.CSVFileReader;
import org.spargonaut.io.DataLoader;
import org.spargonaut.io.parser.*;
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
        CSVFileReader csvFileReader = new CSVFileReader();
        Set<CreditCardActivity> creditCardActivities = new HashSet<>();
        Set<CreditCardActivity> ignoredCreditCardActivities = new HashSet<>();

        DataLoader<CreditCardActivity> boaDataLoader = createChargeDataLoader("bank_of_america", new BankOfAmericaChargeParser(csvFileReader));
        creditCardActivities.addAll(boaDataLoader.loadTransactions());
        ignoredCreditCardActivities.addAll(boaDataLoader.getIgnoredData());

        DataLoader<CreditCardActivity> jpmcDataLoader = createChargeDataLoader("jpmc", new JPMCChargeParser(csvFileReader));
        creditCardActivities.addAll(jpmcDataLoader.loadTransactions());
        ignoredCreditCardActivities.addAll(jpmcDataLoader.getIgnoredData());

        DataLoader<Expense> expenseDataLoader = createExpenseDataLoader("expensify", new ExpenseParser(csvFileReader));
        Set<Expense> expenses = expenseDataLoader.loadTransactions();

        TransactionProcessor transactionProcessor = new TransactionProcessor(creditCardActivities, expenses);
        transactionProcessor.processTransactions(aSetOfMatchers(new ExactMatcher(), new CloseDateMatcher(), new FuzzyMerchantExactAmountMatcher()));

        Map<String, Set<MatchedTransaction>> matchedTransactionsMap = transactionProcessor.getMatchedTransactions();
        Set<CreditCardActivity> unmatchedCreditCardActivity = transactionProcessor.getUnmatchedCreditCardActivies();
        Set<Expense> unmatchedExpenses = transactionProcessor.getUnmatchedExpenses();

        ReportPrinter.printSummary(matchedTransactionsMap,
                                    creditCardActivities,
                                    ignoredCreditCardActivities,
                                    expenses,
                                    unmatchedCreditCardActivity,
                                    unmatchedExpenses);
    }

    private DataLoader<Expense> createExpenseDataLoader(String directoryName, ExpenseParser expenseParser) {
        ParsableDirectory<Expense> expensifyExpensesDirectory = new ParsableDirectory<>(fullPathToExpenseDirectory(directoryName), expenseParser);
        return new DataLoader<>(new CSVFileLoader(), expensifyExpensesDirectory);
    }

    private DataLoader<CreditCardActivity> createChargeDataLoader(String directoryName, Parser<CreditCardActivity> parser) {
        ParsableDirectory<CreditCardActivity> parsableDirectory = new ParsableDirectory<>(fullPathToChargeDirectory(directoryName), parser);
        return new DataLoader<>(new CSVFileLoader(), parsableDirectory);
    }

    private HashSet<TransactionMatcher> aSetOfMatchers(TransactionMatcher... transactionMatchers) {
        return new HashSet<>(Arrays.asList(transactionMatchers));
    }

    private String fullPathToExpenseDirectory(String directory) {
        return dataDirectoryName + fileSeparator + expenseFileDirectoryName + fileSeparator + directory;
    }

    private String fullPathToChargeDirectory(String directory) {
        return dataDirectoryName + fileSeparator + chargeFileDirectoryName + fileSeparator + directory;
    }
}
