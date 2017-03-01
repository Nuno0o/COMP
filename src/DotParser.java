package graph.dot;
import graph.*;
import java.io.IOException; 
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * Parse a DOT file into a graph
 *
 * @author Michael Shilman (michaels@eecs.berkeley.edu)
 * @version $Id$
 */
public class DotParser {
    //Assign this the first time a node is seen with
    //Dot properties.  Until then, don't hog space.
	public static int s_dotIndex = AttributeManager.NO_INDEX;
	public static DotFilter s_filter = new DotFilter();
	
    /**
     * Parse a DOT file and output a graph
     */
    public static Graph parse(InputStream s)
    throws IOException, FileFormatException, GraphException{
        Graph g = new Graph();
        Hashtable ht = new Hashtable(50);
        StreamTokenizer st = new StreamTokenizer(s);
        st.eolIsSignificant(true);
		st.wordChars('(','(');
		st.wordChars(')',')');
		st.wordChars('_','_');
        
       // st.commentChar(
        firstline(g, st);
        tokenize(ht, st);
        pack(g, ht);
        return g;
    }

    /**
     * Read the first line from a graph
     */
	protected static void firstline(Graph g, StreamTokenizer st) 
    throws IOException, FileFormatException, GraphException {
        int val = st.nextToken();
        
        if(val == StreamTokenizer.TT_WORD) {
        	System.out.println("Type = " + st.sval);
        }
        else {
        	String err = "Parse error, line "  + st.lineno() +
        					" : Expected keyword graph/digraph.";
        	throw (new FileFormatException(err));
        }
        
        val = st.nextToken();
        String name;
        if(val == StreamTokenizer.TT_WORD || val == '\"') {
        	name = st.sval;
        }
        else if(val == StreamTokenizer.TT_NUMBER) {
        	name = String.valueOf(st.nval);
        }
        else {
        	String err = "Parse error, line "  + st.lineno() +
        					" : Expected graph name.";
        	throw (new FileFormatException(err));
        }
        g.name = name;
        
        val = st.nextToken();
        if(val != '{') {
        	String err = "Parse error, line "  + st.lineno() +
        					" : Expected open brace, '{'.";
        	throw (new FileFormatException(err));
        }
        
        //pop the newline if there is one
        val = st.nextToken();
        if(val != StreamTokenizer.TT_EOL) {
        	st.pushBack();
        }
    }
    
    
    /**
     * Tokenize a DOT file and stick its contents in
     * a hashtable
     */
    protected static void tokenize(Hashtable ht, StreamTokenizer st) 
    throws IOException, FileFormatException, GraphException {
        int val;
        
        while(true) {
        	val = st.nextToken();
        	
            //Get the first node name
            String name;
            if(val == StreamTokenizer.TT_WORD) {
            	name = st.sval;
            }
            else if(val == StreamTokenizer.TT_NUMBER) {
            	name = String.valueOf(st.nval);
            }
            else if(val == '}') {
            	return; //end of file
            }
            else {
            	String err = "Parse error, line "  + st.lineno() +
            					" : Expected node name or \'}\'.";
            	throw (new FileFormatException(err));
            }
            
        	Node n = getNode(name, ht);
        	
        	val = st.nextToken();
        	if(val == '[') {
        		// Must be a list of properties for a node
        		st.pushBack(); //push the '[' back on the stream
        		nodeProperties(n, st);
        	}
        	else if(val == '-') {
        		st.pushBack();
        		nodeEdges(n, ht, st, true);
        	}
        	else {
            	String err = "Parse error, line "  + st.lineno() +
            					" : Expected --, ->, or []'s.";
            	throw (new FileFormatException(err));
        	}
        	
	        val = st.nextToken();
		    if(val != StreamTokenizer.TT_EOL) {		    	
		    	if(val == StreamTokenizer.TT_EOF) {
	            	String err = "Parse error, line "  + st.lineno() +
	            					" : Premature EOF.";
	            	throw (new FileFormatException(err));
		    	}
		    	else {
            		String warn = "Warning, line "  + st.lineno() +
            					" : Extra data at end of line, skipping.";
            		System.out.println(warn);
		    		while(true) {
		    			val = st.nextToken();
		    			if(val == '}') {
		    				return;
		    			}
		    			else if(val == StreamTokenizer.TT_EOL) {
		    				break;
		    			}
		    			else if(val == StreamTokenizer.TT_EOF) {
			            	String err = "Parse error, line "  + st.lineno() +
			            					" : Premature EOF.";
			            	throw (new FileFormatException(err));
		    			}
		    			//else keep skipping
		    			else {
		    				if(val == StreamTokenizer.TT_WORD) {
		    					System.out.println("\tSKIPPING: " + st.sval);
		    				}
		    				else if(val == StreamTokenizer.TT_NUMBER) {
		    					System.out.println("\tSKIPPING: " + st.nval);
		    				}
		    				else {
		    					char c = (char)val;
		    					System.out.println("\tSKIPPING: " + c);
		    				}
		    			}
		    		}
		    	}
		    }
        }
    }
    
