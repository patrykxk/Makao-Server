package model.network;

import java.io.Serializable;
import java.util.ArrayList;

import model.cards.Card;

public class DataFromServer implements Serializable {
	//Server
	private static final long serialVersionUID = 840010767994965205L;
	private ArrayList<Card> cardsOnTable;
	private ArrayList<Card> clientCards;
	private int whoseTurn;
	private int clientId;
	private int packetId;
	private String request;
	  
	public DataFromServer(int packetNumber, ArrayList<Card> cardsOnTable, ArrayList<Card> newClientCards, int clientId, int whoseTurn) {
		this.packetId = packetNumber;
		this.cardsOnTable = cardsOnTable;
		this.clientCards = newClientCards;
		this.clientId = clientId;
		this.whoseTurn = whoseTurn;
	}
	
	public DataFromServer(int packetNumber, ArrayList<Card> cardsOnTable, int whoseTurn) {
		this.packetId = packetNumber;
		this.cardsOnTable = cardsOnTable;
		this.whoseTurn = whoseTurn;
	}
	
	public DataFromServer(int packetNumber, ArrayList<Card> newClientCards) {
		this.packetId = packetNumber;
		this.clientCards = newClientCards;
	}
	public DataFromServer(int packetNumber, int whoseTurn) {
		this.packetId = packetNumber;
		this.whoseTurn = whoseTurn;
	}
	public DataFromServer(int packetNumber, ArrayList<Card> cardsOnTable, String request, int whoseTurn) {
		this.packetId = packetNumber;
		this.cardsOnTable = cardsOnTable;
		this.request = request;
		this.whoseTurn = whoseTurn;
	}
	public ArrayList<Card> getCardsOnTable(){
		return cardsOnTable;
	}



}
