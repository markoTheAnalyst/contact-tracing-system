package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerThread extends Thread {

	private Socket sock1;
	private boolean exit = false;
	private boolean sock1Med;
	
	private BufferedReader in;
	private PrintWriter out;
	private String END;
	
	ServerThread(Socket sock1, Socket sock2, boolean flag){
		
		this.sock1 = sock1;
		this.sock1Med = flag;
		
		try {
			
			in = new BufferedReader(new InputStreamReader(sock1.getInputStream()));
			out = new PrintWriter(sock2.getOutputStream(), true);
			Properties prop = new Properties();
			prop.load(new FileInputStream("server.properties"));
			END = prop.getProperty("END");
			
		} catch (IOException e) {
			Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}
	}
	
	
	public void run() {
		
		String message;
		
		while(!exit) {
			
			try {
				
				message = in.readLine();
				
				if(END.equals(message)){
					exit = true;
					
					if(sock1Med) {
						
						ChatServer.medicalStaff.add(sock1);
							
					}
				} 
			
				out.println(message);
				
				
			} catch (IOException e) {
				
				Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, e.getClass().getName());
			}		
		}	
	}
}