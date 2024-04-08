package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class MulticastThread extends Thread{
	
	TextArea chatArea;
	MulticastSocket mcSocket;
	
	MulticastThread(MulticastSocket mcSocket, TextArea chatArea){
		this.chatArea = chatArea;
		this.mcSocket = mcSocket;
		setDaemon(true);
	}
	
	public void run() {
		
		byte[] buffer = new byte[256];
		
		while(true) {
        	
        	DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        	try {
				mcSocket.receive(packet);
				String received = new String(packet.getData(), 0, packet.getLength());
	            String toDisplay = received + System.lineSeparator();
				Platform.runLater(() -> {
					chatArea.appendText("multicast: " + toDisplay);
	            });
			} catch (IOException e) {
				Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
			}
            
        }
        
        
	}
}
