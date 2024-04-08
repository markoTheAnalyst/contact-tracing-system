package application;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import token.TokenServer;
import token.TokenServerServiceLocator;

public class AuthenticationController {
	
	@FXML
    public AnchorPane root;

    @FXML
    public JFXTextField jmbg;

    @FXML
    public JFXTextField lastName;

    @FXML
    public JFXTextField name;

    @FXML
    public JFXButton logInbttn;

    @FXML
    public void logIn() throws IOException {
    	
    	
    	TokenServerServiceLocator loc = new TokenServerServiceLocator();
		try {
			TokenServer server = loc.getTokenServer();
			String Name = name.getText();
	       
	        String LastName = lastName.getText();
	        
	        String JMBG = jmbg.getText();
	        
	        if(JMBG.isBlank() || LastName.isBlank() || Name.isBlank())
	        	throw new IllegalArgumentException();
	        String logInInfo = String.join("@","jmbg:"+JMBG,Name,LastName);
			String token = server.logIn(logInInfo);
			if(ClientProperties.ERROR.equals(token))
				throw new IllegalArgumentException();
			ClientProperties.token = token;
			ClientProperties.id = server.doesTokenExist(token);
			Parent pane = (Parent) FXMLLoader.load(getClass().getResource(ClientProperties.LOGIN_FXML));
	    	ClientProperties.sessions = FXCollections.observableArrayList();
	 
	        Stage stage = new Stage();
	        stage.setScene(new Scene(pane));  
	        stage.show();
	        
	        root.getScene().getWindow().hide();
			
		}catch(IllegalArgumentException e) {
	    	Alert alert = new Alert(AlertType.WARNING);
	    	alert.setHeaderText(null);
	    	alert.setContentText("Podaci nisu ispravni!");
	
	    	alert.showAndWait();
		} catch (ServiceException e) {
			
			Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}
    	
    	
    }

}
