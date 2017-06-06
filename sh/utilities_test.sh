cd ../bin
echo "--------------------"
cat ../res/utilities_test
echo "--------------------"
java -cp ".:../lib/commons-io-2.5.jar" Autoanalyze ../res/utilities_test > /dev/null
javac -d . -cp ".:../lib/automaton.jar:../lib/commons-io-2.5.jar" ../res/utilities_test.java > /dev/null
rm ../res/utilities_test.java
read -p "Press ENTER to run the code ..." key
echo "--------------------"
java -cp ".:../lib/commons-io-2.5.jar:../lib/automaton.jar" utilities_test
echo "--------------------"
