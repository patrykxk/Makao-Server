package model.network;

import java.io.Serializable;
import java.util.ArrayList;

import model.cards.Card;

public class DataFromClient implements Serializable{
	
	//Server
	private static final long serialVersionUID = 4069786321538302229L;
	int packetId;
	private ArrayList<Card> clientCards;
	private String request;
	
	public int getPacketId() {
		return packetId;
	}

	public void setPacketId(int packetId) {
		this.packetId = packetId;
	}

	public ArrayList<Card> getClientCards() {
		return clientCards;
	}

	public void setClientCards(ArrayList<Card> clientCards) {
		this.clientCards = clientCards;
	}
	
	public String getRequest(){
		return request;
	}
	
}
