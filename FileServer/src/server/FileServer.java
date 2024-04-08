package server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;

import token.TokenServer;
import token.TokenServerServiceLocator;

public class FileServer implements IFIleServer{

	@Override
	public HashMap<String, byte[]> download(String token) throws RemoteException {
		
		HashMap<String, byte[]> files = new HashMap<String, byte[]>();
		TokenServer tokenServer;
		try {
			tokenServer = new TokenServerServiceLocator().getTokenServer();
			String id = tokenServer.doesTokenExist(token);
			if("none".equals(id))
				return files;
			File fileNames = new File(id);
			if(!fileNames.exists())
				return files;
			for(String file : fileNames.list()) {
				files.put(file,Files.readAllBytes(Paths.get(id+File.separator+file)));
			}
			
		} catch (ServiceException | IOException e) {
			Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}
		return files;
	}

	@Override
	public void upload(byte[] content, String name, String token) throws RemoteException {
		
		try {
			
			TokenServer tokenServer = new TokenServerServiceLocator().getTokenServer();
			String id = tokenServer.doesTokenExist(token);
			
			if("none".equals(id))
				return;
			 
			File file = new File(id);		
			file.mkdir();
				
			try (FileOutputStream stream = new FileOutputStream(id + File.separator + name)) {
				stream.write(content);
			} 
				
		} catch (ServiceException | IOException e) {
			Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, e.getClass().getName());
		}
		
		
		
	}
	
	public static void main(String[] args) {
		
		System.setProperty("java.security.policy", "server_policyfile.txt");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			FileServer fileServer = new FileServer();
			IFIleServer stub = (IFIleServer)UnicastRemoteObject.exportObject(fileServer, 0);
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("FileServer", stub);
			System.out.println("Server started.");
		} catch (Exception ex) {
			Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, ex.getClass().getName());
		}

	}

}
