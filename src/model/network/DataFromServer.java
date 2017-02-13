package model.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import model.cards.Card;

public class DataFromServer implements Serializable {
	//Server
	private static final long serialVersionUID = 840010767994965205L;
	private ArrayList<Card> cardsOnTable;
	private ArrayList<Card> clientCards;
	private Map<Integer, String> clientsLogins;
	private int whoseTurn;
	private int clientId;
	private int packetId;
	private String string;
	  
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
	
	public DataFromServer(int packetNumber, String string) {
		this.packetId = packetNumber;
		this.string = string;
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
		this.string = request;
		this.whoseTurn = whoseTurn;
	}
	public DataFromServer(int packetNumber, ArrayList<Card> cardsOnTable, ArrayList<Card> newClientCards, int clientId, Map<Integer, String> clientsLogins, int whoseTurn) {
		this.packetId = packetNumber;
		this.cardsOnTable = cardsOnTable;
		this.clientCards = newClientCards;
		this.clientId = clientId;
		this.clientsLogins = clientsLogins;
		this.whoseTurn = whoseTurn;
	}
	
	public DataFromServer(int packetNumber, Map<Integer, String> clientsLogins, int whoseTurn) {
		this.packetId = packetNumber;
		this.clientsLogins = clientsLogins;
		this.whoseTurn = whoseTurn;
	}

	public ArrayList<Card> getCardsOnTable(){
		return cardsOnTable;
	}



}
