all:bin/Autoanalyze.class bin/DotParser.class

bin/Autoanalyze.class:bin/Autoanalyze.java
	@mkdir -p bin
	javac -o bin -cp "bin;lib/automaton.jar;lib/commons-io-2.5.jar" bin/Autoanalyze.java
	
bin/Autoanalyze.java:src/Autoanalyze.jj
	@mkdir -p bin
	call javacc -o bin src/Autoanalyze.jj

bin/DotParser.class:bin/DotParser.java
	@mkdir -p bin
	javac -o bin -cp "bin;lib/automaton.jar;lib/commons-io-2.5.jar" DotParser.java

bin/DotParser.java:src/DotParser.jj
	@mkdir -p bin
	call javacc -o bin src/DotParser.jj
	
clean:
	@rm -rf bin