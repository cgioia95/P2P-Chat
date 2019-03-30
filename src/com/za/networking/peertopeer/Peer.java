package com.za.networking.peertopeer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;

import javax.json.Json;

public class Peer {

	public static void main(String[] args) throws Exception {
		
		
		// Peer takes user input, to declare username & port number 
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("> enter username & port # for this peer: ");
		String[] setUpValues = bufferedReader.readLine().split(" ");
		
		// Server Thread established on this port number, that Peer's Server thread then begins to run 
		ServerThread serverThread = new ServerThread(setUpValues[1]);
		
		serverThread.start();
		
		new Peer().updateListenToPeers(bufferedReader, setUpValues[0], serverThread);

	}
	
	// Updates the peers that this peer is taking messages from
	// Accepts the username we chose as well as the instantiated serverThread
	public void updateListenToPeers(BufferedReader bufferedReader, String username, ServerThread serverThread) throws Exception{
		
		// Requests user input the peers it wants to receive messages from, collected into the inputValues string array
		System.out.println("> enter (space seperated) hostname:port#");
		System.out.println("  peers to receive messages messages from (s to skip) ");
		String input = bufferedReader.readLine();
		String[] inputValues = input.split(" ");
		
		
		// cycles through this string array, if the only input is just s, dont go through this process of finding other ports
		// if no s, then cycle through the inputVales of address:port combos
		// for every address"port combo, grab its values and put in an address string array, then initialize a null socket
		// attempt to setup this socket with the address and port, then feed this socket into a peer thread and start the thread
		// if an exception occurs, check if the socket the socket exists, close it down, otherwise state invalid output 
		if(!input.equals("s")) for (int i=0; i < inputValues.length; i++) {
			String[] address = inputValues[i].split(":");
			Socket socket = null;
			try {
				// new peer thread for each peer we're li
				socket = new Socket(address[0], Integer.valueOf(address[1]));
				new PeerThread(socket).start();
			} catch (Exception e) {
				if (socket != null) socket.close();
				else System.out.println("invalid output. skipping to next step");
			}
		}
		
		communicate(bufferedReader, username, serverThread);
		
	}
	
	
	// can e to exit or c to called updateListenToPeers, changing peers its listening to 
	// if doesnt enter either, then we create json object containing a our username and message that we previously picked up (for e,c or other)
	// we send this json message to the specified serverThread 
	public void communicate(BufferedReader bufferedReader, String username, ServerThread serverThread) {
		try {
			System.out.println("> you can now communicate (e to exit, c to change)");
			boolean flag = true; 
			while(flag) {
				String message = bufferedReader.readLine();
				if(message.equals("e")) {
					flag = false;
					break;
				}
				
				else if (message.equals("c")) {
					updateListenToPeers(bufferedReader, username, serverThread);
				} else {
					StringWriter stringWriter = new StringWriter();
					Json.createWriter(stringWriter).writeObject(Json.createObjectBuilder()
							.add("username", username)
							.add("message", message)
							.build());
					
					
					
					serverThread.sendMessage(stringWriter.toString());
				}
			}
			
			System.exit(0);
		} catch (Exception e) {} 
	}

}
