cd ../bin
echo "--------------------"
cat ../res/operations_test
echo "--------------------"
java -cp ".:../lib/commons-io-2.5.jar" Autoanalyze ../res/operations_test > /dev/null
javac -d . -cp ".:../lib/automaton.jar:../lib/commons-io-2.5.jar" ../res/operations_test.java
rm ../res/operations_test.java
read -p "Press ENTER to run the code ..." key
echo "--------------------"
java -cp ".:../lib/commons-io-2.5.jar:../lib/automaton.jar" operations_test
echo "--------------------"
read -p "Press ENTER to print the output automata ..." key
echo "--------------------"
cat ../res/test_automata.dot
echo "--------------------"
rm ../res/test_automata.dot
