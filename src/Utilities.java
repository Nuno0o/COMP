import java.io.*;
import java.util.*;
import dk.brics.automaton.*;

public class Utilities{

	public static void info(Automato a1){
		if(a1.isEmpty()){
			System.out.println("Empty Language");
			return;
		}
		if(a1.isEmptyString()){
			System.out.println("Empty String");
			return;
		}
		if(a1.isEmptyString()){
			System.out.println("All Strings");
			return;
		}

	}

	public static void compare(Automaton a1, Automaton a2){
		boolean ans = BasicOperations.subsetOf(a1,a2) &&  BasicOperations.subsetOf(a2,a1);
		if(ans) System.out.println("Same language");
		else System.out.println("Different language");
	}
	public static Automaton product(Automaton a1, Automaton a2){
		Automaton a3 = new Automaton();

		List<State> state_list = new ArrayList<State>();
		List<Transition> trans_list = new ArrayList<Transition>();

		for(State s : a1.getStates()) state_list.add(s);
		State[] s1 = state_list.toArray(new State[state_list.size()]);

		state_list.clear();

		for(State s : a2.getStates()) state_list.add(s);
		State[] s2 = state_list.toArray(new State[state_list.size()]);

		State i1 = a1.getInitialState();
		State i2 = a2.getInitialState();

		State[][] states = new State[s1.length][s2.length];

		for(State[] i : states)	Arrays.fill(i,new State());

		for(int i = 0; i < s1.length; i++){
			for(int j = 0; j < s2.length; j++){

				if(s1[i].isAccept() && s2[j].isAccept()){
					states[i][j].setAccept(true);
				}

				if(s1[i] == i1 && s2[j] == i2){
					a3.setInitialState(states[i][j]);
				}

				for(Transition t : s1[i].getTransitions()) trans_list.add(t);
				Transition[] t1 = trans_list.toArray(new Transition[trans_list.size()]);
				trans_list.clear();

				for(Transition t : s2[j].getTransitions()) trans_list.add(t);
				Transition[] t2 = trans_list.toArray(new Transition[trans_list.size()]);
				trans_list.clear();

				for(int x = 0; x < t1.length; x++){
					for(int y = 0; y < t2.length; y++){

						int min = Math.max((int)t1[x].getMin(),(int)t2[y].getMin());
						int max = Math.min((int)t1[x].getMax(),(int)t2[y].getMax());

						if(min > max) continue;

						int z = -1;
						int w = -1;

						for(int k = 0; k < s1.length; k++){
							if(s1[k] == t1[x].getDest()){
								z = k;
								break;
							}
						}

						for(int k = 0; k < s1.length; k++){
							if(s2[k] == t2[y].getDest()){
								w = k;
								break;
							}
						}

						if(z == -1 || w == -1) continue;

						Transition t = new Transition((char)min,(char)max,states[z][w]);
						states[i][j].addTransition(t);


					}
				}

			}
		}

		return a3;
	}

	public static void test(Automaton a, File f){
		RunAutomaton run_automaton = new RunAutomaton(a);
		ArrayList<String> lines = FileUtils.readLines(f);

		for (String line : lines) {
			System.out.print(line+":");
			if(run_automaton.run(lines)) System.out.println("accepted");
			else 	System.out.print("refused");
		}

	}
}
