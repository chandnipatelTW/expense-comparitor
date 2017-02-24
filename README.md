i hate most readmes and most documention.  
they almost always lie.  
the code tells the truth.  always.  
believe the code more than the documentation.

#### Dependencies
- java 8 and the ability to run the gradle wrapper
- your expensify files
- your credit card / bank statements

to run the tests, use this command from the projects root directory:
./gradlew clean build

to run this application, you'll need to do the following:

### BASIC SETUP:
open the file at: <root_project_directory>/src/main/java/org/spargonaut/Application.java
uncomment the line that says: application.run();
its usually found on line 25. ymmv.

download your credit card statements as a csv file.
put them in the following directory:
<root project directory>/data/credit_card_files/
(note: currently, the application is only parsing credit card statements from Chase bank)


download your expense reports from expensify (use the default csv template).
put them in the following directory:
<root project directory>/data/unicode_expense_files

run the command:
./gradlew prepare

### RUNNING THE APPLICATION:
use the command ./gradlew run


### ADVANCED SETUP:
in addition to the basic set up, you can identify specific transactions to be ignored so that they don't show up as unmatched charges.
to do this, you'll need to create a file in the directory <root project directory>/data/manual_ignores/
with the filename of: manual_ignores.csv
you'll need to find the exact lines from one of the credit card activity files, and paste it into this file.
if you begin the line with a hash symbal (#), this line will be considered a comment and ignored when parsing the file.  this is useful to make notes about the expenses that you've manually ignored.
