package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;

import org.json.JSONObject;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import server.IFIleServer;
import token.TokenServerServiceLocator;

public class ApplicationController {

    @FXML
    private MenuBar menu;
    
    @FXML
    private JFXListView<String> notificationBar;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField chatBox;

    @FXML
    private JFXButton sendBttn;
    
    @FXML
    private JFXTextField fieldX;

    @FXML
    public JFXTextField fieldY;
    
    @FXML
    private JFXButton uploadBttn;

    @FXML
    private JFXTextField startField;

    @FXML
    private JFXTextField endField;

    @FXML
    private JFXButton submitBttn;

    public ArrayList<String> events = new ArrayList<>();
    static boolean connect = true;
    boolean isConnected;
    public static final String BASE_URL = ClientProperties.BASE_URL;
    
    
    
    @FXML
    void changePassword() {
    	
    	Parent pane;
		try {
			pane = (Parent) FXMLLoader.load(getClass().getResource(ClientProperties.PASS_FXML));
			Stage stage = new Stage();
	        stage.setScene(new Scene(pane));  
	        stage.show();
		} catch (IOException e) {
			Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}

    }
    
    void notify(String notification) {
    	events.add(notification);
    	notificationBar.getItems().add("Potencijalno ste se zarazili!");
    }
    @FXML
    void showNotification() {
    	
    	GridPane root = new GridPane();
        root.setGridLinesVisible(true);
        final int numCols = ClientProperties.COLUMNS;
        final int numRows = ClientProperties.ROWS;
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
        
        for(String event : events){
			
			String[] coordinates = event.split("_");
			Pane pane1 = new Pane(); 		
			pane1.setStyle(ClientProperties.BACKGROUND_COLOR);
			root.add(pane1, Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[0]));
			
			Pane pane2 = new Pane(); 		
			pane2.setStyle(ClientProperties.BACKGROUND_COLOR2);
			root.add(pane2, Integer.parseInt(coordinates[3]), Integer.parseInt(coordinates[2]));
		}
        
