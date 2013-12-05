import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;



public class TestShortestPath
{
	
	CharacterLibrary graph = new CharacterLibrary("/test_lib.tsv");
	CharacterLibrary graph2 = new CharacterLibrary("/labeled_edges.tsv");
	Character A = new Character("Character A");
	Character Z = new Character("Character Z");
	Character G = new Character("Character G");
	Character J = new Character("Character J");
	Character N = new Character("Character N");
	Character X = new Character("Character X");
	Character test1 = new Character("DR. STRANGE | MUTANT");
	Character test2 = new Character("SUB-MARINER/NAMOR MA");
	
	@Test
	public void isCharacterTest(){
		graph.buildGraph();
		assertTrue(graph.isCharacter(A));
		assertFalse(graph.isCharacter(Z));
		assertTrue(graph.isCharacter("Character A"));
		assertFalse(graph.isCharacter("Character Z"));
	}
	@Test 
	public void testGetLex(){
		ArrayList<String> listA = new ArrayList<String>();
		ArrayList<String> listB = new ArrayList<String>();
		listA.add("A");
		listB.add("A");
		listA.add("B");
		listB.add("B");
		listA.add("C");
		listB.add("D");
		assertTrue(Character.getLex(listA, listB).equals(listA));
		listA.add(0, "B");
		assertTrue(Character.getLex(listA, listB).equals(listB));
	}
	@Test
	public void shortestPathTest1(){
		graph.buildGraph();
		assertEquals(graph.shortestPath("Character A", "Character B").toString(), "[Character A, Book A, Character B]");
		assertEquals(graph.shortestPath("Character A", "Character G").toString(), "[Character A, Book A, Character E, Book B, Character G]");
		assertEquals(graph.shortestPath("Character G", "Character A").toString(), "[Character G, Book B, Character E, Book A, Character A]");
		assertEquals(graph.shortestPath("Character B", "Character N").toString(), "[Character B, Book A, Character A, Book E, Character Q, Book D, Character N]");
		assertEquals(graph.shortestPath("Character B", "Character X").toString(), "[]");
		
	}
	@Test
	public void testMostCentral() throws Exception{
		graph.buildGraph();
		assertEquals(graph.mostCentralCharacter(), "Character A");
	}
	@Test
	public void largeTest() throws Exception{
		graph2.buildGraph();
		assertEquals(graph2.shortestPath("SILVER SURFER/NORRIN", "GRIFFITH, D.W.").toString(), "[SILVER SURFER/NORRIN, A 116, CAPTAIN AMERICA, FF 133, GRIFFITH, D.W.]");
		graph2.addCharacter(X);
		assertEquals(graph2.shortestPath("SILVER SURFER/NORRIN", "Character X").toString(), "[]");
		assertEquals(graph2.shortestPath("Character X", "SILVER SURFER/NORRIN").toString(), "[]");
		assertEquals(graph2.mostCentralCharacter(), "CAPTAIN AMERICA");
	}
	
	
}
