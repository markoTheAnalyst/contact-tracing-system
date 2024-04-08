package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class Receiver extends Thread {

	private BufferedReader in;
	private PrintWriter out;
	private Socket sock;
	TextArea chatArea;
	
	
	Receiver(BufferedReader in, PrintWriter out, Socket sock, TextArea chatArea){
		
		this.in = in; 
		this.out = out;
		this.sock = sock;
		this.chatArea = chatArea;
		start();
		
	}
	
	public void run() {
		
		while(true) {
			
			try {
				String message = in.readLine();
				
				if(ClientProperties.END.equals(message)) {
					
					out.println(ClientProperties.END);	
					in.close();
					out.close();
					sock.close();
					
					ApplicationController.connect = !ClientProperties.connect(chatArea);
					
					break;
				
				}
				String toDisplay = message + System.lineSeparator();
				
				Platform.runLater(() -> {
					chatArea.appendText(ClientProperties.MEDIC+": " + toDisplay);
	            });
				
				
				
			} catch (IOException e) {
				Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
			}	
		}
		
	}
}
