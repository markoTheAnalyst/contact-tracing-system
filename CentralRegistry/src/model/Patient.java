package model;

public class Patient {
	String token;
	int coordinateX;
	int coordinateY;
	
	public Patient(String token, String coordinateX, String coordinateY) {
		this.token = token;
		this.coordinateX = Integer.valueOf(coordinateX);
		this.coordinateY = Integer.valueOf(coordinateY);
	}
	
	
}
