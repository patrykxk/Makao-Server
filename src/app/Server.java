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
	    		clientSocket = serverSocket.accept();
		        System.out.println("Connection from: " + clientSocket.getInetAddress());
				Client client = new Client(clientSocket);
				
		    	if(i%4==0 || roomThread.getIsGameStarted()){
		    		System.out.println("Tworzê nowy obiekt w¹tku");
		    		roomThread = new RoomThread(getDeckOfCards());
		    		roomThread.addClient(client);
		    		roomThread.start();
		    	}else{
		    		roomThread.addClient(client);
		    	}
				
				i++;
		      } catch (IOException e) {
		        System.out.println(e);
		      }
		}
	}
}