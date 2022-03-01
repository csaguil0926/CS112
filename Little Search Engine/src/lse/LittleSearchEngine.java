package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		
		//temp hashtable
		HashMap <String, Occurrence> temp = new HashMap<String, Occurrence>();

		//scan doc
		Scanner sc = new Scanner (new File(docFile));

		while (sc.hasNext()){
			
			//get word
			String word = getKeyword(sc.next());

			//if word is not noise word or not all alphabetical char
			if (word != null){
				
				//check to see if it's a new word and if it is put it into hashtable
				if (temp.containsKey(word)==false){
					Occurrence occurrence = new Occurrence(docFile,1);
					temp.put(word, occurrence);
				}
				//else if it's another occurence increment frequency
				else{
					temp.get(word).frequency++;
				}
			}
		}
		//return temp hashtable of loaded key words
		return temp;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/

		//go through <key,value> pairs
		for (String k: kws.keySet()){

			//if the master hashtable doesn't have this key
			if (keywordsIndex.containsKey(k)==false){
				//add key and array list of occurences as <key,value> pair
				ArrayList<Occurrence> occurrence = new ArrayList<Occurrence>();
				occurrence.add(kws.get(k));
				keywordsIndex.put(k,occurrence);
			//if this key is already in hashtable
			} else{
				//add occurence to end of array list
				keywordsIndex.get(k).add(kws.get(k));
				//update it to be sorted in descending order
				insertLastOccurrence(keywordsIndex.get(k));
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/

		//get rid of punctuation
		for(int i = word.length()-1; i>=0; i--){
			if (word.charAt(i)=='.'||word.charAt(i)==','||word.charAt(i)=='?'||word.charAt(i)==':'||word.charAt(i)==';'||word.charAt(i)=='!'){
				word = word.substring(0,i);
			}
		}

		//check if characters are all alphabetical
		for (int i=0; i<=word.length()-1; i++){
			if (Character.isLetter(word.charAt(i))==false){
				return null;
			}
		}

		//make the word lowercase
		word = word.toLowerCase();

		//check if word is a noise word
		if (noiseWords.contains(word)){
			return null;
		}

		//if passes all the test returns word
		return word;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		
		//create an array list to store the sequence of midpoints checked by binary search
		ArrayList<Integer> midpoints = new ArrayList<Integer>();

		//check if size of input list is 1
		if (occs.size() == 1){
			return null;
		}

		Occurrence temp = occs.get(occs.size()-1);
		occs.remove(occs.size()-1);

		int hi = 0;
		int lo = occs.size()-1;
		int mid = 0;

		while (hi<=lo){

			//get mid from formula
			mid = lo + (hi-lo) / 2;

			//add midpoint to arraylist
			midpoints.add(mid);
			
			//binary search y'all
			if (temp.frequency == occs.get(mid).frequency){
				break;
			} else if (temp.frequency > occs.get(mid).frequency){
				lo=mid-1;
			} else {
				hi=mid+1;
				mid++;
			}
		}

		//update input list
		occs.add(mid,temp);
		return midpoints;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		
		//array list to store top5matches
		ArrayList<String> top5matches = new ArrayList<String>();

		//get list of occurrences
		ArrayList<Occurrence> occskw1 = keywordsIndex.get(kw1);
		ArrayList<Occurrence> occskw2 = keywordsIndex.get(kw2);

		//both strings are found
		if (keywordsIndex.containsKey(kw1)&&keywordsIndex.containsKey(kw2)){

			//created list with all occs
			ArrayList<Occurrence> mergedOccs = new ArrayList<Occurrence>();
			mergedOccs.addAll(occskw1);
			mergedOccs.addAll(occskw2);

			//go through all occs and add only top 5
			for(int i = 0; i < 5 && !mergedOccs.isEmpty(); i++){

				int ptr = 0;
				int prev = -1;

				//actually going through occs here
				for( ptr = 0; ptr < mergedOccs.size() && mergedOccs.get(ptr) != null; ptr++){
					//beginning
					if (prev == -1){
						//check for duplicates
						if (top5matches.contains(mergedOccs.get(ptr).document)== false){
							prev = ptr; //update
						}
					//if doc at ptr has more occs than prev doc
					} else if (mergedOccs.get(ptr).frequency > mergedOccs.get(prev).frequency){
						//check for duplicates
						if(top5matches.contains(mergedOccs.get(ptr).document)==false){
							prev = ptr; //update
						}
					//if doc at ptr and prev doc have the same number of occs
					} else if (mergedOccs.get(ptr).frequency == mergedOccs.get(prev).frequency){
						if(keywordsIndex.get(kw1).contains(mergedOccs.get(ptr))){
							if(top5matches.contains(mergedOccs.get(ptr).document)==false){
								prev = ptr;
							}
						}
					}
				}
				
				if (prev != -1){ //if prev is in bounds
					top5matches.add(mergedOccs.remove(prev).document);//add it to top5 and remove from merged occs
				}
			}
		//kw1 is found
		} else if (keywordsIndex.containsKey(kw1)&&keywordsIndex.containsKey(kw2)==false){
			//add top 5 occs or whole list if its less than 5
			for(int i = 0; i < 5 && i<occskw1.size() ; i++){
				top5matches.add(occskw1.get(i).document);
			}
		//kw2 is found
		} else if (keywordsIndex.containsKey(kw1)&&keywordsIndex.containsKey(kw2)==false){
			//add top 5 occs or whole list if its less than 5
			for(int i = 0; i < 5 && i<occskw2.size(); i++){
				top5matches.add(occskw2.get(i).document);
			}
		//no matches
		}else{
			return null;
		}

		return top5matches;
	
	}
}
