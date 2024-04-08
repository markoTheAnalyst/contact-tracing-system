package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import server.IFIleServer;


public class ApplicationController {

    @FXML
    private MenuBar menu;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField chatBox;

    @FXML
    private JFXButton downloadBttn;
    
    @FXML
    private JFXButton sendBttn;

    @FXML
    private TextArea mcChatArea;

    @FXML
    private TextField mcChatBox;

    @FXML
    private JFXButton mcSendBttn;

    @FXML
    private JFXTextField tokenField;

    @FXML
    private JFXRadioButton radioBttn1;

    @FXML
    private ToggleGroup group1;
    
    @FXML
    private ListView<String> notificationList;

    @FXML
    private JFXRadioButton radioBttn2;

    @FXML
    private JFXRadioButton radioBttn3;

    @FXML
    private JFXButton markBttn;

    @FXML
    private JFXButton blockBttn;

    @FXML
    private JFXButton unblockBttn;
    
    @FXML
    private JFXButton tokensBttn;
    
    @FXML
    private JFXButton showBttn;
    
    private ListView<String> view = new ListView<>();
    
    boolean connect = true;
    boolean multicastConnect = true;
    byte[] buf = new byte[256];
	
    @FXML
    void block() {
    	
    	new Thread(() -> {
    	
	    	try {
		    	String token = tokenField.getText();
		    	if(token.isBlank())
		    		throw new IllegalArgumentException();
		    	
		    	for(String activeToken : view.getItems()) {
		    		if(activeToken.startsWith(token)) {
		    			
		    			RESTClient.putRequest("block/"+activeToken,"true");
		    			return;
		    		}
		    	}
		    	throw new IllegalArgumentException();
		    	
	    	} catch(IllegalArgumentException e) {
	    		Platform.runLater(() -> {
	    			alert("Token ne postoji!");
	    		});
	    	}
    	}).start();

    }
    
    @FXML
    void unblock() {
    	
    	new Thread(() -> {
        	
	    	try {
		    	String token = tokenField.getText();
		    	if(token.isBlank())
		    		throw new IllegalArgumentException();
		    	
		    	for(String activeToken : view.getItems()) {
		    		if(activeToken.startsWith(token)) {
		    			
		    			RESTClient.putRequest("block/"+activeToken,"false");
		    			return;
		    		}
		    	}
		    	throw new IllegalArgumentException();
		    	
	    	} catch(IllegalArgumentException  e) {
	    		Platform.runLater(() -> {
	    			alert("Token ne postoji!");
	    		});
	    	}
    	}).start();

    }
    
    @FXML
    void showMap() {
    	
    	String token = tokenField.getText();
    	if(token.isBlank()) {
    		alert("Token ne postoji!");
    		return;
    	}
    	
    	
    	for(String activeToken : view.getItems()) {
    		if(activeToken.startsWith(token)) {			
    			new Thread(() -> {
    		    	
    		        String locations = RESTClient.getRequest("locations/"+activeToken, "application/json");
    		        
    		        if(locations.isBlank()) {
    		    		return;
    		    	}
    				JSONObject json = new JSONObject(locations);

    				GridPane root = new GridPane();
    		        root.setGridLinesVisible(true);
    		        final int numCols = ClientProperties.COLUMNS ;
    		        final int numRows = ClientProperties.ROWS ;
    		        for (int i = 0; i < numCols; i++) {
    		            ColumnConstraints colConst = new ColumnConstraints();
    		            colConst.setPercentWidth(100.0 / numCols);
    		            root.getColumnConstraints().add(colConst);
    		            
    		        }
    		        for (int i = 0; i < numRows; i++) {
    		            RowConstraints rowConst = new RowConstraints();
    		            rowConst.setPercentHeight(100.0 / numRows);
    		            root.getRowConstraints().add(rowConst);         
    		        }
    				
    				for(String key : json.keySet()){
    					
    					String[] coordinates = json.getString(key).split("@");
    					Pane pane = new Pane();
    					
    					pane.setStyle(ClientProperties.BACKGROUND_COLOR);
    					root.add(pane, Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[0]));
    				}
    				
    		        Platform.runLater(() -> {
    			        Stage primaryStage = new Stage();
    			        primaryStage.setScene(new Scene(root, 800, 600));
    			        primaryStage.show();
    		        });
    	    	}).start();
    			return;
    		}
    	}