    /**
     * On a failure, skip to the next instance of "val"
     * in the stream.
     */
    protected static void recover(int mark, StreamTokenizer st) 
    throws FileFormatException, IOException {
    	int val;
    	while((val = st.nextToken()) != mark) {
    		if(val == StreamTokenizer.TT_EOF) {
    			throw (new FileFormatException());//XXX?
    		}
    	}
    }

    /**
     * Parse all the node properties for a given node.
     * Throw a file format exception if the Node has already
     * been given properties, or if the format is otherwise
     * incorrect.
     */
     
     //
     // XXX - Actually parse LISTS of attributes instead of
     //			just a single attribute!
     //
    protected static void nodeProperties(Node n, StreamTokenizer st) 
    throws IOException, FileFormatException {
    	int val;
    	val = st.nextToken();
    	if(val != '[') {
        	String err = "Parse error, line "  + st.lineno() +
        			" : Expected \'[\' denoting node properties.";
        	throw (new FileFormatException(err));
        }
        
        //get this node's property list
        //if this is the first time, reserve the index
        //in the attribute manager
        if(s_dotIndex == AttributeManager.NO_INDEX) {
        	s_dotIndex = AttributeManager.getIndex("Dot");
        }
        
        DotInfo dotinfo = (DotInfo)n.getAttr(s_dotIndex);
        if(dotinfo == null) {
        	dotinfo = new DotInfo();
        	n.setAttr(s_dotIndex, dotinfo);
        }
        Hashtable props = dotinfo.props;
        if(props == null) {
        	props = new Hashtable(3);
        	dotinfo.props = props;
        }
        
            
        while(true) {
            nodeProperty(props, st);
            val = st.nextToken();
            if(val == ']') {
            	break;
            }
        }
        
        //Push those properties into the node
        s_filter.apply(n);
        
        
        val = st.nextToken();
        if(val != ';') {
        	String err = "Parse eror, line "  + st.lineno() +
        			" : Expected \';\' at end of line.";
        	throw (new FileFormatException(err));
        }       	
        
        //IMPORTANT: DO THIS IN THE UPPER LEVEL
		/*       
		val = st.nextToken();
        if(val != StreamTokenizer.TT_EOL) {
        	String warn = "Warning, line "  + st.lineno() +
        			" : Ignoring extra information at end of line.";
        	System.out.println(warn);
        	recover(StreamTokenizer.TT_EOL, st);
        }
        */
    }
    
