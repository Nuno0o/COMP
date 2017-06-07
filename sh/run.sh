cd ../bin
java -cp ".:../lib/commons-io-2.5.jar" Autoanalyze ../res/sample_code > /dev/null
javac -d . -cp ".:../lib/automaton.jar:../lib/commons-io-2.5.jar" ../res/sample_code.java > /dev/null
rm ../res/sample_code.java
java -cp ".:../lib/commons-io-2.5.jar:../lib/automaton.jar" sample_code
