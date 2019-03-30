package com.za.networking.peertopeer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class ServerThread extends Thread{
	
	
	// A server socket and a set of ServerThreadThreads
	private ServerSocket serverSocket;
	
	private Set<ServerThreadThread> serverThreadThreads = new HashSet<ServerThreadThread>();
	
	public ServerThread(String portNumb) throws NumberFormatException, IOException {
		serverSocket = new ServerSocket(Integer.valueOf(portNumb));
	}
	
	// We have a ServerThreadThread for each one of the peers (we wait to accept incoming peer)
	// Then we add it to our ServerThreadThreads set 
	// then we start that ServerThreadThread
	public void run() {
		try {
			while (true) {
				ServerThreadThread serverThreadThread = new ServerThreadThread(serverSocket.accept(), this);
				serverThreadThreads.add(serverThreadThread);
				serverThreadThread.start();
			}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	
	// Uses print writer to send messages grab each ServerThreadThread that are in this ServerThread's set, and sends a message to them 
	void sendMessage(String message) {
		
		try { serverThreadThreads.forEach(t-> t.getPrintWriter().println(message));
	} catch (Exception e) {e.printStackTrace();} 
		
	}
		public Set<ServerThreadThread> getServerThreadThreads() {return serverThreadThreads;}

}

	