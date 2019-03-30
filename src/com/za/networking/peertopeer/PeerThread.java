package com.za.networking.peertopeer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.json.Json;
import javax.json.JsonObject;



public class PeerThread extends Thread{
	
	private BufferedReader bufferedReader;
	
	// constructor takes a socket, and use that socket to get the input stream for its buffered reader
	public PeerThread(Socket socket) throws IOException {
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public void run() {
		boolean flag = true;
		while(flag) {
			try {
				// here we pick up Json messages from other peers and print out their username and the message they sent  
				JsonObject jsonObject = Json.createReader(bufferedReader).readObject();
				if (jsonObject.containsKey("username"))
					System.out.println("["+jsonObject.getString("username")+"]: "+jsonObject.getString("message"));
			} catch (Exception e) {
				flag = false;
				interrupt();
			}
			
		}
	}

}
