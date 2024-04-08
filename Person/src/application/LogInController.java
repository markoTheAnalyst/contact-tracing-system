package application;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LogInController {

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXButton loginBttn;
    
    @FXML
    private JFXButton changePassBttn;

    @FXML
    void logIn() {
    	
    	if(passwordField.getText().isEmpty()) {
    		alert("Unesite lozinku!");
    		return;
    	}
    	
    	if(!ClientProperties.PASS.equals(passwordField.getText())) {
    		alert("Lozinka nije ispravna!");
    		return;
    	}
    	
    	Parent pane;
		try {
			
	    	ClientProperties.logInTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
	    	ClientProperties.logInMs = System.currentTimeMillis();
	    	
	        FXMLLoader loader = new FXMLLoader(getClass().getResource(ClientProperties.APP_FXML));
	        pane = loader.load();
			
			ClientProperties.controller = loader.getController();
	        ClientProperties.receiveNotification();
	        
	        Stage stage = new Stage();
			stage.setScene(new Scene(pane));  
	        stage.show();
	        loginBttn.getScene().getWindow().hide();
	                
		} catch (IOException e) {
			Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}
       
    }
    
    @FXML
    void changePass() {
    	
    	if(passwordField.getText().isEmpty()) {
    		alert("Unesite lozinku!");
    		return;
    	}
    	ClientProperties.PASS = passwordField.getText();
    	passwordField.getScene().getWindow().hide();
    }
    
    void alert(String alertMessage) {
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setHeaderText(null);
    	alert.setContentText(alertMessage);
    	alert.showAndWait();
    }

}