    protected static void nodeProperty(Hashtable props, StreamTokenizer st) 
    throws IOException, FileFormatException {          
        int val = st.nextToken();
        String name;
        if(val == StreamTokenizer.TT_WORD) {
        	name = st.sval;
        }
        else if(val == StreamTokenizer.TT_NUMBER) {
        	name = String.valueOf(st.nval);
        }
        else {
        	String err = "Parse error, line "  + st.lineno() +
        					" : Expected property name.";
        	throw (new FileFormatException(err));
        }
        
        if(props.contains(name)) {
    		String warn = "Warning, line "  + st.lineno() +
    					" : Property \"" + name + 
    					"\" already defined.";
    		System.out.println(warn);
        }
        
        val = st.nextToken();
        if(val != '=') {
        	String err = "Parse error, line "  + st.lineno() +
        					" : Expected \'=\' after property \"" + name + "\".";
        	throw (new FileFormatException(err));
        }
        
        val = st.nextToken();
        String prop;
        if(val == StreamTokenizer.TT_WORD || val == '\"') {
        	prop = st.sval;
        }
        else if(val == StreamTokenizer.TT_NUMBER) {
        	prop = String.valueOf(st.nval);
        }
        else {
        	String err = "Parse error, line "  + st.lineno() +
        					" : Expected value for property \"" + name + "\".";
        	throw (new FileFormatException(err));
        }
		props.put(name, prop);
		System.out.println("Setting \"" + name + "\" to \"" + prop + "\".");
    }
    
    
    /**
     * Pack the Node contents of a Hashtable into a Graph
     */
    protected static void pack(Graph g, Hashtable ht) {
        for(Enumeration e = ht.elements(); e.hasMoreElements(); ) {
            Node n = (Node)e.nextElement();
//			System.out.println(">>>> PACKING: " + n.name + ", "  + n.x + ", " + n.y + ", " + n.w + ", " + n.h );
            g.add(n);
        }
    }    
    
    
    
    protected static void nodeEdges(Node n, Hashtable ht, StreamTokenizer st, boolean first) 
    throws IOException, FileFormatException, GraphException {
    	int	val = st.nextToken();
    	if(val == '-') {
    		val = st.nextToken();
    		if((val == '>') || (val == '-')) {
    			boolean directed = (val == '>');

    			String name2;
        		val = st.nextToken();
	            if(val == StreamTokenizer.TT_WORD) {
	            	name2 = st.sval;
	            }
	            else if(val == StreamTokenizer.TT_NUMBER) {
	            	name2 = String.valueOf(st.nval);
	            }
	            else {
	            	String err = "Parse error, line "  + st.lineno() +
	            					" : Expected node name.";
	            	throw (new FileFormatException(err));
	            }
	        	Node n2 = getNode(name2, ht);
        		System.out.println("Attaching " + n.name + " to " + n2.name);
        		Edge e = n.attach(n2);
        		e.directed = directed;
        		if(!directed) {
        		    e.rep.showArrow = false; //XXX
        		}
        		
        		nodeEdges(n2, ht, st, false); //it's okay to have EOL now
        	}
        	else {
            	String err = "Parse error, line "  + st.lineno() +
            					" : Expected -- or ->.";
            	throw (new FileFormatException(err));
        	}
        }
	    else if(val == StreamTokenizer.TT_EOL) {
	    	if(!first) {
		    	st.pushBack();
		    	return;
		    }
		    else {
            	String err = "Parse error, line "  + st.lineno() +
            					" : Expected -- or ->.";
            	throw (new FileFormatException(err));
		    }
	    }
	    else if(val == '[') {
	    	//XXX edge properties
			System.out.println("For now, ignoring edge properties.");
			recover(StreamTokenizer.TT_EOL, st);
			//IMPORTANT FOR UPPER LEVEL
			st.pushBack();
	    }
		else {
	    	String err = "Parse error, line "  + st.lineno() +
	    					" : Expected --, ->, or [...].";
	    	throw (new FileFormatException(err));
		}
    }          		
    
    public static Node getNode(String name, Hashtable ht) {
    	Node n;
		if(ht.containsKey(name)){
	    	n = (Node)ht.get(name);
//			System.out.println(">>>> RETRIEVING: " + n.name + ", " + n.x + ", " + n.y + ", " + n.w + ", " + n.h );
	    }
	    else {
	    	n = new Node();
	    	n.name = name;
	    	n.lbl.label = n.name;
	    	ht.put(name,n);
//			System.out.println(">>>> CREATING: " + n.name + ", " + n.x + ", " + n.y + ", " + n.w + ", " + n.h );
	    }
	    return n;
    }
}