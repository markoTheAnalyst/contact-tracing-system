package application;

import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

public class ClientProperties {
	
	private static final String CONFIG_PATH = "client.properties";
	private static String HOST;
	private static int PORT;
	private static String KEY_STORE_PATH;
	private static String KEY_STORE_PASSWORD;
	private static String PERSON_EXCHANGE;
	static String BASE_URL;
	static String APP_FXML;
	static String AUTH_FXML;
	static String LOGIN_FXML;
	static String PASS_FXML;
	static String ERROR;
	static String PERSON;
	static String MEDIC;
	static String END;
	static int ROWS;
	static int COLUMNS;
	static String BACKGROUND_COLOR;
	static String BACKGROUND_COLOR2;
	static String PASS;
	static String POLICY;
	
	
	static  
    {  
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(CONFIG_PATH));
			HOST = prop.getProperty("HOST");
			PORT = Integer.parseInt(prop.getProperty("PORT"));
			KEY_STORE_PATH = prop.getProperty("KEY_STORE_PATH");
			KEY_STORE_PASSWORD = prop.getProperty("KEY_STORE_PASSWORD");
			PERSON_EXCHANGE = prop.getProperty("PERSON_EXCHANGE");
			BASE_URL = prop.getProperty("BASE_URL");
			APP_FXML = prop.getProperty("APP_FXML");
			PASS = prop.getProperty("PASS");
			AUTH_FXML = prop.getProperty("AUTH_FXML");
			LOGIN_FXML = prop.getProperty("LOGIN_FXML");
			PASS_FXML = prop.getProperty("PASS_FXML");
			ERROR = prop.getProperty("ERROR");
			PERSON = prop.getProperty("PERSON");
			MEDIC = prop.getProperty("MEDIC");
			END = prop.getProperty("END");
			ROWS = Integer.parseInt(prop.getProperty("ROWS"));
			COLUMNS = Integer.parseInt(prop.getProperty("COLUMNS"));
			BACKGROUND_COLOR = prop.getProperty("BACKGROUND_COLOR");
			BACKGROUND_COLOR2 = prop.getProperty("BACKGROUND_COLOR2");
			POLICY = prop.getProperty("POLICY");
			Handler handler = new FileHandler("error.log");
			Logger.getLogger(ClientProperties.class.getName()).addHandler(handler);
		} catch (IOException e) {
			Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}
    } 
	
	private static int serializationType;
	static String token;
	static String id;
	static String logInTime;
	static long logInMs;
	static ObservableList<Session> sessions;
	static SSLSocket socket;
	static BufferedReader in;
	static PrintWriter out;
	static ApplicationController controller;
	
	
	public static void receiveNotification() {
		try {
			
			System.setProperty("java.security.policy", POLICY);
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
			}
			
			ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost(HOST);
		    Connection connection = factory.newConnection();
		    Channel channel = connection.createChannel();
	
		    channel.exchangeDeclare(PERSON_EXCHANGE, BuiltinExchangeType.DIRECT);
		    String queueName = channel.queueDeclare().getQueue();
		    channel.queueBind(queueName, PERSON_EXCHANGE, id);
	
		    Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
					controller.notify(message);
					
					if(serializationType % 4 == 0) {
						
						FileWriter out = new FileWriter(new File("gson.out"+new Random().nextInt()));
						out.write(new Gson().toJson(message));
						out.close();
						
					}
					
					else if(serializationType % 4 == 1) {
						
						Output out = new Output(new FileOutputStream(new File("kryo.out"+new Random().nextInt())));
						new Kryo().writeClassAndObject(out, message);
						out.close();
						
					}
					
					else if(serializationType % 4 == 2) {
						
						ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("java.out"+new Random().nextInt()));
						out.writeObject(message);
						out.close();
						
					}
					
					else if(serializationType % 4 == 3) {
						
						XMLEncoder encoder = new XMLEncoder(new FileOutputStream(new File("xml.out"+new Random().nextInt())));
						encoder.writeObject(message);
						encoder.close();
						
					}
					
					serializationType++;
	
				}
			};
			channel.basicConsume(queueName, true, consumer);
		} catch( IOException | TimeoutException e) {
			Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}

	}
	public static boolean connect(TextArea chatArea) {
		
		System.setProperty("javax.net.ssl.trustStore", KEY_STORE_PATH);
		System.setProperty("javax.net.ssl.trustStorePassword", KEY_STORE_PASSWORD);
		boolean isConnected = false;
		try {
			SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
			socket = (SSLSocket) sf.createSocket(HOST, PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			out.println(PERSON);
			
			String message = in.readLine();
			isConnected = MEDIC.equals(message);
			if(isConnected)
				new Receiver(in,out,socket, chatArea);
			
		} catch (IOException e) {
			Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}
		return isConnected;
		
	}


}
