package friends;

import java.io.*;
import java.util.*;

// Testing client for Friends
public class FriendsApp {

    public static void main (String[] args) {

	if ( args.length < 1 ) {
	    System.out.println("Expecting graph text file as input");
	    return;
	}

	String filename = args[0];
	try {
	    Graph g = new Graph(new Scanner(new File(filename)));

	    // Update p1 and p2 to refer to people on Graph g
	    // sam and sergei are from sample graph
	    String p1 = "sam";
	    String p2 = "ricardo";
	    ArrayList<String> shortestChain = Friends.shortestChain(g, p1, p2);

	    // Testing Friends.shortestChain
	    System.out.println("Shortest chain from " + p1 + " to " + p2);
	    for ( String s : shortestChain ) {
		System.out.println(s);
	    }

		System.out.println("\n\n");
	    
	    // ADD test for Friends.cliques() here
		String school = "cornell";
		ArrayList<ArrayList<String>> cliques = Friends.cliques(g, school);
		System.out.println(cliques.size() + " cliques " + school);
	    
	
		if(cliques != null) {
			for(ArrayList<String> perClique : cliques) {
				System.out.println(perClique.toString());
			}
		}
		
		System.out.println("\n\n");
	    
	    // ADD test for Friends.connectors() here

		ArrayList<String> connectors = Friends.connectors(g);
		System.out.println("There are " + connectors.size() + " connectors ");
		for(String connector : connectors) {
			System.out.println(connector);
		}

		//figure out what members
	} catch (FileNotFoundException e) {

	    System.out.println(filename + " not found");
	}
    }
}
