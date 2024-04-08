package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface IFIleServer extends Remote {
	
	HashMap<String, byte[]> download(String token) throws RemoteException;
	void  upload(byte[] content, String name, String token) throws RemoteException;

}
