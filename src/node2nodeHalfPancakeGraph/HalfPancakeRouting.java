package node2nodeHalfPancakeGraph;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * Author: Pongsatorn Siriwong B.Sc. Computer Science KMUTT
 * version 0.1
 * Algorithm based on Paper: Node-to-Node Disjoint Path Problem in Half-pancake Graphs
 * by Sinyu JUNG, and Keiichi KANEKO lab members TUAT
 * 
 * This class implements methods of routing in this problem
 */

public class HalfPancakeRouting{
	private int n;
	private int nTilda;
	private String s;
	private String d;
	
	public HalfPancakeRouting(){
		n = 0;
		nTilda = 0;
		s = null;
		d = null;
	}
	
	public HalfPancakeRouting(PancakeGraph pk){
		this.n = pk.getN();
		this.s = pk.getS();
		this.d = pk.toDestination(s);
		this.nTilda = pk.getNT();
	}
	
	public void routingOperation(){
		//Using the method route(s,d) to find all the vertices in path from s to d and collect in routes
	    // Case 1: n is odd
	    if(n % 2 != 0){                                                          
	    	// Case 1-1: d is in P(s)   e.g. "3214567" and "1234567"
	    	if(s.substring(nTilda).equals(d.substring(nTilda))){
	    		System.out.println("Case 1-1");
	    		ArrayList<String> route = routeOdd_1_1(s,d);
	    		printRoute(route);
	    	}
	    	// Case 1-2: s(n) = d       e.g. "7654321" and "1234567"
	    	else if(prefixReversal(n,s).equals(d)){                              
	    	  	System.out.println("Case 1-2");
	    	  	ArrayList<ArrayList<String>> routes = routeOdd_1_2(s,d);
	    	  	printRoutes(routes);
	    	}
	    	// Case 1-3: s(n) is in P(d) and s(n) != d
	    	else if(prefixReversal(n,s).substring(nTilda).equals(d.substring(nTilda)) && !(prefixReversal(n, s).equals(d))){
	    		System.out.println("Case 1-3");
	    		ArrayList<ArrayList<String>> routes = routeOdd_1_3(s,d);
	    		printRoutes(routes);
	    	}
	    	// Case 1-4: 
	    	else if(checkCase1_4(s)){
	    		System.out.println("Case 1-4");
	    		System.out.println("Under construction");
	    	}
	    	// Case 1-6: Otherwise
	    	else{
	    		System.out.println("Under construction");
	    		System.out.println(prefixReversal(n,s));
	    	}
	    }
	  }
  
	public ArrayList<String> routeOdd_1_1(String s, String d){               //Case 1-1 (d is a member of P(s)) 
		ArrayList<String> paths = new ArrayList<String>();
		//Find path from source
	    paths.add(s);                                                                 //Add source node
	    if(s.equals(d)){                                                              //Check if input source = destination or not
	    	return paths;
	    }else{
		    String path = prefixReversal(n,s);                                        //s(n)
		    paths.add(path);
		    path = prefixReversal(2,path);                                            //s(n,2)
		    paths.add(path);
		    path = prefixReversal(n,path);                                            //s(n,2,n)
		    paths.add(path);
		    //from S(n,2,n) --> d(n,2,n) 
		    //Find path from destination
		    ArrayList<String> dPaths = new ArrayList<String>();
		    dPaths.add(d);                                                             //Add destination node
		    String dPath = prefixReversal(n,d);                                        //d(n)
		    dPaths.add(0,dPath);
		    dPath = prefixReversal(2,dPath);                                           //d(n,2)
		    dPaths.add(0,dPath);
		    dPath = prefixReversal(n,dPath);
		    
		    ArrayList<String> subPaths = route(path,dPath);                            //Get paths from s(n,2,n) -> d(n,2,n) (Same sub-Pancake graph)
		    
		    paths.addAll(subPaths);
		    paths.addAll(dPaths);
		    return paths;
	    }
	  }
  
	public ArrayList<ArrayList<String>> routeOdd_1_2(String s, String d){
    ArrayList<ArrayList<String>> paths = new ArrayList<ArrayList<String>>();
    //Add path p1: s -> d since it is neighbor: path cost = 1
    ArrayList<String> pSD = new ArrayList<String>();
    pSD.add(s);
    pSD.add(d);
    paths.add(pSD);
    for(int i = 2; i < nTilda; i++){
    	ArrayList<String> pI = new ArrayList<String>();
    	String path = s;
    	pI.add(path);
    	path = prefixReversal(i,s);                              //s(i)
    	pI.add(path);
    	path = prefixReversal(n, path);                          //s(i,n)
    	pI.add(path);
    	path = prefixReversal(nTilda-i+2, path);                 //s(i,n,nTilda-i+2)
    	pI.add(path);
    	path = prefixReversal(n, path);                          //s(i,n,nTilda-i+2,n)
    	pI.add(path);
    	path = prefixReversal(i, path);                          //s(i,n,nTilda-i+2,n,i)
    	pI.add(path);
    	path = prefixReversal(n, path);                          //s(i,n,nTilda-i+2,n,i,n)
    	pI.add(path);
    	path = prefixReversal(nTilda-i+2, path);                 //s(i,n,nTilda-i+2,n,i,n,nTilda-i+2) [Should reach destination]
    	pI.add(path);
    	paths.add(pI);
    }
    return paths;
  }
  
