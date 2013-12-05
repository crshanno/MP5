import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;




public class CharacterLibrary{
	   
    private String		address,
    					result,
    					filename,
    					centralCharacter = "";
    
    private ArrayList<String> 	characters = new  ArrayList<String>(),
    			 		        books = new  ArrayList<String>();
    
    String[]	pairs;
    
    Boolean		isInitialized,
    			centralChanged = true;
    
    HashMap<String, Character> library;
    
    Random random = new Random(System.currentTimeMillis());
	
	/**
	 * @param args
	 */
	
	/**
	 * Constructor, builds a library with the given file name
	 * 
	 * @param The file extension of the file you want to test,
	 * starting from the project folder.
	 */
	public CharacterLibrary(String filename){
		this.filename = filename;
	}
	/**
	 * Parses the library from a filename
	 * 
	 * @param The file extension of the file you want to test,
	 * starting from the project folder. 
	 * @return boolean returns true if the initialization was successful
	 * 
	 */
	private boolean initialize(String filename){
		 File file = new File(".");
		 boolean initialized;
		 
         try{
         	address = file.getCanonicalPath() + filename;  
         	result = readFile(address);
         	pairs = result.split("\\n");
         	
         	for(int index = 0; index < pairs.length; index ++){
         		String[] temp = pairs[index].split("\\t");
         		if((temp[0].trim().length() > 2)){
         			String character = temp[0].trim().substring(1, temp[0].trim().length() - 1);
	         		characters.add(character);
	         		String book = temp[1].trim().substring(1, temp[1].trim().length() - 1);
	         		books.add(book);
	         		//System.out.println(character);
	         		//System.out.println(book);
         		}
         	}	
         	
         	initialized = true;
         }
         catch (Exception e){ 
        	System.out.println(e.toString());
         	
         	initialized =  false;
         }
         this.isInitialized = initialized;
         return initialized;
    }
	public void addCharacter(Character character){
		this.library.put(character.getName(), character);
	}
	/**
	 * Builds the library from the stored filename, this creates a library of characters 
	 * with interconnected edges.
	 * 
	 */
	public void buildGraph(){
		centralChanged = true;
		this.initialize(filename);
		if(isInitialized){
			library = new HashMap<String, Character>();
			int mainIndex = 0;
			while(true){
				ArrayList<Character> connectedChars = new ArrayList<Character>();
				String book = books.get(mainIndex);
				int bookIndex = mainIndex;
				while(( bookIndex != books.size())&&book.equals(books.get(bookIndex))){
					String temp = characters.get(bookIndex);
					if(!this.isCharacter(temp)){
						this.library.put(temp, new Character(temp));
					}
					connectedChars.add(library.get(temp));
					bookIndex ++;
					
				}
				if(connectedChars.size() > 1){
					for(int index = 0; index < connectedChars.size(); index ++){
						for(int index2 = index + 1; index2 < connectedChars.size(); index2 ++){
							connectedChars.get(index).addEdge(book, connectedChars.get(index2));
							connectedChars.get(index2).addEdge(book, connectedChars.get(index));
						}
					}
				}
				mainIndex = bookIndex;
				if(( mainIndex == books.size()))
					break;
			}
		}
		else{
			System.out.println("Library not initialized");
		}
		System.out.println("Library contains " + library.size() + " characters.");
	}
	 /**
     * Returns an input stream to read from the given address.
     * Works with normal file names.
     * @param The address of the file to be read
     * @return The contents of the file in string form
     * 
     */
    public static String readFile(String address) throws IOException {
    	// open file
    	InputStream stream = new FileInputStream(address);   
        // read each letter into a buffer
        StringBuffer buffer = new StringBuffer();
        while (true) {
            int ch = stream.read();
            if (ch < 0) {
                break;
            }

            buffer.append((char) ch);
        }
        
        return buffer.toString();
    }    
    /**
     * Returns a random Character from the Library
     * @return a random character from the library
     * 
     */
    public String randChar(){
    	String[] characters = this.library.keySet().toArray(new String[1]);
    	
    	return characters[Math.abs(random.nextInt()%library.size())];
    }
    /**
     * The total number of edges through out the Library.
     * @return int the number of edges throughout the library. Counts 2-way edges as 2 separate edges.
     */
    public int numEdges(){
    	int edgeNum = 0;
    	if(!this.isInitialized){
    		return 0;
    	}else{
    		Iterator<Character> characters = this.library.values().iterator();
    		while(characters.hasNext()){
    			edgeNum += characters.next().getEdges().size();
    		}
    	}
    	return edgeNum;
    }
    /**
     * Main method acts as an interface to interact with the program
     * @param args
     */
    public static void main(String[] args){
    	
    	Scanner console = new Scanner(System.in);
    	
    	System.out.println("Enter test for test library");
    	String test = console.nextLine().trim();
    	String build = "/labeled_edges.tsv";
    	if(test.equals("test"))
    		build = "/test_lib.tsv";
    	
    	
    	long buildStart = System.currentTimeMillis();
    	System.out.println("Initializing Library");
    	CharacterLibrary catalogue = new CharacterLibrary(build);
    	long buildEnd = System.currentTimeMillis();
    	System.out.println("Building Graph");
    	long graphStart = System.currentTimeMillis();
    	catalogue.buildGraph();
    	System.out.println("Graph Built");
        long graphEnd = System.currentTimeMillis();
        System.out.println("Build time:" + (buildEnd - buildStart) + " milliseconds");
        System.out.println("Graph time:" + (graphEnd - graphStart) + " milliseconds");
    	System.out.println(catalogue.numEdges());
    	while(true){
    		try{
	    		System.out.println("\n\nType s for the shortest path between characters, type c for most central character, type r for two random characters:");
	    		
	    		String choice = console.nextLine().trim().toLowerCase();
	    		if(choice.equals("s")){
	    			
	    			String character1 = null;
	    			String character2 = null;
	    			do{
	    				System.out.println("Please enter the first Character Name:");
	    				character1 = console.nextLine().trim();
	    			}while(!catalogue.isCharacter(character1));
	    			catalogue.library.get(character1).getEdges();
	    			
	    			
	    			do{
	    				System.out.println("Please enter the second Character Name:");
	    				character2 = console.nextLine().trim();
		    		}while(!catalogue.isCharacter(character2));
	    			
	    			catalogue.library.get(character2).getEdges();
	    			console.nextLine().trim();
					long pathStart = System.currentTimeMillis();
					ArrayList<String> path = catalogue.shortestPath(character1, character2);
		            long pathEnd = System.currentTimeMillis();
		            if(path != (null)){
			            System.out.print("Path length is: ");
			            System.out.println(path.size()/2);
			            System.out.println(path.toString());
		            }else {
		            	System.out.println("No Connection");
		            }
		            	
		           
		            System.out.println(" " + (pathEnd - pathStart) + " milliseconds");
	    			
	    			
	    			
	    		}else if(choice.equals("c")){
	        	
	                long startTime = System.currentTimeMillis();
	                String central = catalogue.mostCentralCharacter();
	                long endTime = System.currentTimeMillis();
	                System.out.print("\n\nMost Central Character is:");
	                System.out.println(central);
	                System.out.println(" " + (endTime - startTime) + " milliseconds");
	    		}else if(choice.equals("r")){
	    			
	    			long pathStart = System.currentTimeMillis();
	    			String character1 =catalogue.randChar();
	    			String character2 =catalogue.randChar();
	    			System.out.println(character1);
	    			System.out.println(character2);
					ArrayList<String> path = catalogue.shortestPath(character1, character2);
		            long pathEnd = System.currentTimeMillis();
		            if(path != (null)){
			            System.out.print("Path length is: ");
			            System.out.println(path.size()/2);
			            System.out.println(path.toString());
		            }else {
		            	System.out.println("No Connection");
		            }
		            System.out.println(" " + (pathEnd - pathStart) + " milliseconds");
	    		}
    		}
	    	catch(Exception e){
    			System.out.println(e.toString());
    		}			
    	}  	
    }
    /**
     * Returns true if there is a Character in the library with the given name
     * @param string name of the character to be searched for
     * @return true if a character of that name exists in the library
     */
    public boolean isCharacter(String character){
    	return this.library.containsKey(character);
    }
    /**
     * Returns true if the given Character is in the library
     * @param Character to be searched for
     * @return true if the character exists in the library
     */
    public boolean isCharacter(Character character){
    	return this.library.containsValue(character);
    }
    /**
     * Returns the shortest path between the given characters
     * @param String, name of first Character to be searched for
     * @param String, name of second Character to be searched for
     * @return the trace list between the two characters
     */
	public ArrayList<String> shortestPath(String character1, String character2){
		//System.out.println(library.get(character1).getEdges());
		//System.out.println(library.get(character2).getEdges());
		return library.get(character2).shortestPath(library.get(character1), library.size());
	}
	/**
     * Returns the name of the most central character, defined as the character with the least average distance to all other characters
     * @throw Exception if the library is not initialized
     * @return the trace list between the two characters
     */
	public String mostCentralCharacter() throws Exception{
		Double smallest = new Double(library.size());
		
		int percent = 0;
		
		if(!this.isInitialized){
			throw new Exception("Library not initialized");
		}
		if(!this.centralChanged){
			return this.centralCharacter;
		}
		int[][] shortestPath = new int[this.library.size()][this.library.size()];
		String[] keySet = this.library.keySet().toArray(new String[1]);
		
		
		for(int index = 0; index < this.library.size(); index ++){
			if((library.size() > 100)&&(index%(library.size()/100) == 0)&&(percent < 100)){
				percent += 1;
				System.out.println(percent + "% Complete");
			}
			for(int index2 = index + 1; index2 < this.library.size(); index2 ++){
				shortestPath[index][index2] = (this.shortestPath(keySet[index], keySet[index2]).size())/2;
				//System.out.print(shortestPath[index][index2] + " ");
			}
			//System.out.println();
			
		}
		
		for(int index = 0; index < this.library.size(); index ++){
			
			Double total = new Double(0);
			for(int index2 = 0; index2 < index; index2 ++){
					if(shortestPath[index][index2] > 0){
						total += new Double(shortestPath[index][index2]);
					}else{
						total += library.size();
					}
						
			}
			for(int index3 = index; index3 < this.library.size(); index3 ++){
				if(shortestPath[index3][index] > 0){
					total += new Double(shortestPath[index3][index]);
				}else{
					total += library.size();
				}
				
			}
			System.out.println(total + " | " + smallest);
			if(total/((double)this.library.size()) < smallest){
				smallest = (double)total/(double)this.library.size();
				
				this.centralCharacter = keySet[index];
			}
			if((total == smallest)&&(centralCharacter.compareTo(keySet[index]) < 0)){
				this.centralCharacter = keySet[index];
			}
		}
		this.centralChanged = false;
    	return centralCharacter;
    }
}
