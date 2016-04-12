package prices;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

public class PriceImporter{
	private FTPClient ftpClient;
	private boolean connectedToServer;
	
	PriceImporter(){
		super();
		connectedToServer = connectToServer();
	}
	
	public void getFile(String serverFileDirectory, String localFileDirectory) throws IOException {
		if(connectedToServer){
			ftpClient.retrieveFile(serverFileDirectory, new FileOutputStream(localFileDirectory));
		}
	}
	
	public boolean connectToServer() {
		boolean login = false;
		try {
			ftpClient.connect("stormsproductions.com");
			login = ftpClient.login("projectXman", "projectXman1!");
		} catch (Exception e){
			e.printStackTrace();
		}
		return login;
	}
	
	public void importPrices(){
		
	}
}