	public ArrayList<ArrayList<String>> routeOdd_1_3(String s, String d){
	  ArrayList<ArrayList<String>> paths = new ArrayList<ArrayList<String>>();
	  //Step1 7 8 9
	  if(checkPattern_1_3(s,d)){                                 //Check Step 1 Condition (if there are any pattern of 1 - nTilda in s)
		  //next step... find value of l
		  int l = ((s.indexOf("1")+1)+2)-nTilda;
		  System.out.println("nTilda = " + nTilda);
		  System.out.println("Value of l = " + l);
		  for(int i = 2; i < nTilda; i++){
			  if(i == l){
				  //Step 7: routing p1 (s -> d)
				  ArrayList<String> p1 = new ArrayList<String>();
				  p1.add(s);                                             //Add s to p1
				  String path = prefixReversal(l,s);                     //s(l)
				  p1.add(path);
				  path = prefixReversal(n,path);                         //s(l,n)
				  p1.add(path);
				  path = prefixReversal((nTilda-l)+2, path);             //s(l,n,(nTilda-l)+2)
				  p1.add(path);
				  path = prefixReversal(n, path);                        //s(l,n,(nTilda-l)+2,n)
				  p1.add(path);
				  path = prefixReversal(l, path);                        //s(l,n,(nTilda-l)+2,n,l)
				  p1.add(path);                                          
				  path = prefixReversal(n, path);                        //s(l,n,(nTilda-l)+2,n,l,n) --> Should reach destination
				  p1.add(path);
				  paths.add(p1);                                         //Add path p1 to collection
			  }
			  else{
				  System.out.println("i = " + i);
				  //Step 8: routing ai (while i = 2 -> nTilda && i != l)
				  ArrayList<String> path = new ArrayList<String>();
				  path.add(s);
				  String vertex = prefixReversal(i, s);            //s(i)
				  path.add(vertex);
				  vertex = prefixReversal(n, vertex);              //s(i,n)
				  path.add(vertex);
				  vertex = prefixReversal(nTilda-i+2, vertex);     //s(i,n,nTilda-i+2)
				  path.add(vertex);
				  vertex = prefixReversal(n, vertex);              //s(i,n,nTilda-i+2,n)
				  path.add(vertex);
				  vertex = prefixReversal(i, vertex);              //s(i,n,nTilda-i+2,n,i)
				  path.add(vertex);
				  vertex = prefixReversal(n, vertex);              //s(i,n,nTilda-i+2,n,i,n) :: now path ai is complete
				  path.add(vertex);                                //add path ai to collection
				  paths.add(path);                                 //add path in Step 8 to collection
			  }
		  }
	  }
	  //Step2 3 4
	  else{
		  for(int i = 1; i <= nTilda; i++){
			  //Step 4: routing a1
			  if(i == 1){
				  ArrayList<String> path = new ArrayList<String>();
				  path.add(s);
				  path.add(prefixReversal(n,s));                 
				  paths.add(path);                               //add path a1 to collection
			  }
			  //Step 2: routing a2
			  else if(i == 2){
				  ArrayList<String> path = new ArrayList<String>();
				  path.add(s);
				  String vertex = prefixReversal(2, s);           //s(2)
				  path.add(vertex);
				  vertex = prefixReversal(n, vertex);             //s(2,n)
				  path.add(vertex);
				  vertex = prefixReversal(nTilda, vertex);        //s(2,n,nTilda)
				  path.add(vertex);
				  //generate a2
				  char[] a2Chars = d.toCharArray();
				  char tmp = a2Chars[n-2];
				  a2Chars[n-2] = a2Chars[n-1];
				  a2Chars[n-1] = tmp;
				  String a2 = new String(a2Chars);                //a2
				  path.add(a2);                                   
				  path.addAll(route(vertex,a2));                  //s(2,n,nTilda) -> a2 
				  a2 = prefixReversal(n, a2);                     //a2(n)
				  path.add(a2);                
				  a2 = prefixReversal(2, a2);                     //a2(n,2)
				  path.add(a2);
				  a2 = prefixReversal(n, a2);                     //a2(n,2,n)  should reach destination
				  path.add(a2);
				  paths.add(path);                                //add path in Step 2 to collection
			  }
			  //Step 3 routing ai while (i >= 3 && i <= nTilda)
			  else{
				  ArrayList<String> path = new ArrayList<String>();
				  path.add(s);
				  String vertex = prefixReversal(i, s);            //s(i)
				  path.add(vertex);
				  vertex = prefixReversal(n, vertex);              //s(i,n)
				  path.add(vertex);
				  vertex = prefixReversal(nTilda-i+2, vertex);     //s(i,n,nTilda-i+2)
				  path.add(vertex);
				  vertex = prefixReversal(n, vertex);              //s(i,n,nTilda-i+2,n)
				  path.add(vertex);
				  vertex = prefixReversal(i, vertex);              //s(i,n,nTilda-i+2,n,i)
				  path.add(vertex);
				  vertex = prefixReversal(n, vertex);              //s(i,n,nTilda-i+2,n,i,n) :: now path ai is complete
				  path.add(vertex);                                //add path ai to collection
				  paths.add(path);                                 //add path in Step 3 to collection
			  }
		  }
	  }
	  
	  return paths;
  }
  
