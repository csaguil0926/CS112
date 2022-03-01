package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		
		TrieNode root = new TrieNode(null,null,null);

		Indexes firstIndexes = new Indexes(0, (short)(0), (short)(allWords[0].length() - 1));

		root.firstChild = new TrieNode(firstIndexes, null, null);

		TrieNode ptr = root.firstChild;
		TrieNode temp = root.firstChild;
		
		int commonUpTo = -1; 
		int startIndex = -1;
		int endIndex = -1; 
		int wordIndex = -1;
	
		for(int i = 1; i < allWords.length; i++) {
			
			String word = allWords[i];
			
			while(ptr != null) {
				
				startIndex = ptr.substr.startIndex;
				endIndex = ptr.substr.endIndex;
				wordIndex = ptr.substr.wordIndex;
				commonUpTo = commonUpTo(allWords[wordIndex].substring(startIndex, endIndex+1), word.substring(startIndex), allWords);	
				
				if(commonUpTo == -1) { //nothing in common
					//go right
					temp = ptr;
					ptr = ptr.sibling;
				}
				else if (commonUpTo == endIndex){ //everything in common
					//go down
					commonUpTo += startIndex;
					temp = ptr;
					ptr = ptr.firstChild;
				} 
				else if (commonUpTo < endIndex){ //some matches
					commonUpTo += startIndex;
					temp = ptr;
					break;
				}
			}
			
			if(ptr == null) {
				//or add node
				Indexes indexes = new Indexes(i, (short)startIndex, (short)(word.length()-1));
				temp.sibling = new TrieNode(indexes, null, null);
			} else {
				//store first child in temp
				TrieNode temp1 = temp.firstChild;
				
				//update common
				Indexes restOldWord = new Indexes(temp.substr.wordIndex, (short)(commonUpTo+1), temp.substr.endIndex);
				temp.substr.endIndex = (short)commonUpTo; 
				
				//set first child to rest of old word
				temp.firstChild = new TrieNode(restOldWord, null, null);
				//set fc sibling to rest of new word
				temp.firstChild.sibling = new TrieNode(new Indexes((short)i, (short)(commonUpTo+1), (short)(word.length()-1)), null, null);
				//set fc's fc to temp
				temp.firstChild.firstChild = temp1;
			}
			
			//reset for next word
			ptr = root.firstChild;
			temp = root.firstChild;
		
			commonUpTo = -1; 
			startIndex = -1;
			endIndex = -1; 
			wordIndex = -1;
		}
		
		return root;
	}

	private static int commonUpTo(String first, String second, String[] allWords) {
		
		int commonUpToIndex = 0;
		int minLength = 0;

		if (first.length()==second.length()){
			minLength = first.length();
		} else if (first.length()<second.length()){
			minLength = first.length();
		} else if (second.length()<first.length()){
			minLength = second.length();
		}
		
		for (int i = 0; i < minLength; i++) {
			
			if (first.charAt(0) != second.charAt(0)) {
				return -1;
			}
			
			if (first.charAt(i) == second.charAt(i)) {
				commonUpToIndex++;
			}
		}
		return (commonUpToIndex-1);
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/

		TrieNode ptr = root.firstChild;

		boolean repeat = true;
		boolean endingsFound = false;

		while (repeat){

			String word=allWords[ptr.substr.wordIndex].substring(ptr.substr.startIndex, ptr.substr.endIndex + 1);
			
			int commonUpTo = commonUpTo(word, prefix, allWords);

			if (commonUpTo == -1){ //no match
				ptr=ptr.sibling; //go right
			} else if (word.startsWith(prefix)) { //prefix is in word (some match)
				repeat = false; //stop while loop
				endingsFound = true; //found matches to add to endings	
			} else if (prefix.startsWith(word)){ //word is in prefix (complete match)
				ptr = ptr.firstChild; //go down
				prefix = prefix.substring(ptr.substr.startIndex); //update prefix
			}
		}

		if (endingsFound == true){ 
			return(leafNodes(ptr));
		} else { //if prefix has no match at all
			return null;
		}


	}

	private static ArrayList<TrieNode> leafNodes(TrieNode ptr){

		ArrayList<TrieNode> endings = new ArrayList<TrieNode>();

		if (ptr.firstChild == null){ //base case
			endings.add(ptr);
		}else{
			ptr = ptr.firstChild; 
			while(ptr!=null){ 
				endings.addAll(leafNodes(ptr)); //recursion bbbyyyyy
				ptr=ptr.sibling;
			}
		}

		return endings;
	}
		
	
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex].substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
