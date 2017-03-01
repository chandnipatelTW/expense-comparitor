the code tells the truth.  always.  
believe the code more than the documentation.

**Dependencies**
- java 8 and the ability to run the gradle wrapper
- your expensify files
- your credit card / bank statements

### Development
to run the tests, use the command:  
./gradlew clean build  

for more tasks, use the command:  
./gradlew tasks  
  
### BASIC SETUP:
download your credit card statements as a csv file.    
put them in the following directory:  
<root project directory>/expense_matcher_cli/data/credit_card_files/    
(note: currently, the application is only parsing credit card statements from Chase bank)    

download your expense reports from expensify (use the default csv template).  
put them in the following directory:  
<root project directory>/expense_matcher_cli/data/unicode_expense_files  

run the command:  
./gradlew prepare
(expensify exports your expense reports in unicode by default.  this script converts them to ASCII and places them in the correct directory)

### RUNNING THE APPLICATION:
use the command ./gradlew run  
  
  
### ADVANCED SETUP:
in addition to the basic set up, you can identify specific transactions and expenses to be ignored so that they don't show up as unmatched charges or expenses.  
to manually ignore charges, create a file in the directory <root project directory>/data/manually_ignored_credit_card_files/  
that ends with .csv
to manually ignore expenses, create a file in the directory <root project directory>/data/manually_ignored_expense_files/  
that ends with .csv

you'll need to find the exact lines from one of the credit card activity files or the expense files, and paste it into their respective ignore file.  
if you begin the line with a hash symbal (#), this line will be considered a comment and ignored when parsing the file.  this is useful to make notes about the lines that you've manually ignored. 
