## Tests
### Running Invitation
Run the following commands from within the Invitation App directory

->mkdir tmp
->javac -d tmp -cp ./test/lib/\*:./test/\* test/Invitation.java
->java -cp ./test/lib/\*:./test/\*:tmp test.Invitation "/path/to/customer_list.txt" 100

### Running Flatten
->mkdir tmp
->javac -d tmp -cp ./test/\* test/Flatten.java
->java -cp ./test/\*:tmp test.Flatten
