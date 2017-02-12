package app;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import model.cards.*;
import java.net.ServerSocket;

public class Server {
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket = null;
	private static RoomThread roomThread;
	private static int portNumber;
	private static int maxPlayersInRoom;
	
	public static int getPortNumber() {
		return portNumber;
	}
	public static int getMaxPlayersInRoom() {
		return maxPlayersInRoom;
	}

	public static Stack<Card> getDeckOfCards(){
		Deck deck = new Deck();
		Stack<Card> cards = deck.getDeck();
	 	return cards;
	}
	
	public static void main(String args[]) {
		xmlParse();
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
		    	if(i%maxPlayersInRoom==0 || roomThread.getIsGameEnded()){
		    		roomThread = new RoomThread(getDeckOfCards());
		    		roomThread.addClient(0,client);
		    		roomThread.start();
		    		i=0;
		    	}else{
		    		roomThread.addClient(i%maxPlayersInRoom,client);
		    	}
				
				i++;
		      } catch (IOException e) {
		        System.out.println(e);
		      }
		}
	}
	public static void xmlParse(){
		try{
			File inputFile = new File("src/model/network/config.xml");
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(inputFile);
	        doc.getDocumentElement().normalize();
	        NodeList nList = doc.getElementsByTagName("config");
	        for (int temp = 0; temp < nList.getLength(); temp++) {
	           Node nNode = nList.item(temp);
	           if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	              Element eElement = (Element) nNode;
	              portNumber = Integer.parseInt(eElement.getElementsByTagName("port").item(0).getTextContent());
	              maxPlayersInRoom = Integer.parseInt(eElement.getElementsByTagName("maxPlayersInRoom").item(0).getTextContent());
	           }
	        }
	     } catch (Exception e) {
	        e.printStackTrace();
	     }
	}
	
	
}