package node2nodeHalfPancakeGraph;

import java.util.Scanner;

/*
 * Author: Pongsatorn Siriwong B.Sc. Computer Science KMUTT
 * version 0.1
 * Algorithm based on Paper: Node-to-Node Disjoint Path Problem in Half-pancake Graphs
 * by Sinyu JUNG, and Keiichi KANEKO lab members TUAT
 * 
 * This class is the main running program
 */

public class Main {
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		//Input the size of the pancake graph (n-Pancake graph)
	    System.out.print("Insert the number of n (size of pancake graph) ");
	    int n = in.nextInt();
	    System.out.println();
	    //Input the source node
	    System.out.println("Please input the source node \n(enter each number seperate by space e.g. '7 6 5 4 3 2 1')");
	    String s = "";
	    for(int i = 0; i < n; i++){
	      s += in.next();
	    }
	    //Declare an object of pancake graph
	    PancakeGraph pk1 = new PancakeGraph(n,s);
	    
	    System.out.println("Source node = " + s);
	    System.out.println("Destination node = " + pk1.getD());
	    
	    HalfPancakeRouting hpk = new HalfPancakeRouting(pk1);
	    hpk.routingOperation();
	    
		in.close();
	}
}