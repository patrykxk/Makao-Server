package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import model.cards.Card;
import model.cards.CardValue;
import model.cards.Suit;
import model.network.DataFromClient;
import model.network.DataFromServer;

public class RoomThread extends Thread {

	//private final Map<int,Client> clients = new Map();
	private final Map<Integer, Client> clients = new HashMap();
	
	private Stack<Card> cardsDeck;
	private ArrayList<Card> cardsOnTable = new ArrayList();
	private DataFromServer dataFromServer;
	private boolean isGameEnded = false;
	private int noOfPenaltyCards = 0;
	private int whoseTurn = 0;
	private Object[] keyList;
	private ArrayList<Card> globalClientCards = new ArrayList();
	
	public RoomThread(Stack<Card> cards){
		this.cardsDeck = cards;
		cardsOnTable.add(cards.pop());
	}
	
	public void run() {
		while(!Thread.currentThread().isInterrupted()){
			System.out.println("--------------------------------------------");
			System.out.println("karty na stole: ");
			for(Card card : cardsOnTable){
				System.out.println(card.getCardValue() + " of " + card.getSuit());			
			}
			System.out.println("--------------------------------------------");
			DataFromClient dataFromClient = readDataFromClient();
			int packetId = 0;
			if(dataFromClient!=null){
				packetId = dataFromClient.getPacketId();
			}
			switch(packetId){
				case 1://Client put cards on Table
					System.out.println("Clients hashmap size: "+ clients.size());
					for(Card card : dataFromClient.getClientCards()){
						cardsOnTable.add(card);
					}
					System.out.println("Po³o¿one karty Gracza " + whoseTurn);
					System.out.println(dataFromClient.getClientCards().get(0).getCardValue() +" "+ dataFromClient.getClientCards().get(0).getSuit() );

					int check = checkForSpecialCard(dataFromClient.getClientCards());
					
					if(check!=(-1)){
						System.out.println("check: -1");
						whoseTurn = (whoseTurn +1)%(clients.size());
					}
					if(check==1){
						System.out.println("check: 1");
						dataFromServer = new DataFromServer(4, dataFromClient.getClientCards(), (Integer)keyList[whoseTurn]);
						for(Client client : clients.values()){
							System.out.println("pakiet poszed³ do wszzystkich prócz: " + keyList[whoseTurn]);
							if(!client.equals(clients.get(keyList[whoseTurn]))){
								sendPacket(client,dataFromServer);
							}	
						}
					}else if(check==0){
						dataFromServer = new DataFromServer(4, dataFromClient.getClientCards(), (Integer)keyList[whoseTurn]);	
						sendPacketToEveryone(dataFromServer);
					}
					break;
				case 2: //end Turn packet
					int nextTurn = (whoseTurn +1)%(clients.size());
					dataFromServer = new DataFromServer(2, nextTurn);
					sendPacket(clients.get(keyList[whoseTurn]), dataFromServer);
					whoseTurn = nextTurn;
					sendPacket(clients.get(keyList[whoseTurn]), dataFromServer);
					break;
				case 3: //take card packet
					ArrayList<Card> newClientCards = new ArrayList();
					//newClientCards.add(cards.pop());
					Card card;
					int j =0;		
					//do{
					 	if(cardsDeck.isEmpty()){
					 		addCardsFromTableToDeck();
					 	}
					 	card = cardsDeck.pop();
					 	newClientCards.add(card);
					 	j++;
					//}while(j<10);
					 //}while(!(card.getCardValue().toString().equals("JACK")));
					 
					dataFromServer = new DataFromServer(3, newClientCards);
					sendPacket(clients.get(keyList[whoseTurn]), dataFromServer);	
					System.out.println("WhoseTurn: " + keyList[whoseTurn]);
					break;
				case 4: //take panalty cards
					ArrayList<Card> penaltyCards = new ArrayList();
 					for(int i=0; i<noOfPenaltyCards;i++){
 						if(cardsDeck.isEmpty()){
					 		addCardsFromTableToDeck();
					 	}
 						card = cardsDeck.pop();
 						penaltyCards.add(card);
 						cardsOnTable.add(card);
 					}
 					dataFromServer = new DataFromServer(8, penaltyCards, (Integer)keyList[(whoseTurn +1)%(clients.size())]);
 					noOfPenaltyCards=0;
 					sendPacket(clients.get(keyList[whoseTurn]), dataFromServer);
 					whoseTurn = (whoseTurn +1)%(clients.size());
 					dataFromServer = new DataFromServer(4, (Integer)keyList[whoseTurn]);
 					sendPacket(clients.get(keyList[whoseTurn]), dataFromServer);
 					break;
				case 5:
					whoseTurn = (whoseTurn +1)%(clients.size());
					dataFromServer = new DataFromServer(11, globalClientCards,dataFromClient.getRequest(), (Integer)keyList[whoseTurn]);
					sendPacketToEveryone(dataFromServer);
					globalClientCards.clear();
					break;
				case 6:
					whoseTurn = (whoseTurn +1)%(clients.size());
					dataFromServer = new DataFromServer(12, globalClientCards,dataFromClient.getRequest(), (Integer)keyList[whoseTurn]);
					sendPacketToEveryone(dataFromServer);
					globalClientCards.clear();
					break;
				
			}
			if(clients.isEmpty()){
				System.out.println("Zamykam pokój");
				setIsGameEnded(true);
				this.interrupt();
			}
	  }
	}

