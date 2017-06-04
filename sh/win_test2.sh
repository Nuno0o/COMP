cd .. &&
javac -d bin -cp "bin;lib/automaton.jar;lib/commons-io-2.5.jar" res/sample_code.java &&
rm res/sample_code.java &&
cd bin &&
java -cp ".;../lib/commons-io-2.5.jar;../lib/automaton.jar" sample_code
read
