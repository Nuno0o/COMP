cd ../bin
echo "--------------------"
cat ../res/non_determinism_test
echo "--------------------"
java -cp ".:../lib/commons-io-2.5.jar" Autoanalyze ../res/non_determinism_test > /dev/null
javac -d . -cp ".:../lib/automaton.jar:../lib/commons-io-2.5.jar" ../res/non_determinism_test.java
rm ../res/non_determinism_test.java
read -p "Press ENTER to run the code ..." key
echo "--------------------"
java -cp ".:../lib/commons-io-2.5.jar:../lib/automaton.jar" non_determinism_test
echo "--------------------"
