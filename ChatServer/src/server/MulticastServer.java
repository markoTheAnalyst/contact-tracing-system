package server;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MulticastServer {
	
	private static String HOST;
	private static int MC_PORT1;
	private static int MC_PORT2;
	
	public static void main(String[] args) {
		
		MulticastSocket socket = null;
		try {
			Properties prop = new Properties();
	    	prop.load(new FileInputStream("server.properties"));
	    	
	    	MC_PORT1 = Integer.parseInt(prop.getProperty("MC_PORT1"));
	    	MC_PORT2 = Integer.parseInt(prop.getProperty("MC_PORT2"));
			HOST = prop.getProperty("HOST");
			Handler handler = new FileHandler("eror.log");
			Logger.getLogger(MulticastServer.class.getName()).addHandler(handler);
			
			System.out.println("Multicast server pokrenut...");
			
			byte[] buf = new byte[256];
			socket = new MulticastSocket(MC_PORT1);
			InetAddress address = InetAddress.getByName(HOST);
			socket.joinGroup(address);
			
			while (true) {
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				String received = new String(packet.getData(), 0, packet.getLength());
				System.out.println(received);
				buf = received.getBytes();
				packet = new DatagramPacket(buf, buf.length, address, MC_PORT2);
				socket.send(packet);
				buf = new byte[256];
			}
			
		} catch (IOException e) {
			Logger.getLogger(MulticastServer.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}
	}

}
