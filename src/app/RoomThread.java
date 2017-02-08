package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import model.cards.Card;
import model.cards.CardValue;
import model.cards.Suit;
import model.network.DataFromClient;
import model.network.DataFromServer;

class RoomThread extends Thread {

	private final ArrayList<Client> clients = new ArrayList();
	
	private Stack<Card> cards;
	private ArrayList<Card> cardsOnTable = new ArrayList();
	private int whoseTurn = 0;
	private DataFromServer dataFromServer;
	boolean isGameStarted = false;
	int noOfPenaltyCards = 0;
	
	public boolean getIsGameStarted(){
		return isGameStarted;
	}
	
	public RoomThread(Stack<Card> cards){
		this.cards = cards;
		cardsOnTable.add(cards.pop());
	}
	
	public void addClient(Client client){
		clients.add(client);
		dataFromServer = new DataFromServer(1, cardsOnTable, getStartingCards(), clients.size()-1, whoseTurn);
		try {
			client.getObjectOutputStream().reset();
			client.getObjectOutputStream().writeObject(dataFromServer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private ArrayList<Card> getStartingCards(){
		ArrayList<Card> startingCards = new ArrayList<Card>();
		int i = 0;
		while(i<5){
			Card card = cards.pop();
			card.setClickable(isCardClickable(card));
			startingCards.add(card);
			i++;
		}
		return startingCards;
	}
	private boolean isCardClickable(Card card){
		CardValue cardOnTableValue = cardsOnTable.get(cardsOnTable.size()-1).getCardValue();
		Suit cardOnTableSuit = cardsOnTable.get(cardsOnTable.size()-1).getSuit();

		if((card.getCardValue().equals(cardOnTableValue)) || 
			(card.getSuit().equals(cardOnTableSuit))){
			return true;
		}
		return false;
	}

	
	public void run() {
		while(true){
			
			DataFromClient dataFromClient = readDataFromClient();

			switch(dataFromClient.getPacketId()){
				case 1://Client put cards on Table
					for(Card card : dataFromClient.getClientCards()){
						cardsOnTable.add(card);
					}
					System.out.println("Po³o¿one karty Gracza " + whoseTurn);
					System.out.println(dataFromClient.getClientCards().get(0).getCardValue() +" "+ dataFromClient.getClientCards().get(0).getSuit() );
					whoseTurn = (whoseTurn +1)%(clients.size());
					System.out.println("WhoseTurn2: " + whoseTurn);
					
					if(!checkForSpecialCard(dataFromClient.getClientCards())){
						DataFromServer dataFromServer = new DataFromServer(4, dataFromClient.getClientCards(), whoseTurn);
						for(Client client : clients){
							sendPacket(client,dataFromServer);
						}
					}
					break;
				case 2: //end Turn packet
					int nextTurn = (whoseTurn +1)%(clients.size());
					dataFromServer = new DataFromServer(2, nextTurn);
					sendPacket(clients.get(whoseTurn), dataFromServer);
					whoseTurn = nextTurn;
					sendPacket(clients.get(whoseTurn), dataFromServer);
					break;
				case 3: //take card packet
					ArrayList<Card> newClientCards = new ArrayList();
					for(int i=0;i<5;i++){
					newClientCards.add(cards.pop());
					}
					dataFromServer = new DataFromServer(3, newClientCards);
					sendPacket(clients.get(whoseTurn), dataFromServer);	
					System.out.println("WhoseTurn: " + whoseTurn);
					break;
				case 4: //take panalty cards
					ArrayList<Card> penaltyCards = new ArrayList();
 					for(int i=0; i<noOfPenaltyCards;i++){
 						penaltyCards.add(cards.pop());
 					}
 					dataFromServer = new DataFromServer(8, penaltyCards, (whoseTurn +1)%(clients.size()));
 					noOfPenaltyCards=0;
 					sendPacket(clients.get(whoseTurn), dataFromServer);
 					whoseTurn = (whoseTurn +1)%(clients.size());
 					dataFromServer = new DataFromServer(4, whoseTurn);
 					sendPacket(clients.get(whoseTurn), dataFromServer);
 					break;
			}
			if(clients.size()<1){
				break;
			}
	  }
	}

	private DataFromClient readDataFromClient() {
		Object readObject = null;
		try {
			System.out.println("Czytam------------------------------------");
			readObject = clients.get(whoseTurn).getObjectInputStream().readObject();
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		DataFromClient dataFromClient = (DataFromClient) readObject;
		return dataFromClient;
	}
	
	
	private void sendPacket(Client client, DataFromServer dataFromServer){
		try {
			client.getObjectOutputStream().reset();
			client.getObjectOutputStream().writeObject(dataFromServer);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	private boolean checkForSpecialCard(ArrayList<Card> clientCards) {
		Card card = clientCards.get(clientCards.size()-1);
		boolean flag = false;
		DataFromServer dataFromServer;
		switch(card.getCardValue()){
			case TWO:
				noOfPenaltyCards = 2*clientCards.size();
				dataFromServer = new DataFromServer(5,clientCards, whoseTurn);
				sendPacket(clients.get(whoseTurn), dataFromServer);
				flag = true;
				break;
			case THREE:
				noOfPenaltyCards = 3*clientCards.size();
				dataFromServer = new DataFromServer(6,clientCards, whoseTurn);
				sendPacket(clients.get(whoseTurn), dataFromServer);
				flag = true;
				break;
			case FOUR:
				noOfPenaltyCards = 3*clientCards.size();
				dataFromServer = new DataFromServer(7,clientCards, whoseTurn);
				sendPacket(clients.get(whoseTurn), dataFromServer);
				flag = true;
				break;

		}
		return flag;
		
	}

}