	public boolean checkPattern_1_3(String s, String d){
	   //Condition Identifier 
	   String[] checker = new String[nTilda];
	   for(int i = 0; i < nTilda; i++){
	     checker[i] = d.substring(i, i+1);
	   }
	   //Create a checker string
	   String sPattern = s.substring(nTilda-1);
	   //pattern 1 -> (nTilda-1)
	   if(checker.equals(sPattern)){
	    return true;
	   }else{
	  //pattern 2++ -> (nTilda-1)
	  //String[] ck = new String[nTilda];
	    for(int i = -1; i < nTilda-2; i++){
	     //String[] ck = new String[nTilda];
	     //Collect pattern in an array
	      if(i < 0){
	         String c = "";
	         for(int j = 0; j < nTilda; j++){
	           c += checker[j];
	         }
	         /*For checking the value in this algorithm
	         System.out.println(Arrays.toString(checker));
	         System.out.println("c = " + c);
	         System.out.println("sP = " + sPattern);*/
	         continue;
	      }
	     String temp1;
	     String temp2 = checker[i];
	     for(int j = i; j < nTilda  ; j++){
	       temp1 = temp2;
	       temp2 = checker[((j+1) % nTilda)+((j+1)/nTilda)*i];
	       checker[((j+1) % nTilda)+ ((j+1)/nTilda)*i] = temp1;
	     }
	     String c = "";
	     for(int j = 0; j < nTilda; j++){
	       c += checker[j];
	     }
	     /*For checking the value in this algorithm
         System.out.println(Arrays.toString(checker));
         System.out.println("c = " + c);
         System.out.println("sP = " + sPattern);*/
	     //Check
	     if(c.equals(sPattern)){
	      return true;
	     }else{
	      continue;
	     }
	    }
	    return false;
	   }
	  }
    
	public boolean checkCase1_4(String s){
		  for(int k = 2; k < nTilda; k++){
			  String sP = prefixReversal(k,s);
			  sP = prefixReversal(n,sP);
			  //Check if sP is in the same pancake graph as d and sP is not d
			  if(sP.substring(nTilda).equals(d.substring(nTilda)) && !sP.equals(d)){
				  return true;
			  }else{
				  continue;
			  }
		  }
		  return false;
	}
	//This method implements prefix reversal operation used in Pancake graph routing
	public String prefixReversal(int i, String u){                   
	    String s = "";
	    //System.out.println("PR Input = " + u);                              //For checking
	    for(int j = 0, k = i; j < i; j++, k--){
	      s += u.substring(k-1,k);
	    }
	    for(int k = i; k < u.length(); k++){
	      s += u.substring(k,k+1);
	    }
	    //System.out.println("PR Output = " + s);                             //For checking
	    return s;
	  }
	  
	//This method print out the route collecting as String in ArrayList
	public void printRoute(ArrayList<String> r){
	    System.out.println("Routing from s -> d");
	    for(int i = 0; i < r.size(); i++){
	      if(i == r.size()-1){
	        System.out.print(r.get(i));
	      }else{
	        System.out.print(r.get(i) + " -> " );
	      }
	    }
	  }
	  
	public void printRoutes(ArrayList<ArrayList<String>> r){
		  for(int i = 0; i < r.size();i++){
			  System.out.println("Routing from s -> d" + ": Path " + (i+1));
			  for(int j = 0; j < r.get(i).size(); j++){
				  if(j == r.get(i).size()-1){
				      System.out.print(r.get(i).get(j));
				  }else{
				      System.out.print(r.get(i).get(j) + " -> " );
				  }
			  }
			  System.out.println();
		  }
	  };
	
	//This method implements a routing algorithm inside a Pancake graph
	public ArrayList<String> route(String s, String d){             
	    ArrayList<String> routes = new ArrayList<String>();                  // Declare a collector for collecting routes vertices
	    for(int i = s.length()-1; i >= 0; i--){                               //for i:=n to 1 step -1
	      if(!(s.substring(i,i+1).equals(d.substring(i,i+1)))){               //if s[i] != d[i] begin...
	        String sR = "";
	        for(int k = 0; k < s.length(); k++){
	          if(s.substring(k,k+1).equals(d.substring(i,i+1))){              //find k such that s[k] = d[i]
	            //System.out.println("k = " + (k+1));                         //For checking k
	            sR = prefixReversal(k+1,s);                                   //sR = PrefixReversal(k,s)
	            if(k > 1){                                                    // if k > 1 
	              routes.add(sR);                                             //then add the sR to routes
	            }
	          }
	        }
	        //System.out.println("i = " + (i+1));                             //For checking i
	        s = prefixReversal(i+1,sR);                                         
	        routes.add(s);
	      }
	    }
	    return routes;
	  }
}