        Stage primaryStage = new Stage();
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
     

    }


    @FXML
    void localLogOut() {
    	
    	String logOutTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
    	long  logOutMs = System.currentTimeMillis();
    	long timeDifference = logOutMs - ClientProperties.logInMs;
    	ClientProperties.sessions.add(new Session(ClientProperties.logInTime, logOutTime, timeDifference/1000));
    	
		try {
			Parent pane = (Parent) FXMLLoader.load(getClass().getResource(ClientProperties.LOGIN_FXML));
			Stage stage = new Stage();
	        stage.setScene(new Scene(pane));  
	        stage.show();
	        
	        menu.getScene().getWindow().hide();
		} catch (IOException e) {
			Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}     
    }

    @FXML
    void registryLogOut() {
    	
    	String logOutTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
    	long  logOutMs = System.currentTimeMillis();
    	long timeDifference = logOutMs - ClientProperties.logInMs;
    	ClientProperties.sessions.add(new Session(ClientProperties.logInTime, logOutTime, timeDifference/1000));
    	
    	try {
			Parent pane = (Parent) FXMLLoader.load(getClass().getResource(ClientProperties.AUTH_FXML));
			Stage stage = new Stage();
	        stage.setScene(new Scene(pane));  
	        stage.show();
	        
	        menu.getScene().getWindow().hide();
	        new TokenServerServiceLocator().getTokenServer().logOut(ClientProperties.token);
		} catch (IOException | ServiceException e) {
			Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
		} 

    }
    
    @FXML
    void upload() {
    	String name = "FileServer";
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry(1099);
			IFIleServer fileServer = (IFIleServer) registry.lookup(name);
	    	FileChooser fileChooser = new FileChooser();
	    	List<File> files = fileChooser.showOpenMultipleDialog(null);
	    	if(files == null)
	    		return;
	    	new Thread(() -> {
	    		int i = 0;
		        for(File file : files) {
		        	
		        	try {
		        		if(i == 5)
		        			return;
						fileServer.upload(Files.readAllBytes(Paths.get(file.getAbsolutePath())), file.getName(), ClientProperties.token);
						i++;
		        	} catch (IOException e) {
		        		Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
					}
		        }
	    	}).start();

		} catch (NotBoundException | IOException e) {
			Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}
		

    }
    
    @SuppressWarnings("unchecked")
	@FXML
    void showActivity() {
    	
    	
    	TableView<Session> tableView = new TableView<>();

        TableColumn<Session, String> column1 = new TableColumn<>("Vrijeme prijave");
        column1.setCellValueFactory(new PropertyValueFactory<>("logInTime"));
        column1.setMinWidth(200);

        TableColumn<Session, String> column2 = new TableColumn<>("Vrijeme odjave");
        column2.setCellValueFactory(new PropertyValueFactory<>("logOutTime"));
        column2.setMinWidth(200);
        
        TableColumn<Session, Long> column3 = new TableColumn<>("Aktivno vrijeme");
        column3.setCellValueFactory(new PropertyValueFactory<>("activeTime"));
        column3.setMinWidth(200);
        
        tableView.getColumns().addAll(column1,column2,column3);
        tableView.setItems(ClientProperties.sessions);
        

        VBox vbox = new VBox(tableView);

        Scene scene = new Scene(vbox);
        
        Stage primaryStage = new Stage();

        primaryStage.setScene(scene);

        primaryStage.show();

    }

    @FXML
    void send() {
    	
    	if(connect) {
    		isConnected = ClientProperties.connect(chatArea);
    		connect = !isConnected;
    	}
    	if(isConnected) {
	    	String message = chatBox.getText();
	    	chatBox.clear();
	    	chatArea.appendText(message + System.lineSeparator());
	    	ClientProperties.out.println(message);
    	}
    }
    
    @FXML
    void show() {
    	
    	new Thread(() -> {
	    	GridPane root = new GridPane();
	        root.setGridLinesVisible(true);
	        final int numCols = ClientProperties.COLUMNS;
	        final int numRows = ClientProperties.ROWS;
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
	     
	        try {
				URL url = new URL(BASE_URL + "locations/" + ClientProperties.token);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
	
				if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
					Platform.runLater(() -> {
						alert("Blokirani ste");
					});
					return;
				}
	
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	
				String output;
				StringBuilder sb = new StringBuilder();
				while ((output = br.readLine()) != null) {
					sb.append(output);
				}
				JSONObject json = new JSONObject(sb.toString());
	
				
				for(String key : json.keySet()){
					
					String[] coordinates = json.getString(key).split("@");
					Pane pane = new Pane();
	        		
					pane.setStyle(ClientProperties.BACKGROUND_COLOR);
	    			root.add(pane, Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[0]));
				}
	
				conn.disconnect();
			} catch (IOException e) {
				Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
			}
	        Platform.runLater(() -> {
		        Stage primaryStage = new Stage();
		        primaryStage.setScene(new Scene(root, 800, 600));
		        primaryStage.show();
	        });
    	}).start();
    }
    
    @FXML
    void submit() {
    	
    	try {
    		Integer.parseInt(fieldY.getText());
    		Integer.parseInt(fieldX.getText());
    		int start = Integer.parseInt(startField.getText());
    		int end = Integer.parseInt(endField.getText());
    		if(start >= end)
    			throw new NumberFormatException();
    		
    		String location = fieldX.getText() + "@" + fieldY.getText()
			  + "$" +startField.getText() + "@" + endField.getText();
    		
    		fieldX.clear();
			fieldY.clear();
			startField.clear();
			endField.clear();
    		
    		new Thread(() -> {
	    		
    			try {
    				URL url = new URL(BASE_URL +"location/"+ ClientProperties.token);
    				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    				conn.setDoOutput(true);
    				conn.setRequestMethod("PUT"); 
    				conn.setRequestProperty("Content-Type", "text/plain");
    				
    			
    				OutputStream os = conn.getOutputStream();
    				os.write(location.getBytes());
    				os.flush();
    				
    				int code = conn.getResponseCode();
    				os.close();
    				conn.disconnect();
    				if (code != HttpURLConnection.HTTP_OK) {
    					Platform.runLater(() -> {
    						alert("Blokirani ste");
    					});
    				}
    				
    			} catch(IOException e) {
    				Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
    			}
				
    		}).start();
    	}catch(NumberFormatException e) {
	    	alert("Podaci nisu u odgovarajucem formatu!");
    	}
    	
    }
    
    void alert(String alertMessage) {
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setHeaderText(null);
    	alert.setContentText(alertMessage);
    	alert.showAndWait();
    }
}