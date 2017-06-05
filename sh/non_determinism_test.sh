@cd ../bin
@cat ../res/non_determinism_test
@java -cp ".:../lib/commons-io-2.5.jar" Autoanalyze ../res/non_determinism_test
@javac -cp ".:../lib/automaton.jar:../lib/commons-io-2.5.jar" ../res/non_determinism_test.java
@rm ../res/non_determinism_test.java
echo "\nRunning code ..."
@java -cp ".:../lib/commons-io-2.5.jar:../lib/automaton.jar" non_determinism_test