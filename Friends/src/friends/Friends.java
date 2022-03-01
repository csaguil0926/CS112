package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */

	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {

		ArrayList<String> shortestPath = new ArrayList<String>();
		
		//lowercase
		p1 = p1.toLowerCase();
		p2 = p2.toLowerCase();

		//if same just return name
		if (p1==p2){
			shortestPath.add(p1);
			return shortestPath;
		}

		//bfs
		boolean[] marked = new boolean[g.members.length];
		Person[] edgeTo = new Person[g.members.length];

		Queue<Person> q = new Queue<Person>();

		int startIndex = g.map.get(p1);
		q.enqueue(g.members[startIndex]);
		marked[startIndex]=true;

		while(!q.isEmpty()){

			Person v = q.dequeue();

			for (Friend w = v.first; w!=null; w=w.next){

				if(!marked[w.fnum]){
					q.enqueue(g.members[w.fnum]);
					marked[w.fnum]=true;
					edgeTo[w.fnum]= v;
					
					//add to array list
					if (g.members[w.fnum].name.equals(p2)){
						v = g.members[w.fnum];
						
						while (v.name.equals(p1) == false){
							shortestPath.add(0, v.name);
							v = edgeTo[g.map.get(v.name)];
						}
						shortestPath.add(0, p1);
					}
				}
			}
		}

		return shortestPath;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {

		//lowercase
		school = school.toLowerCase();

		ArrayList<ArrayList<String>> listOfCliques = new ArrayList<>(); //all cliques

		boolean[] marked = new boolean[g.members.length];
		
		for(int i = 0; i < g.members.length; i++) {
			
			Person p = g.members[i];
			
			if(marked[i] || !p.student){ //don't go over null(school) or marked
				continue;
			}
			
			ArrayList<String> cliqueMembers = new ArrayList<>(); //individual school cliques
			DFS1(g, school, marked, cliqueMembers, i); //get them by dfs
			
			if(cliqueMembers != null && !cliqueMembers.isEmpty()){ //don't add empty cliques
				listOfCliques.add(cliqueMembers);
			}		
		}
		return listOfCliques;	
	}

	private static void DFS1(Graph g, String school, boolean[] marked, ArrayList<String> cliqueMembers, int index){

		//just normal dfs but i add clique members to list

		Person v = g.members[index];

		if(!marked[index] && v.student && v.school.equals(school))
			cliqueMembers.add(v.name);

		marked[g.map.get(v.name)] = true;

		for (Friend w = v.first; w!=null; w=w.next){

			if(!marked[w.fnum] && g.members[w.fnum].student && g.members[w.fnum].school.equals(school)){
				DFS1(g, school, marked, cliqueMembers, w.fnum); 
			}
		}
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		ArrayList<String> connectors = new ArrayList<>();

		int counter = 1;

		boolean[] marked = new boolean[g.members.length];
		boolean[] check = new boolean[g.members.length];
		int[] dfsnum = new int[g.members.length];
		int[] back = new int[g.members.length];

		for(int i = 0; i < g.members.length; i++) {
			DFS2(g, marked, check, connectors, true, dfsnum, back, counter, i);
		}

		return connectors;
	}

	private static void DFS2(Graph g, boolean[] marked, boolean[] check, ArrayList<String> connectors, boolean startingPoint, int[] dfsnum, int[] back, int counter, int index){
	
		Person v = g.members[index];
	
		marked[index] = true;
		dfsnum[index] = counter;
		back[index] = counter;

		for (Friend w = v.first; w!=null; w=w.next){

			if(!marked[w.fnum]){

				DFS2(g, marked, check, connectors, false, dfsnum, back, counter=counter+1, w.fnum);

				if (dfsnum[index]>back[w.fnum]){
					back[index] = Math.min(back[index], back[w.fnum]);
				}
				
				if (dfsnum[index]<=back[w.fnum]){
					if(!startingPoint){
						if(!connectors.contains(v.name)){
							connectors.add(v.name);
						}
					} else {
						if(!check[index]){
							check[index]=true;
						}else{
							if(!connectors.contains(v.name)){
								connectors.add(v.name);
							}
						}
					}
				}
			} else {
				//already visited
				back[index] = Math.min(back[index], dfsnum[w.fnum]);
			}
		}

			
	}
}

