all:bin/Autoanalyze.java bin/DotParser.java
	@mkdir -p bin
	javac -d bin -cp "bin;lib/automaton.jar;lib/commons-io-2.5.jar" bin/*.java src/*.java
	
bin/Autoanalyze.java:src/Autoanalyze.jj
	@mkdir -p bin
	java -cp lib/javacc.jar javacc src/Autoanalyze.jj
	@mv *.java bin
	
bin/DotParser.java:src/DotParser.jj
	@mkdir -p bin
	java -cp lib/javacc.jar javacc src/DotParser.jj
	@mv *.java bin
	
clean:
	@rm -rf bin