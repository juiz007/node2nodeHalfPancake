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
	private int nT;
	private String s;
	private String d;
	private int case4_k;
	
	public HalfPancakeRouting(){
		n = 0;
		nT = 0;
		s = null;
		d = null;
	}
	
	public HalfPancakeRouting(PancakeGraph pk){
		this.n = pk.getN();
		this.s = pk.getS();
		this.d = pk.toDestination(s);
		this.nT = pk.getNT();
	}
	
	public void routingOperation(){
		//Using the method route(s,d) to find all the vertices in path from s to d and collect in routes
	    // Case 1: n is odd
	    if(n % 2 != 0){                                                          
	    	// Case 1-1: d is in P(s)   --> s is _ _ _ 4 5 6 7
	    	if(s.substring(nT-1).equals(d.substring(nT-1))){
	    		System.out.println("Case 1-1");
	    		ArrayList<String> route = routeOdd_1_1(s,d);
	    		printRoute(route);
	    	}
	    	// Case 1-2: s(n) = d       --> s = "7654321" and d = "1234567"
	    	else if(prefixReversal(n,s).equals(d)){                              
	    	  	System.out.println("Case 1-2");
	    	  	ArrayList<ArrayList<String>> routes = routeOdd_1_2(s,d);
	    	  	printRoutes(routes);
	    	}
	    	// Case 1-3: s(n) is in P(d) and s(n) != d    --> s = "7654+++" 
	    	else if(prefixReversal(n,s).substring(nT-1).equals(d.substring(nT-1)) && !(prefixReversal(n, s).equals(d))){
	    		System.out.println("Case 1-3");
	    		ArrayList<ArrayList<String>> routes = routeOdd_1_3(s,d);
	    		printRoutes(routes);
	    	}
	    	// Case 1-4: (∃k(<nT) such that s(k,n) ∈ P(d) and s(k,n) != d)
	    	else if(checkCase1_4(s)){
	    		System.out.println("Case 1-4");
	    		ArrayList<ArrayList<String>> routes = routeOdd_1_4(s,d);
	    		printRoutes(routes);
	    	}
	    	// Case 1-5: ( s(nT,n) is in P(d) and s(nT,n) != d )
	    	else if(checkCase1_5(s)){
	    		System.out.println("Case 1-5");
	    		ArrayList<ArrayList<String>> routes = routeOdd_1_5(s,d);
	    		printRoutes(routes);
	    	}
	    	// Case 1-6: Otherwise
	    	else{
	    		System.out.println("Under construction");
	    		System.out.println(prefixReversal(n,s));
	    	}
	    }
	  }
  
	public ArrayList<String> routeOdd_1_1(String s, String d){                        //Case 1-1 (d is a member of P(s)) 
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
    //Step 1: Add path p1: s -> d since it is neighbor: path cost = 1
    ArrayList<String> pSD = new ArrayList<String>();
    pSD.add(s);
    pSD.add(d);
    paths.add(pSD);
    //Step 2: Add nT-1 paths
    for(int i = 2; i <= nT; i++){
    	ArrayList<String> pI = new ArrayList<String>();
    	String path = s;
    	pI.add(path);
    	path = prefixReversal(i,s);                              //s(i)
    	pI.add(path);
    	path = prefixReversal(n, path);                          //s(i,n)
    	pI.add(path);
    	path = prefixReversal((nT-i)+2, path);                   //s(i,n,nT-i+2)
    	pI.add(path);
    	path = prefixReversal(n, path);                          //s(i,n,nT-i+2,n)
    	pI.add(path);
    	path = prefixReversal(i, path);                          //s(i,n,nT-i+2,n,i)
    	pI.add(path);
    	path = prefixReversal(n, path);                          //s(i,n,nT-i+2,n,i,n)
    	pI.add(path); 
    	path = prefixReversal((nT-i)+2, path);                   //s(i,n,nT-i+2,n,i,n,nT-i+2) [Should reach destination]
    	pI.add(path);
    	paths.add(pI);
    }
    return paths;
	}
	
	public ArrayList<ArrayList<String>> routeOdd_1_3(String s, String d){
	  ArrayList<ArrayList<String>> paths = new ArrayList<ArrayList<String>>();
	  //Step1 7 8 9
	  if(checkArrangePattern(s,d)){                                 //Check Step 1 Condition (if there are any pattern of 1 - nT in s)
		  //next step... find value of l
		  int l = ((s.indexOf("1")+1)+2)-nT;
		  //System.out.println("nT = " + nT);
		  //System.out.println("Value of l = " + l);
		  for(int i = 2; i <= nT; i++){
			  if(i == l){
				  //Step 7: routing p1 (s -> d)
				  ArrayList<String> p1 = new ArrayList<String>();
				  p1.add(s);                                             //Add s to p1
				  String path = prefixReversal(l,s);                     //s(l)
				  p1.add(path);
				  path = prefixReversal(n,path);                         //s(l,n)
				  p1.add(path);
				  path = prefixReversal((nT-l)+2, path);                 //s(l,n,(nT-l)+2)
				  p1.add(path);
				  path = prefixReversal(n, path);                        //s(l,n,(nT-l)+2,n)
				  p1.add(path);
				  path = prefixReversal(l, path);                        //s(l,n,(nT-l)+2,n,l)
				  p1.add(path);                                          
				  path = prefixReversal(n, path);                        //s(l,n,(nT-l)+2,n,l,n) --> Should reach destination
				  p1.add(path);
				  paths.add(p1);                                         //Add path p1 to collection
			  }
			  else{
				  //Step 8: routing aI (while i = 2 -> nT && i != l)
				  ArrayList<String> path = new ArrayList<String>();
				  path.add(s);
				  String vertex = prefixReversal(i, s);            //s(i)
				  path.add(vertex);
				  vertex = prefixReversal(n, vertex);              //s(i,n)
				  path.add(vertex);
				  vertex = prefixReversal((nT-i)+2, vertex);       //s(i,n,nT-i+2)
				  path.add(vertex);
				  vertex = prefixReversal(n, vertex);              //s(i,n,nT-i+2,n)
				  path.add(vertex);
				  vertex = prefixReversal(i, vertex);              //s(i,n,nT-i+2,n,i)
				  path.add(vertex);
				  vertex = prefixReversal(n, vertex);              //s(i,n,nT-i+2,n,i,n) :: now path aI is complete
				  path.add(vertex);                                //add path aI to collection
				  paths.add(path);                                 //add path in Step 8 to collection
			  }
		  }
		  //Step 9: Select routing to a1
		  ArrayList<String> pA1 = new ArrayList<String>();
		  pA1.add(s);
		  pA1.add(prefixReversal(n, s));                           //reach a1
		  paths.add(pA1);
		  //Step 10&11: Apply PNS in P(d) to obtain nT-1 disjoint sub paths and route them with ai from each disjoint paths from s
	  }
	  //Step2 3 4
	  else{
		  for(int i = 1; i <= nT; i++){
			  //Step 4: routing a1
			  if(i == 1){
				  ArrayList<String> pA1 = new ArrayList<String>();
				  pA1.add(s);
				  pA1.add(prefixReversal(n,s));                 
				  paths.add(pA1);                               //add path a1 to collection
			  }
			  //Step 2: routing to a2
			  else if(i == 2){
				  ArrayList<String> p2 = new ArrayList<String>();
				  p2.add(s);
				  String vertex = prefixReversal(2, s);           //s(2)
				  p2.add(vertex);
				  vertex = prefixReversal(n, vertex);             //s(2,n)
				  p2.add(vertex);
				  vertex = prefixReversal(nT, vertex);            //s(2,n,nT)
				  p2.add(vertex);
				  //generate a2
				  char[] a2Chars = d.toCharArray();
				  char tmp = a2Chars[n-2];
				  a2Chars[n-2] = a2Chars[n-1];
				  a2Chars[n-1] = tmp;
				  String a2 = new String(a2Chars);                //a2                                  
				  p2.addAll(route(vertex,a2));                    //sub path from s(2,n,nT) -> a2 
				  a2 = prefixReversal(n, a2);                     //a2(n)
				  p2.add(a2);                
				  a2 = prefixReversal(2, a2);                     //a2(n,2)
				  p2.add(a2);
				  a2 = prefixReversal(n, a2);                     //a2(n,2,n)  should reach destination
				  p2.add(a2);
				  paths.add(p2);                                //add path in Step 2 to collection
			  }
			  //Step 3 routing aI while (i >= 3 && i <= nT)
			  else{
				  ArrayList<String> pi = new ArrayList<String>();
				  pi.add(s);
				  String vertex = prefixReversal(i, s);            //s(i)
				  pi.add(vertex);
				  vertex = prefixReversal(n, vertex);              //s(i,n)
				  pi.add(vertex);
				  vertex = prefixReversal(nT-i+2, vertex);     //s(i,n,nT-i+2)
				  pi.add(vertex);
				  vertex = prefixReversal(n, vertex);              //s(i,n,nT-i+2,n)
				  pi.add(vertex);
				  vertex = prefixReversal(i, vertex);              //s(i,n,nT-i+2,n,i)
				  pi.add(vertex);
				  vertex = prefixReversal(n, vertex);              //s(i,n,nT-i+2,n,i,n) :: now path ai is complete
				  pi.add(vertex);                                //add path ai to collection
				  paths.add(pi);                                 //add path in Step 3 to collection
			  }
			  //Step 5&6: Apply PNS in P(d) to obtain nT-1 disjoint subpaths and route them with ai from each disjoint paths from s
		  }
	  }
	  
	  return paths;
  }
    
	public ArrayList<ArrayList<String>> routeOdd_1_4(String s, String d){
		ArrayList<ArrayList<String>> paths = new ArrayList<ArrayList<String>>();
		if(checkArrangePattern(s,d)){
			int l = ((s.indexOf("1")+1)+2)-nT;
			//Step 7) Find path to a1
			ArrayList<String> pathA1 = new ArrayList<String>();
			pathA1.add(s);
			String p1 = prefixReversal(n, s);                             //s(n)
			pathA1.add(p1);
			p1 = prefixReversal((nT-case4_k)+2, p1);                      //s(n,nT-k+2)
			pathA1.add(p1);
			p1 = prefixReversal(n, p1);                                   //s(n,nT-k+2,n)
			pathA1.add(p1);
			p1 = prefixReversal(case4_k, p1);                             //s(n,nT-k+2,n,k)
			pathA1.add(p1);
			p1 = prefixReversal(n, p1);                                   //s(n,nT-k+2,n,k,n) --> reach a1
			pathA1.add(p1);
			paths.add(pathA1);
			
			for(int i = 2; i <= nT; i++){
				if(i == l){
					//Step 6) Find path pl (s -> d)
					ArrayList<String> pl = new ArrayList<String>();
					pl.add(s);                                             //Add s to p1
					String path = prefixReversal(l,s);                     //s(l)
					pl.add(path);
					path = prefixReversal(n,path);                         //s(l,n)
					pl.add(path);
					path = prefixReversal((nT-l)+2, path);                 //s(l,n,(nT-l)+2)
					pl.add(path);
					path = prefixReversal(n, path);                        //s(l,n,(nT-l)+2,n)
					pl.add(path);
					path = prefixReversal(l, path);                        //s(l,n,(nT-l)+2,n,l)
					pl.add(path);
					path = prefixReversal(case4_k, path);                  //s(l,n,(nT-l)+2,n,l,k)
					pl.add(path);
					path = prefixReversal(n, path);                        //s(l,n,(nT-l)+2,n,l,k,n) --> Should reach destination
					pl.add(path);
					paths.add(pl);                                         //Add path pl to collection
				}
				else{
					//Step 8) Find path pI where 2 <= i <= nT && != l  [Total = n-2 paths]
					ArrayList<String> pI = new ArrayList<String>();
					pI.add(s);
					String path = prefixReversal(i, s);            //s(i)
					pI.add(path);
					path = prefixReversal(n, path);              //s(i,n)
					pI.add(path);
					path = prefixReversal(nT-i+2, path);         //s(i,n,nT-i+2)
					pI.add(path);
					path = prefixReversal(n, path);              //s(i,n,nT-i+2,n)
					pI.add(path);
					path = prefixReversal(i, path);              //s(i,n,nT-i+2,n,i)
					pI.add(path);
					path = prefixReversal(case4_k, path);        //s(i,n,nT-i+2,n,i,k)
					pI.add(path);
					path = prefixReversal(n, path);              //s(i,n,nT-i+2,n,i,k,n)
					pI.add(path);
					paths.add(pI);                               //add pI to collection
				}
			}
		}else{
			for(int i = 1; i <= nT; i++){
				if(i < 2){
					ArrayList<String> p1 = new ArrayList<String>();
					//Step 2) Find path p1 (s -> d)
					String vertex = s;
					p1.add(vertex);                                     //s
					vertex = prefixReversal(n, vertex);
				    p1.add(vertex);                                     //s(n)
				    String aN = suffixReversal(case4_k,d);             //aN = (1,2,...,n-k,n,n-1,...,n-k+1)
				    p1.addAll(route(vertex,aN));                       //Sub path from s(n) to aN
				    aN = prefixReversal(n,aN);                         //aN(n)
				    p1.add(aN);
				    aN = prefixReversal(case4_k,aN);                   //aN(n,k) ==> should reach d(n);
				    p1.add(aN);
				    aN = prefixReversal(n,aN);                         //should reach d;
				    p1.add(aN);
				    paths.add(p1);
				}else{
				    //Step 3: find path pI (i = 2 -> nT)
					String vertex = s;
					ArrayList<String> path = new ArrayList<String>();
					path.add(vertex);
					vertex = prefixReversal(i,vertex);                        //s(i)
					path.add(vertex);
					vertex = prefixReversal(n,vertex);                        //s(i,n)
					path.add(vertex);
					vertex = prefixReversal((nT-i)+2,vertex);                 //s(i,n,nT-i+2)
					path.add(vertex);
					vertex = prefixReversal(i, vertex);                       //s(i,n,nT-i+2,i)
					path.add(vertex);
					vertex = prefixReversal(case4_k,vertex);                  //s(i,n,nT-i+2,i,k)
					path.add(vertex);
					vertex = prefixReversal(n, vertex);                       //s(i,n,nT-i+2,i,k)
					path.add(vertex);
					paths.add(path);
				}
			}
		}
		
		return paths;
	}
	
	public ArrayList<ArrayList<String>> routeOdd_1_5(String s, String d){
		ArrayList<ArrayList<String>> paths = new ArrayList<ArrayList<String>>();
		if(checkPattern_1_5(s,d)){
			int l = n-(s.indexOf("1")+1)+1;
			//Step 8) Find path to a1
			ArrayList<String> p1 = new ArrayList<String>();
			String vertex = s;                                               //s
			p1.add(vertex);                                                 
			vertex = prefixReversal(n, vertex);                              //s(n)
			p1.add(vertex);
			vertex = prefixReversal(nT, vertex);                             //s(n,nT)
			p1.add(vertex);
			vertex = prefixReversal(n, vertex);                              //s(n,nT,n)
			p1.add(vertex);
			vertex = prefixReversal(nT, vertex);                             //s(n,nT,n,nT)
			p1.add(vertex);
			vertex = prefixReversal(n, vertex);                              //s(n,nT,n,nT,n)
			p1.add(vertex);
			String b = prefixReversal(nT, vertex);                           //s(n,nT,n,nT,n,nT) == b
			p1.add(b);
			b = prefixReversal(n, b);                                        //b(n)
			p1.add(b);
			b = prefixReversal(nT, b);                                       //b(n,nT)
			p1.add(b);
			b = prefixReversal(nT-1, b);                                     //b(n,nT,n,nT-1)
			p1.add(b);
			b = prefixReversal(nT-2, b);                                     //b(n,nT,n,nT-1,nT-2)
			p1.add(b);
			b = prefixReversal(nT-1, b);                                     //b(n,nT,n,nT-1,nT-2,nT-1)
			p1.add(b);
			b = prefixReversal(n, b);                                        //b(n,nT,n,nT-1,nT-2,nT-1,n) == a1
			p1.add(b);
			paths.add(p1);                                                   //add this path to collection
			
			for(int i = 2; i <= nT-1; i++){
				//Step 7) Find path pl (lead to d)
				if(i == l){
					System.out.println(l);
					ArrayList<String> pl = new ArrayList<String>();
					vertex = s;                                              //s
					pl.add(vertex);                      
					vertex = prefixReversal(l, vertex);                      //s(l)
					pl.add(vertex);
					vertex = prefixReversal(n, vertex);                      //s(l,n)
					pl.add(vertex);
					vertex = prefixReversal(l,vertex);                       //s(l,n,l)
					pl.add(vertex);
					vertex = prefixReversal(n, vertex);                      //s(l,n,l,n)
					pl.add(vertex);
					vertex = prefixReversal(l, vertex);                      //s(l,n,l,n,l)
					pl.add(vertex);
					vertex = prefixReversal(nT, vertex);                     //s(l,n,l,n,l,nT)
			        pl.add(vertex);
			        vertex = prefixReversal(n, vertex);                      //s(l,n,l,n,l,nT,n) == d
			        pl.add(vertex);
			        paths.add(pl);                                           //add this path to collection
				}else{
					//Step 9) Find (n-2) sub paths to aI
					ArrayList<String> pI = new ArrayList<String>();
					vertex = s;
					pI.add(vertex);                      
					vertex = prefixReversal(i, vertex);                      //s(i)
					pI.add(vertex);
					vertex = prefixReversal(n, vertex);                      //s(i,n)
					pI.add(vertex);
					vertex = prefixReversal(i,vertex);                       //s(i,n,i)
					pI.add(vertex);
					vertex = prefixReversal(n, vertex);                      //s(i,n,i,n)
					pI.add(vertex);
					vertex = prefixReversal(i, vertex);                      //s(i,n,i,n,i)
					pI.add(vertex);
					vertex = prefixReversal(nT, vertex);                     //s(i,n,i,n,i,nT)
			        pI.add(vertex);
			        vertex = prefixReversal(n, vertex);                      //s(l,n,l,n,l,nT,n) == aI
			        pI.add(vertex);
			        paths.add(pI);                                           //add this path to collection
				}
			}
			//Step 10) Find path to aNT
			ArrayList<String> pNT = new ArrayList<String>();
			pNT.add(s);                                                      //s
			pNT.add(prefixReversal(nT, s));                                  //s(nT)
			pNT.add(prefixReversal(n, prefixReversal(nT, s)));               //s(nT,n) == aNT
			paths.add(pNT);                                                  //add this path to collection
			//Step 11&12) Apply PNS tin P(d) and connect to disjoint paths
		}else{
			//Step 2) if s1 == nT:
			if(s.substring(0,1).equals(nT+"")){
				ArrayList<String> p1 = new ArrayList<String>();              
				String vertex = s;                                           //s
				p1.add(vertex);                                              
				vertex = prefixReversal(n, vertex);                          //s(n)
				p1.add(vertex);
				String a = suffixReversal(nT,d);                             //a = (1,2,...nT-1,n,n-1,...,nT)
				p1.addAll(route(vertex,a));                                  //Sub path from s(n) to a
				a = prefixReversal(n, a);                                    //a(n)
				p1.add(a);
				a = prefixReversal(nT, a);                                   //a(n,nT)  == d(n)
				p1.add(a);
				a = prefixReversal(n, a);                                    //a(n,nT,n) == d
				p1.add(a);
				paths.add(p1);                                               //add this path to collection  
			}else{
				int l = s.indexOf(nT)+1;
				ArrayList<String> p1 = new ArrayList<String>();              
				String vertex = s;                                           //s
				p1.add(vertex);
				vertex = prefixReversal(n, vertex);                          //s(n)
				p1.add(vertex);
				vertex = prefixReversal((nT-l)+1, vertex);                   //s(n,nT-l+1)
				p1.add(vertex);
				vertex = prefixReversal(nT, vertex);                         //s(n,nT-l+1,nT)
				p1.add(vertex);
				vertex = prefixReversal(n, vertex);                          //s(n,nT-l+1,nT,n)
				p1.add(vertex);
				vertex = prefixReversal(nT, vertex);                         //s(n,nT-l+1,nT,n,nT)
				p1.add(vertex);
				vertex = prefixReversal(n, vertex);                          //s(n,nT-l+1,nT,n,nT,n)
				//Generate vertex b
				char[] bChars = d.toCharArray();
				char tmp = bChars[n-1];
				bChars[n-1] = bChars[nT-1];
				bChars[nT-1] = tmp;
				String b = new String(bChars);                               //s(n,nT-l+1,nT,n,nT,n) --> b                                        
				p1.addAll(route(vertex,b));                                  //Sub paths from latest vertex to b  
			    b = prefixReversal(n, b);                                    //b(n)
			    p1.add(b);
			    b = prefixReversal(nT, b);                                   //b(n,nT)
			    p1.add(b);
			    b = prefixReversal(nT-1, b);                                 //b(n,nT,nT-1)
				p1.add(b);
				b = prefixReversal(nT-2, b);                                 //b(n,nT,nT-1,nT-2)
				p1.add(b);
				b = prefixReversal(nT-1, b);                                 //b(n,nT,nT-1,nT-2,nT-1)    == d(n)
				p1.add(b);
				b = prefixReversal(n, b);                                    //b(n,nT,nT-1,nT-2,nT-1,n) == d
			    p1.add(b);
				paths.add(p1);                                               //add this path to collection  
			}
			//Step 3) Find (n-2) sub paths to aI
			for(int i = 2; i <= nT-1; i++){
				ArrayList<String> pI = new ArrayList<String>();
				String vertex = s;                                           //s
				pI.add(vertex);                                              
				vertex = prefixReversal(i, vertex);                          //s(i)
				pI.add(vertex);
				vertex = prefixReversal(n, vertex);                          //s(i,n)
				pI.add(vertex);
				vertex = prefixReversal(i, vertex);                          //s(i,n,i)
				pI.add(vertex);
				vertex = prefixReversal(n, vertex);                          //s(i,n,i,n)
				pI.add(vertex);
				vertex = prefixReversal(i, vertex);                          //s(i,n,i,n,i)
				pI.add(vertex);
				vertex = prefixReversal(nT, vertex);                         //s(i,n,i,n,i,nT)
				pI.add(vertex);
				vertex = prefixReversal(n, vertex);                          //s(i,n,i,n,i,nT,n) == aI
				pI.add(vertex);
				paths.add(pI);                                               //add path to collection
			}
			//Step 4) find path to aNT
			ArrayList<String> pNT = new ArrayList<String>();
			pNT.add(s);                                                      //s
			pNT.add(prefixReversal(nT, s));                                  //s(nT)
			pNT.add(prefixReversal(n, prefixReversal(nT, s)));               //s(nT,n)  == aNT
			paths.add(pNT);                                                  //add path to collection
			//Step 5&6) Apply PNS in P(d) and connect to disjoint paths
		}
		
		return paths;
	}
	
	public ArrayList<ArrayList<String>> routeOdd_Otherwise(String s, String d){
        //Step 1) Source Side
		ArrayList<ArrayList<String>> pathS = new ArrayList<ArrayList<String>>();
		//Find (nT-1) paths from s to aI
		for(int i = 2; i < nT; i++){
			ArrayList<String> pI = new ArrayList<String>();
			pI.add(s);
			String vertex = prefixReversal(i, s);                            //s(i)
			pI.add(vertex);
			vertex = prefixReversal(n, vertex);                              //s(i,n) == aI
			pI.add(vertex);
			pathS.add(i, pI);
		}
		//Find path from s to a1
		ArrayList<String> ps1 = new ArrayList<String>();
		ps1.add(s);
		ps1.add(prefixReversal(n, s));                                        //s(n) == a1
		pathS.add(1, ps1);
		//Find path from s to aNT
		ArrayList<String> psNT = new ArrayList<String>();
		psNT.add(s);
		String vertex = prefixReversal(nT, s);                               //s(nT)
		psNT.add(vertex);
		vertex = prefixReversal(3, vertex);                                  //s(nT,3)
		psNT.add(vertex);
		vertex = prefixReversal(2, vertex);                                  //s(nT,3,2)
		psNT.add(vertex);
		vertex = prefixReversal(3, vertex);                                  //s(nT,3,2,3)
		psNT.add(vertex);
		vertex = prefixReversal(nT, vertex);                                 //s(nT,3,2,3,nT)
		psNT.add(vertex);
		vertex = prefixReversal(n, vertex);                                  //s(nT,3,2,3,nT,n) == aNT
		pathS.add(nT, psNT);
		
	    //Step 2) Destination Side
		ArrayList<ArrayList<String>> pathD = new ArrayList<ArrayList<String>>();
		//Find paths from d to bJ
		for(int j = 2; j < nT; j++){
			ArrayList<String> pJ = new ArrayList<String>();
			pJ.add(d);
			vertex = prefixReversal(j, d);                                   //d(j)
			pJ.add(vertex);
			vertex = prefixReversal(n, vertex);                              //d(j,n) == bJ
			pJ.add(vertex);
			pathD.add(j, pJ);
		}
		//Continue to find path from d to b1
		ArrayList<String> pd1 = new ArrayList<String>();
		pd1.add(d);
		pd1.add(prefixReversal(n, d));                                        //d(n) == b1
		pathD.add(1, pd1);
		//Continue to find path from s to aNT
		ArrayList<String> pdNT = new ArrayList<String>();
		pdNT.add(d);
		vertex = prefixReversal(nT, d);                                       //d(nT)
		pdNT.add(vertex);
		vertex = prefixReversal(3, vertex);                                   //d(nT,3)
		pdNT.add(vertex);
		vertex = prefixReversal(2, vertex);                                   //d(nT,3,2)
		pdNT.add(vertex);
		vertex = prefixReversal(3, vertex);                                   //d(nT,3,2,3)
		pdNT.add(vertex);
		vertex = prefixReversal(nT, vertex);                                  //d(nT,3,2,3,nT)
		pdNT.add(vertex);
		vertex = prefixReversal(n, vertex);                                   //d(nT,3,2,3,nT,n) == bNT
		pathD.add(nT, pdNT);
		//Step 3) Construct a bijection from i to j such that B(i) = j if P(aI) = P(bJ)
		for(int i = 1; i <= nT; i++){
			for(int j = 1; j <= nT; j++){
				//Find P(aI) = P(bJ)
				if(pathS.get(i).get(pathS.get(i).size()-1).substring(nT).equals(pathD.get(j).get(pathD.get(j).size()).substring(nT))){
					
				}
			}
		}
		//Step 4) For each pair of nodes aI and bB(i), apply algorithm HR to obtain a path Pi: aI -> bB(i)
		
		return pathS;
	}
	
	//Check if there is a pattern of symbol "123... nT" in source vertex in Case 1-3, 1-4
	public boolean checkArrangePattern(String s,String d){
		//Condition Identifier 
		String checker = d.substring(0,nT);
		//Create a checker string
		String sPattern = s.substring(nT-1);
		//pattern 1 -> (nT-1)
		if(checker.equals(sPattern)){
			return true;
			}
		else{
			//pattern 2++ -> (nT-1)
			String[] ck = new String[nT];
			for(int i = 0; i < nT-1; i++){
				//Collect pattern in an array
		        for(int j = 0; j < nT; j++){
		          if((i+j) < nT){
		            ck[(i+j)] = checker.substring(j,j+1);
		          }
		          else{
		            for(int k = 0; k < i; k++){
		              ck[k] = checker.substring(nT-k-1,nT-k);
		            }
		            break;
		          }
		        }
		        String c = "";
		        //Convert the pattern back to String
		        for(int j = 0; j < nT; j++){
		          c += ck[j];
		        }
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
	
	public boolean checkPattern_1_5(String s, String d){
	    //Condition Identifier 
	    String checker = d.substring(0,nT-1);
	    //Create a checker string
	    String sPattern = s.substring(nT);
	    //pattern 1 -> (nT-1)
	    if(checker.equals(sPattern)){
	      return true;
	    }else{
	      //pattern 2++ -> (nT-1)
	      String[] ck = new String[nT-1];
	      for(int i = 0; i < nT-2; i++){
	        //Collect pattern in an array
	        for(int j = 0; j < nT-1; j++){
	          if((i+j) < nT-1){
	            ck[(i+j)] = checker.substring(j,j+1);
	          }
	          else{
	            for(int k = 0; k < i; k++){
	              ck[k] = checker.substring(nT-k-2,nT-k-1);
	            }
	            break;
	          }
	        }
	        String c = "";
	        //Convert the pattern back to String
	        for(int j = 0; j < nT-1; j++){
	          c += ck[j];
	        }
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
	
	// Case 1-4: (∃k(<nT) such that s(k,n) ∈ P(d) and s(k,n) != d)
	public boolean checkCase1_4(String s){
		  for(int k = 2; k < nT; k++){
			  String sP = prefixReversal(k,s);
			  sP = prefixReversal(n,sP);
			  //Check if sP is in the same pancake graph as d and sP is not d
			  if(sP.substring(nT).equals(d.substring(nT)) && !sP.equals(d)){
				  case4_k = k;
				  return true;
			  }else{
				  continue;
			  }
		  }
		  return false;
	}
	
	// Case 1-5 (s(nT,n) is in P(d) and s(nT,n) != d
	public boolean checkCase1_5(String s){
		String c = prefixReversal(n, prefixReversal(nT, s));         //s(nT,n)
		if(c.substring(nT).equals(d.substring(nT)) && !c.equals(d)){
			return true;
		}else{
			return false;
		}
	}
	
    //This method implements suffix reversal operation used in Case1_4
	public String suffixReversal(int k, String u){
		String s = "";
		for(int i = 0; i < n-k; i++){
			s += u.substring(i,i+1);
		}
		for(int j = n; j > n-k; j--){
			s += u.substring(j-1,j);
		}
		return s;
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
				  if(j>0 && j%10 == 0){
					  System.out.println();
				  }
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
	    ArrayList<String> path = new ArrayList<String>();                     // Declare a collector for collecting vertices in the path
	    for(int i = s.length()-1; i >= 0; i--){                               //for i:=n to 1 step -1
	      if(!(s.substring(i,i+1).equals(d.substring(i,i+1)))){               //if s[i] != d[i] begin...
	        String sR = "";
	        for(int k = 0; k < s.length(); k++){
	          if(s.substring(k,k+1).equals(d.substring(i,i+1))){              //find k such that s[k] = d[i]
	            //System.out.println("k = " + (k+1));                         //For checking k
	            sR = prefixReversal(k+1,s);                                   //sR = PrefixReversal(k,s)
	            if(k > 0){                                                    // if k > 1 
	              path.add(sR);                                               //then add the sR to routes
	            }
	          }
	        }
	        //System.out.println("i = " + (i+1));                             //For checking i
	        s = prefixReversal(i+1,sR);                                         
	        path.add(s);
	      }
	    }
	    return path;
	  }
}
