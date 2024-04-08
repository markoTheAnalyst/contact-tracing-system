package application;
	
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(ClientProperties.APP_FXML));
			Parent root = loader.load();
			
			ApplicationController  controller = loader.getController();
			controller.activateChat();
			ClientProperties.controller = controller;
			ClientProperties.receiveNotification();
	        primaryStage.setScene(new Scene(root));
	        primaryStage.show();
		} catch(Exception e) {
			Logger.getLogger(ClientProperties.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
