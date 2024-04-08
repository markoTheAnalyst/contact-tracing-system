package application;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ClientProperties {
	
	private static final String CONFIG_PATH = "medic.properties";
	public static String END;
	public static int MC_PORT;
	public static String HOST;
	public static String LOCALHOST;
	public static int MC_SERVER_PORT;
	public static int SECURE_PORT;
	private static String KEY_STORE_PATH;
	private static String KEY_STORE_PASSWORD;
	private static String MEDIC_EXCHANGE;
	public static String BASE_URL;
	public static String APP_FXML;
	public static String MEDIC;
	public static int ROWS;
	public static int COLUMNS;
	public static String BACKGROUND_COLOR;
	static String POLICY;
	
	static  
    {  
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(CONFIG_PATH));
			HOST = prop.getProperty("HOST");
			LOCALHOST = prop.getProperty("LOCALHOST");
			MC_PORT = Integer.parseInt(prop.getProperty("MC_PORT"));
			SECURE_PORT = Integer.parseInt(prop.getProperty("SECURE_PORT"));
			MC_SERVER_PORT = Integer.parseInt(prop.getProperty("MC_SERVER_PORT"));
			KEY_STORE_PATH = prop.getProperty("KEY_STORE_PATH");
			KEY_STORE_PASSWORD = prop.getProperty("KEY_STORE_PASSWORD");
			MEDIC_EXCHANGE = prop.getProperty("MEDIC_EXCHANGE");
			BASE_URL = prop.getProperty("BASE_URL");
			APP_FXML = prop.getProperty("APP_FXML");
			MEDIC = prop.getProperty("MEDIC");
			END = prop.getProperty("END");
			ROWS = Integer.parseInt(prop.getProperty("ROWS"));
			COLUMNS = Integer.parseInt(prop.getProperty("COLUMNS"));
			BACKGROUND_COLOR = prop.getProperty("BACKGROUND_COLOR");
			POLICY = prop.getProperty("POLICY");
			Handler handler = new FileHandler("error.log");
			Logger.getLogger(ClientProperties.class.getName()).addHandler(handler);
		} catch (IOException e) {
			Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}
    } 
	
	static SSLSocket socket;
	static BufferedReader in;
	static PrintWriter out;
	static ApplicationController controller;
	
	
	static MulticastSocket mcSocket;
        
	public static void joinGroup() {
		
		try {
			mcSocket = new MulticastSocket(MC_PORT);
			InetAddress address = InetAddress.getByName(HOST);
			mcSocket.joinGroup(address);
		} catch (IOException e) {
			Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}
		
				
	}
	public static void connect() {
		
		System.setProperty("javax.net.ssl.trustStore", KEY_STORE_PATH);
		System.setProperty("javax.net.ssl.trustStorePassword", KEY_STORE_PASSWORD);
		System.setProperty("java.security.policy",POLICY);
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		try {
			
			SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
			socket = (SSLSocket) sf.createSocket(LOCALHOST, SECURE_PORT);
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			out.println(MEDIC);
			
			
			
			
		} catch (IOException e) {
			Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}
		
	}
	
	public static void receiveNotification() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost(LOCALHOST);
		    Connection connection = factory.newConnection();
		    Channel channel = connection.createChannel();
	
		    channel.exchangeDeclare(MEDIC_EXCHANGE, BuiltinExchangeType.FANOUT);
		    String queueName = channel.queueDeclare().getQueue();
		    channel.queueBind(queueName, MEDIC_EXCHANGE, "");
	
		    Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					controller.notify(message);
	
				}
			};
			channel.basicConsume(queueName, true, consumer);
		} catch( IOException | TimeoutException e) {
			Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}

	}


}
