package app;
import java.io.IOException;
import java.net.Socket;
import java.util.Stack;

import model.cards.*;
import java.net.ServerSocket;

public class Server {
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;
	private static RoomThread roomThread;
	
	private static Stack<Card> getDeckOfCards(){
		Deck deck = new Deck();
		Stack<Card> cards = deck.getDeck();
	 	return cards;
	}
	
	public static void main(String args[]) {
		int portNumber = 1111;
	    try {
	      serverSocket = new ServerSocket(portNumber);
	    } catch (IOException e) {
	      System.out.println(e);
	    }
	    System.out.println("Server started");
	    int i = 0;
	    
	    roomThread = new RoomThread(getDeckOfCards());
	    
	    while (true) {
	    	try {
		    	if(i>=4 || roomThread.getIsGameStarted()){
		    		roomThread = new RoomThread(getDeckOfCards());
		    		
		    	}
		    	clientSocket = serverSocket.accept();
		        System.out.println("Connection from: " + clientSocket.getInetAddress());
				Client client = new Client(clientSocket);
				
				roomThread.addClient(client);
				if(i%4==0){
					roomThread.start();
				}
				
				i++;
		      } catch (IOException e) {
		        System.out.println(e);
		      }
		}
	}
}