    	alert("Token ne postoji!");
    	

    }
    
    void notify(String notification) {
    	
    	notificationList.getItems().add(notification);
    }

    
    void activateChat() {
    	
    	
    	radioBttn3.setSelected(true);
    	
    	ClientProperties.joinGroup();
    	ClientProperties.connect();
    	
    	new Receiver(ClientProperties.in,chatArea);
    	new MulticastThread(ClientProperties.mcSocket, mcChatArea).start();
    	
    	
    }
    
    @FXML
    void send() {
    	
    	String message = chatBox.getText();
    	chatBox.clear();
    	chatArea.appendText(message + System.lineSeparator());
    	ClientProperties.out.println(message);
    	
    }
    
    @FXML
    void download() {
    	String token = tokenField.getText();
    	if(token.isBlank()) {
    		alert("Token ne postoji!");
    		return;
    	}
    	
    	for(String activeToken : view.getItems()) {
    		if(activeToken.startsWith(token)) {
    			
    			new Thread(() -> {
    				String name = "FileServer";
    				try {
    					Registry registry = LocateRegistry.getRegistry(1099);
						IFIleServer fileServer = (IFIleServer) registry.lookup(name);
						HashMap<String,byte[]> files = fileServer.download(activeToken);
						for(String file : files.keySet()) {
							File folder = new File(activeToken);		
							folder.mkdir();
								
							try (FileOutputStream stream = new FileOutputStream(activeToken + File.separator + file)) {
								stream.write(files.get(file));
							} 
						}
					} catch (IOException | NotBoundException e) {
						Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
					}
    				
    			}).start();
    			return;
    		}
    	}
    	alert("Token ne postoji!");

    }
    
    @FXML
    void sendMulticast() {
    	
    	
    	String message = mcChatBox.getText();
    	mcChatBox.clear();
    	
    	buf = message.getBytes();
    	
		try {
			DatagramPacket packet = new DatagramPacket(buf, buf.length, 
					InetAddress.getByName(ClientProperties.HOST), ClientProperties.MC_SERVER_PORT);
			ClientProperties.mcSocket.send(packet);
		} catch (IOException e) {
			Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}
    }
    
    @FXML
    void listTokens() {
    	
    	new Thread(()->
    	{
    		String tokens = RESTClient.getRequest("", "text/plain");
    		
    		Platform.runLater(() -> {
				
				view.getItems().clear();
				view.getItems().addAll(tokens.replaceAll("[\\[\\] ]", "").split(","));
				Scene scene = view.getScene();
				if(scene == null)
					scene = new Scene(view);
				Stage primaryStage = new Stage();
				primaryStage.setScene(scene);
				primaryStage.show();
			});
    	
    	}).start();
    }
    
    @FXML
    void mark() {
    	
    	new Thread(() -> {
	    	try {
		    	String token = tokenField.getText();
		    	if(token.isBlank())
		    		throw new IllegalArgumentException();
		    	JFXRadioButton selectedRadioButton = (JFXRadioButton) group1.getSelectedToggle();
		    	String toogleGroupValue = selectedRadioButton.getAccessibleText();
		    	
		    	for(String activeToken : view.getItems()) {
		    		if(activeToken.startsWith(token)) {
		    			
		    			RESTClient.putRequest("mark/"+activeToken, toogleGroupValue);
		    			return;
		    		}
		    	}
		    	throw new IllegalArgumentException();
	    	} catch(IllegalArgumentException  e) {
	    		Platform.runLater(() -> {
			    	alert("Token ne postoji!");
	    		});
	    	} 
    	}).start();

    }
    
    void alert(String alertMessage) {
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setHeaderText(null);
    	alert.setContentText(alertMessage);
    	alert.showAndWait();
    }
}