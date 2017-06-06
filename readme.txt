AUTOANALYZE

Dependências: jdk (compilador javac)
Compilar: make

Nota:
	O makefile e os scripts de teste foram feitos com ferramentas de Linux.
	Recomendamos correr o projeto em Linux ou em Windows com MinGW ou Cygwin.
	
	Na pasta "sh/" existem 3 scripts que demonstram brevemente todas as
	funcionalidades da linguagem. Recomendamos executar os scripts na
	seguinte ordem:
		utilities_test.sh
		non_determinism_test.sh
		operations_test.sh

Funcionamento:

	java Autoanalyze <source> [-d <dest>]

	<source> Ficheiro com código Autoanalyze (vários exemplos na pasta "res/")
	<dest> Código traduzido em Java

Linguagem:

	Input/Output:
		read(<file>): Lê automato de um dot file
		write(X,<file>): Escreve um automato num dot file

	Operações: Retornam um autómato
		product(X,Y)
		intersection(X,Y)
		difference(X,Y)
		union(X,Y)
		concatenation(X,Y)
		complement(X)

	Utilidades: Não retornam um automato
		test(X,<file>): Testa cada linha de um ficheiro com um automato
		info(X): Avalia o determinismo e a linguagem de um automato
		compare(X,Y): Compara as linguagens de dois automatos

	Exemplos:
		/* exemplo de read and write */
		A = read("exemplo.dot");
		write(A,"exemplo.dot");

		/* exemplo de uma atribuição */
		A = product(intersection(A,B),C);

