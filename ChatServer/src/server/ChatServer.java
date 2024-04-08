package server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class ChatServer {
	
	
	
	private static int TCP_PORT;
	private static String KEY_STORE_PATH;
	private static String KEY_STORE_PASSWORD;
	private static String MEDIC;
	private static String NO_MEDIC;
	private static String PERSON;
	
	private static BufferedReader in;
	private static PrintWriter out;
	public static Queue<Socket> medicalStaff = new LinkedList<Socket>();

	public static void main(String[] args) {
		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("server.properties"));
			TCP_PORT = Integer.parseInt(prop.getProperty("TCP_PORT"));
			KEY_STORE_PATH = prop.getProperty("KEY_STORE_PATH");
			KEY_STORE_PASSWORD = prop.getProperty("KEY_STORE_PASSWORD");
			PERSON = prop.getProperty("PERSON");
			MEDIC = prop.getProperty("MEDIC");
			NO_MEDIC = prop.getProperty("NO_MEDIC");
			Handler handler = new FileHandler("eror.log");
			Logger.getLogger(ChatServer.class.getName()).addHandler(handler);
			
			System.setProperty("javax.net.ssl.keyStore", KEY_STORE_PATH);
			System.setProperty("javax.net.ssl.keyStorePassword", KEY_STORE_PASSWORD);
			
			SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			ServerSocket ss = ssf.createServerSocket(TCP_PORT);
			
			System.out.println("Server running...");
			while (true) {
				SSLSocket sock = (SSLSocket) ss.accept();
				
				in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

				String firstMessage = in.readLine();
			
				out = new PrintWriter(sock.getOutputStream(), true);

				if(PERSON.equals(firstMessage)) {
					
					if(medicalStaff.isEmpty()) {
								
						out.println(NO_MEDIC);
						
					}else {
					
						Socket medicalSocket = medicalStaff.poll();
					
						new ServerThread(sock,medicalSocket,false).start();
						new ServerThread(medicalSocket,sock,true).start();
						
						out.println(MEDIC);
						
					}
					
				}else if(MEDIC.equals(firstMessage)){
					
					medicalStaff.add(sock);		
				}
			
			}
			
		} catch (IOException e) {
			Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}
	}
	
}
