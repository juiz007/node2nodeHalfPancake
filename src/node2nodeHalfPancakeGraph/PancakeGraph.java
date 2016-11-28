package node2nodeHalfPancakeGraph;


import java.util.Arrays;

/*
 * Author: Pongsatorn Siriwong B.Sc. Computer Science KMUTT
 * version 0.1
 * Algorithm based on Paper: Node-to-Node Disjoint Path Problem in Half-pancake Graphs
 * by Sinyu JUNG, and Keiichi KANEKO lab members TUAT
 * 
 * This class collect all parameter of a pancake graph
 */

public class PancakeGraph{
	private int n;
	private String s;
	private String d;
	private int nTilda;
	
	//Constructor method
	public PancakeGraph(){
		n = 0;
		s = null;
		d = null;
	}
	
	public PancakeGraph(int n, String s){
		this.n = n;
		this.s = s;
		this.d = toDestination(s);
		nTilda = (n/2)+1;
	}
	
	 //This method sort the source node in order to get the destination node (which is sorted of n-integers in pancake graph)
	 //e.g. "1975423a86b" -> "123456789ab"
	 public String toDestination(String s){
	    char[] tempChars = s.toCharArray();
	    Arrays.sort(tempChars);
	    String d = new String(tempChars);
	    return d;
	 }
	 
	 public int getN(){
		 return n;
	 }
	 
	 public String getS(){
		 return s;
	 }
	 
	 public String getD(){
		 return d;
	 }
	 
	 public int getNT(){
		 return nTilda;
	 }
}
