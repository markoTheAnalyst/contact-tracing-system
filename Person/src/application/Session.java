package application;

public class Session {
	
	String logInTime;
	String logOutTime;
	long activeTime;
	
	public Session(String logInTime, String logOutTime, long activeTime) {
		this.logInTime = logInTime;
		this.logOutTime = logOutTime;
		this.activeTime = activeTime;
	}

	public String getLogInTime() {
		return logInTime;
	}

	public void setLogInTime(String logInTime) {
		this.logInTime = logInTime;
	}

	public String getLogOutTime() {
		return logOutTime;
	}

	public void setLogOutTime(String logOutTime) {
		this.logOutTime = logOutTime;
	}

	public long getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(long activeTime) {
		this.activeTime = activeTime;
	}
	
	

}
