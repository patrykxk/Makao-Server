package app;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Stack;

import model.cards.*;
import model.network.*;

import java.net.ServerSocket;

public class Server {
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;
	private static final ArrayList<ArrayList<ClientThread>> rooms = new ArrayList<ArrayList<ClientThread>>();
	private static ArrayList<ClientThread> threadsInRoom;
	private static Stack<Card> cards;
	
	private static void getPlayingCards(){
		Deck deck = new Deck();
	 	cards = deck.getDeck();
	}
	
	public static void main(String args[]) {
	    int portNumber = 1111;
	    try {
	      serverSocket = new ServerSocket(portNumber);
	    } catch (IOException e) {
	      System.out.println(e);
	    }
	    System.out.println("Server started");
	    int i = 1;
	    threadsInRoom = new ArrayList<ClientThread>();
	    rooms.add(threadsInRoom);
	    getPlayingCards();
	    while (true) {
	      try {
	        clientSocket = serverSocket.accept();
	        if(i>4){
	        	threadsInRoom = new ArrayList<ClientThread>();
	    	    rooms.add(threadsInRoom);
	    	    getPlayingCards();
	        }
	        System.out.println("Connection from: " + clientSocket.getInetAddress());
	        
			ClientThread thread = new ClientThread(clientSocket, threadsInRoom, cards, i);
			thread.start();
			threadsInRoom.add(thread);
			i++;
	      } catch (IOException e) {
	        System.out.println(e);
	      }
	    }
	}
}

class ClientThread extends Thread {

  private Socket clientSocket = null;
  private final ArrayList<ClientThread> threads;
  private Stack<Card> cards;
  private ArrayList<Card> clientCards = new ArrayList<Card>();
  private ArrayList<Card> cardsOnTable = new ArrayList<Card>();
  private ObjectOutputStream objectOutputStream;
  private int whosxeTurn = 1;
  private int clientId;
  
  public ClientThread(Socket clientSocket, ArrayList<ClientThread> threads, Stack<Card> cards, int clientId) {
    this.clientSocket = clientSocket;
    this.threads = threads;    
    this.cards = cards;
    this.clientId = clientId;
    
	cardsOnTable.add(cards.pop());
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

    try {
    	objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

		DataFromServer dataFromServer;
		
		dataFromServer = new DataFromServer(cardsOnTable, getStartingCards(), clientId);

		objectOutputStream.reset();
		objectOutputStream.writeObject(dataFromServer);
		String inputString = "S";
		int j = 2;
		
		
		
	    while (true) {

	    	if (inputString.startsWith("0")) {
	    		break;
	    	}
	
	    }

	      for (int i = 0; i < threads.size(); i++) {
	        if (threads.get(i) == this) {
	        	threads.get(i).interrupt();
	        }
	      }
	
	      clientSocket.close();
    } catch (IOException e) {
    } 
  }

}