	private void sendPacketToEveryone(DataFromServer dataFromServer) {
		for(Client client : clients.values()){
			sendPacket(client,dataFromServer);
		}
	}

	private DataFromClient readDataFromClient() {
		Object readObject = null;
		while(!clients.isEmpty()){
			try {
				System.out.println("----------------------------------------------------------------------");
				//System.out.println("Czytam od gracza "+ keyList[whoseTurn]);
				readObject = clients.get(keyList[whoseTurn]).getObjectInputStream().readObject();
				DataFromClient dataFromClient = (DataFromClient) readObject;
				return dataFromClient;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException x) {
				System.out.println("Socket exception");
				System.out.println("U¿ytkownik " + whoseTurn + " opuœci³ stó³");
				clients.remove(keyList[whoseTurn]);
				keyList = clients.keySet().toArray();
				System.out.println("Clients hashmap size: "+ clients.size());
				
				if(!clients.isEmpty()){
					//whoseTurn = (whoseTurn +1)%(clients.size());
					if(whoseTurn==clients.size()){
						whoseTurn-=1;
					}
					System.out.println("Teraz ruch gracza: " + keyList[whoseTurn]);
					dataFromServer = new DataFromServer(4, (Integer)keyList[whoseTurn]);
					sendPacketToEveryone(dataFromServer);
				}
				
				
			}
		}
		return (DataFromClient) readObject;
	}
	
	
	private void sendPacket(Client client, DataFromServer dataFromServer){
		try {
			client.getObjectOutputStream().reset();
			client.getObjectOutputStream().writeObject(dataFromServer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int checkForSpecialCard(ArrayList<Card> clientCards) {
		Card card = clientCards.get(clientCards.size()-1);
		int flag = 0;
		DataFromServer dataFromServer;
		switch(card.getCardValue()){
			case TWO:
				noOfPenaltyCards += 2;
				dataFromServer = new DataFromServer(5,clientCards, (Integer)keyList[(whoseTurn +1)%(clients.size())] );
				sendPacket(clients.get(keyList[(whoseTurn +1)%(clients.size())]), dataFromServer);
				flag = 1;
				break;
			case THREE:
				noOfPenaltyCards += 3;
				dataFromServer = new DataFromServer(6,clientCards, (Integer)keyList[(whoseTurn +1)%(clients.size())]);
				sendPacket(clients.get(keyList[(whoseTurn +1)%(clients.size())]), dataFromServer);
				flag = 1;
				break;
			case FOUR:
				dataFromServer = new DataFromServer(7,clientCards, (Integer)keyList[(whoseTurn +1)%(clients.size())]);
				sendPacket(clients.get(keyList[(whoseTurn +1)%(clients.size())]), dataFromServer);
				flag = 1;
				break;
			case ACE:
				globalClientCards = clientCards;
				System.out.println("przyszed³ as " + globalClientCards.get(0).getCardValue()+ " " + globalClientCards.get(0).getSuit() );
				dataFromServer = new DataFromServer(10,clientCards, (Integer)keyList[whoseTurn]);
				System.out.println("AS "+keyList[whoseTurn]);
				sendPacket(clients.get(keyList[whoseTurn]), dataFromServer);
				flag = -1;
				break;
			case JACK:
				globalClientCards = clientCards;
				System.out.println("przyszed³ jopek " + globalClientCards.get(0).getCardValue()+ " " + globalClientCards.get(0).getSuit() );
				dataFromServer = new DataFromServer(9,clientCards, (Integer)keyList[whoseTurn]);
				sendPacket(clients.get(keyList[whoseTurn]), dataFromServer);
				flag = -1;
				break;
		}
		return flag;
		
	}
	
	private void addCardsFromTableToDeck(){
		System.out.println("wywo³a³o " + cardsOnTable.size());
		for(int i=0;i<(cardsOnTable.size()-3);i++){
			System.out.println("dodaje");
			cardsDeck.add(cardsOnTable.get(i));	
		}
		System.out.println("koniec");
		Collections.shuffle(cardsDeck);
	}
	public boolean getIsGameEnded(){
		return isGameEnded;
	}
	public void setIsGameEnded(boolean isGameEnded){
		this.isGameEnded = isGameEnded;
	}
	public void addClient(int clientId, Client client){
		clients.put(clientId, client);
		keyList = clients.keySet().toArray();
		
		System.out.println("clients size: " + clients.size());
		dataFromServer = new DataFromServer(1, cardsOnTable, getStartingCards(), clientId, whoseTurn);
		try {
			client.getObjectOutputStream().reset();
			client.getObjectOutputStream().writeObject(dataFromServer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<Card> getStartingCards(){
		ArrayList<Card> startingCards = new ArrayList<Card>();
		int i = 0;
		while(i<5){
			Card card = cardsDeck.pop();
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

}