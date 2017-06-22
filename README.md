## Description
This application compares credit card statements and expense statements to help you find any charges you forgot to
expense.  It does this by processing credit card statements and expense reports and comparing them using several
different types of matching techniques. 

**Dependencies**
- java 8 and the ability to run the gradle wrapper
- your expensify files
- your credit card / bank statements
    - note: the application currently supports statements from JPM Chase bank and Bank of America

### BASIC SETUP:
download your credit card statements as a csv file.
put them in the following directories:  
for JPM chase statements:: \<root project directory>/expense_matcher_cli/data/charge_files/jpmc/  
for bank of america statements: \<root project directory>/expense_matcher_cli/data/charge_files/bank_of_america/  

for expenses:   
download your expense reports from expensify (use the default csv template).  
put them in the following directory:  
\<root project directory>/expense_matcher_cli/data/unicode_expense_files  

run the command:  
./gradlew prepare  
  
This will convert the expensify files from unicode to ASCII, and copy them to the correct directory:
\<root project directory>/data/expense_files/expensify/  
The need to 'prepare' the expensify files is due to them being exported in unicode by default.
This script converts them to ASCII and places them in the correct directory  
  

### RUNNING THE APPLICATION:
use the command ./gradlew run  
 
  
#### Ignoring certain transactions:
in addition to the basic set up, you can identify specific transactions and expenses to be ignored so that they don't
show up as unmatched charges or expenses.  To manually ignore charges, create a .csv file in the directory  
for JPM Chase charges: \<root project directory>/expense_matcher_cli/data/charge_files/jpmc/manually_ignored/
for Bank of America charges: \<root project directory>/expense_matcher_cli/data/charge_files/bank_of_america/manually_ignored
for expenses: \<root project directory>/data/expense_files/expensify/manually_ignored  


you'll need to find the exact lines from one of the credit card activity files or the expense files, and paste it into their respective ignore file.  
if you begin the line with a hash symbal (#), this line will be considered a comment and ignored when parsing the file.  this is useful to make notes about the lines that you've manually ignored. 


### Development notes
to run the tests, use the command:  
./gradlew clean build  

for more tasks, use the command:  
./gradlew tasks  