package app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


class Client {

	private Socket clientSocket = null;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	private String login;
	
	public Client(Socket clientSocket) {
		this.clientSocket = clientSocket; 
		try {
			objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public ObjectOutputStream getObjectOutputStream() {
		return objectOutputStream;
	}

	public ObjectInputStream getObjectInputStream() {
		return objectInputStream;
	}
	public void setLogin(String login){
		this.login = login;
	}
	public String getLogin(){
		return login;
	}

}