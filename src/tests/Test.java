package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import app.*;
import model.cards.Card;

public class Test {
	private static Server server = new Server();
	private static Stack<Card> cardsDeck = server.getDeckOfCards();

	@org.junit.Test
	public void testDuplicatesInDeck() {
		Set<Card> treeSet = new  TreeSet(cardsDeck);
		assertTrue(cardsDeck.size()==treeSet.size());
	}
	
	@org.junit.Test
	public void testNumberOfCardsInDeck(){
		assertEquals(52, cardsDeck.size());
	}
	@org.junit.Test
	public void testNumberOfStartingCards(){
		RoomThread roomThread = new RoomThread(cardsDeck);
		ArrayList<Card> startingCards = roomThread.getStartingCards();
		assertEquals(5, startingCards.size());
	}
	
	@org.junit.Test
	public void testXMLparsing(){
		server.xmlParse();
		assertEquals(1111 ,server.getPortNumber());
		assertEquals(4, server.getMaxPlayersInRoom());
	}

}
