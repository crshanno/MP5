import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Character
{
	private String name;
	private HashMap<Character, String> edges = new HashMap<Character, String>();
	private HashMap<Character, Character> paths = new HashMap<Character, Character>(100);
	private boolean isSearched = false;
	
	
	/**
	 * Constructs a new Character with the given name
	 * @param name String representing the name of the Character
	 */
	public Character(String name){
		this.name = name;
	}
	/**
	 * Returns the name of the Character
	 * @return String representing the name of the Character
	 */
	public String getName(){
		return this.name;
	}
	/**
	 * Adds a labeled one way edge from this character to the argument Character,
	 * for a two way edge add a one way edge to both characters
	 * 
	 * @param comic String representing the name of the edge
	 * @param character Character representing the connected node
	 */
	public void addEdge(String comic, Character character){
		
		if(!edges.containsKey(character)){
			edges.put(character, comic);
		}else if(edges.get(character).compareTo(comic) > 0){
			edges.put(character, comic);
		}
	}
	/**
	 * Returns the shortest path to the target character that is no longer than a specific maximum.
	 * For example if you already found a connection 2 deep, it is unnecessary to search beyond that distance next time. 
	 * Recursively calls itself on other characters and remembers the shortest path to a Character for future searches
	 * so as more searches are done on a library performance should improve for the library as a whole.
	 * Initial call should usually be the number of nodes in the graph to be searched.
	 * Returns
	 *
	 * @param character Character representing the connected node
	 * @param max int the maximum number of edges to be traversed.
	 * @return the lexicographically least of the shortest paths connecting the Argument Character to this Character.
	 */
	public ArrayList<String> shortestPath(Character character, int max){
		
		//System.out.println(this.name);
		ArrayList<String> returnList = new ArrayList<String>();
		Character bestCharacter = null;
	
		if(this.edges.size() == 0){
			//System.out.println("No edges");
			return returnList;
		}
		
		if(this.edges.containsKey(character)){
			returnList.add(character.getName());
			returnList.add(edges.get(character));
			returnList.add(this.getName());
			//System.out.println("Found it!");
			return returnList;
		}
		
		if(this.isSearched){
			//System.out.println("Been here.");
			return returnList;
		}
		
		if(max == 0){
			//System.out.println("Gone too far.");
			return returnList;
			
		}
		this.isSearched = true;
		
		if(this.paths.containsKey(character)){
			//System.out.println("Seen this.");
			if(paths.get(character) == null){
				this.isSearched = false;
				//System.out.println("Wrong way");
				return returnList;
			}
			returnList = paths.get(character).shortestPath(character, max -1);
			if(returnList.size() == 0 ){
				this.isSearched = false;
				return returnList;
			}
			returnList.add(edges.get(new Character(returnList.get(returnList.size() - 1))));
			returnList.add(this.getName());
			this.isSearched = false;
			//System.out.println("That way");
			return returnList;
		}
		Set<Character> keySet = edges.keySet();
		Iterator<Character> keys = keySet.iterator();
		int count = 0;
		while(keys.hasNext()){
			//System.out.println("Try the next");
			Character tempCharacter =  keys.next();
			ArrayList<String> temp; 
	
			count ++;
			
			if(returnList.size() == 0){
				returnList = tempCharacter.shortestPath(character, max - 1);
				if(returnList.size() > 0){
					bestCharacter = tempCharacter;
				}
			}else{
				temp = tempCharacter.shortestPath(character, (returnList.size()/2) - 1);
				if(temp.size() > 0){
					if(temp.size() < returnList.size()){
						//System.out.println("Better Choice");
						returnList = temp;
					}else if(temp.size() == returnList.size()){
						//System.out.println("Lex it");
						returnList = getLex(temp, returnList);
					}
					if(returnList.equals(temp)){
						bestCharacter = tempCharacter;
					}
			
				}
			}
		}
		//System.out.println(count);
		paths.put(character, bestCharacter);
		if(!(returnList.size() > 0)){
			this.isSearched = false;
			return returnList;		
		}
		returnList.add(edges.get(new Character(returnList.get(returnList.size() - 1))));
		returnList.add(this.getName());
		this.isSearched = false;
		
		return returnList;
		
	}

	/**
	 *  Returns the HashMap of this Character's edges, edges are the lexicographically least book connecting them
	 * @return HashMap containg Characters mapped to the lexicographically least book connecting them
	 */
	public HashMap<Character, String> getEdges(){
		return edges;
	}
	/**
	 *  Returns lexicographically least of two paths
	 * @return ArrayList containing  the lexicographically least sequence connecting them
	 */
	public static ArrayList<String> getLex(ArrayList<String> temp, ArrayList<String> returnList)
	{
		if(temp.toString().compareTo(returnList.toString()) > 0)
			return returnList;
		else
			return temp;
	}
	/** 
	 * Returns equal if the names of the argument is equal to that of the target
	 * @param the Character to compare against
	 * @return true if the names are equal
	 */
	@Override
	public boolean equals(Object character){
		if(!(character.getClass().equals(this.getClass()))){
			return false;
		}
		else if(this.getName().equals(((Character) character).getName())){
			return true;
		}
		else 
			return false;
	}
	
	/** 
	 * Returns the hashcode of the name
	 * @return the hashcode of the name
	 */
	@Override
	public int hashCode(){
		return name.hashCode();
	}
	
	/** 
	 * Returns the name
	 * @return the name
	 */
	@Override
	public String toString(){
		return this.name;
	}
}
