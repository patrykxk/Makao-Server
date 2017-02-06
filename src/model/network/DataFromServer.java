package model.network;

import java.io.Serializable;
import java.util.ArrayList;

import model.cards.Card;

public class DataFromServer implements Serializable {
	//Server
	private static final long serialVersionUID = 840010767994965205L;
	private ArrayList<Card> cardsOnTable;
	private ArrayList<Card> clientCards = new ArrayList<Card>();
	int whoseTurn;
	  
	public DataFromServer(ArrayList<Card> cardsOnTable, ArrayList<Card> newClientCards, int whoseTurn) {
		this.cardsOnTable = cardsOnTable;
		this.clientCards = newClientCards;
		this.whoseTurn = whoseTurn;
	}



}
