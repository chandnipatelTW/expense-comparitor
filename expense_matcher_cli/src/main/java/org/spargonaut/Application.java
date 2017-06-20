package org.spargonaut;

import org.spargonaut.datamodels.CreditCardActivity;
import org.spargonaut.datamodels.Expense;
import org.spargonaut.datamodels.MatchedTransaction;
import org.spargonaut.io.CSVFileLoader;
import org.spargonaut.io.CSVFileReader;
import org.spargonaut.io.DataLoader;
import org.spargonaut.io.parser.BankOfAmericaChargeParser;
import org.spargonaut.io.parser.JPMCChargeParser;
import org.spargonaut.io.parser.ExpenseParser;
import org.spargonaut.io.printer.SummaryPrinter;
import org.spargonaut.matchers.CloseDateMatcher;
import org.spargonaut.matchers.ExactMatcher;
import org.spargonaut.matchers.FuzzyMerchantExactAmountMatcher;
import org.spargonaut.matchers.TransactionMatcher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Application {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Application application = new Application();
        application.run();
    }

    public void run() {

        String chargeDirectoryName = "./data/credit_card_files";

        String boaChargeDirectoryName = "./data/bank_of_america";

        String manualIgnoreCreditCardDirectoryName = "./data/manually_ignored_credit_card_files";
        String expenseDirectoryName = "./data/expense_files";
        String manualIgnoreExpenseDirectoryName = "./data/manually_ignored_expense_files";

        CSVFileReader csvFileReader = new CSVFileReader();

        DataLoader<CreditCardActivity> creditCardactivityDataLoader = new DataLoader<>(new CSVFileLoader());

        creditCardactivityDataLoader.load(chargeDirectoryName, new JPMCChargeParser(csvFileReader));
        creditCardactivityDataLoader.ignore(manualIgnoreCreditCardDirectoryName, new JPMCChargeParser(csvFileReader));
        Set<CreditCardActivity> creditCardActivities = new HashSet<>(creditCardactivityDataLoader.getLoadedFiles());

        creditCardactivityDataLoader.load(boaChargeDirectoryName, new BankOfAmericaChargeParser(csvFileReader));
        creditCardActivities.addAll(creditCardactivityDataLoader.getLoadedFiles());


        DataLoader<Expense> expenseDataLoader = new DataLoader<>(new CSVFileLoader());
        expenseDataLoader.load(expenseDirectoryName, new ExpenseParser(csvFileReader));
        expenseDataLoader.ignore(manualIgnoreExpenseDirectoryName, new ExpenseParser(csvFileReader));
        Set<Expense> expenses = new HashSet<>(expenseDataLoader.getLoadedFiles());

        TransactionProcessor transactionProcessor = new TransactionProcessor(creditCardActivities, expenses);

        TransactionMatcher exactMatcher = new ExactMatcher();
        TransactionMatcher closeDateMatcher = new CloseDateMatcher();
        TransactionMatcher fuzzyMatcher = new FuzzyMerchantExactAmountMatcher();
        Set<TransactionMatcher> matchers = new HashSet<>(Arrays.asList(exactMatcher, closeDateMatcher, fuzzyMatcher));
        transactionProcessor.processTransactions(matchers);

        Map<String, Set<MatchedTransaction>> matchedTransactionsMap = transactionProcessor.getMatchedTransactions();
        Set<CreditCardActivity> unmatchedCreditCardActivity = transactionProcessor.getUnmatchedCreditCardActivies();
        Set<Expense> unmatchedExpenses = transactionProcessor.getUnmatchedExpenses();

        SummaryPrinter.printSummary(matchedTransactionsMap,
                                    creditCardActivities,
                                    creditCardactivityDataLoader.getIgnoredData(),
                                    expenses,
                                    unmatchedCreditCardActivity,
                                    unmatchedExpenses);
    }
}
