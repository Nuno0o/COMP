import java.io.*;
import java.util.*;
import dk.brics.automaton.*;
import org.apache.commons.io.*;

public class Test{
	public static void main(String[] args){
		try{
			State s1 = new State();
			State s2 = new State();
			State s3 = new State();
			State s4 = new State();
			State s5 = new State();
			State s6 = new State();
			State s7 = new State();
			State s8 = new State();
			State s9 = new State();
			
			s2.addTransition(new Transition('f',s3));
			s3.addTransition(new Transition('o',s4));
			s4.addTransition(new Transition('o',s5));
			s6.addTransition(new Transition('b',s7));
			s7.addTransition(new Transition('a',s8));
			s8.addTransition(new Transition('r',s9));
			
			s5.setAccept(true);	
			s9.setAccept(true);			
			
			Automaton a = new Automaton();
			a.setInitialState(s1);
			a.restoreInvariant();
			a.setDeterministic(false);
			
			ArrayList<StatePair> pairs = new ArrayList<StatePair>();
			
			pairs.add(new StatePair(s1,s2));
			pairs.add(new StatePair(s1,s6));
			
			a.addEpsilons(pairs);
			
			
			
			System.out.print("foo:");			
			if(a.run("foo")) System.out.println("Accepted");
			else System.out.println("Rejected");
			
			System.out.print("bar:");
			
			if(a.run("bar")) System.out.println("Accepted");
			else System.out.println("Rejected");
		
			FileUtils.writeStringToFile(new File("e_nfa.dot"),a.toDot());
			
		}catch(Exception e){e.printStackTrace();}
	}
}