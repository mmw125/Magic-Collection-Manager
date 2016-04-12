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
			//ftpClient.connect("....com");
			//login = ftpClient.login("...", "...");
		} catch (Exception e){
			e.printStackTrace();
		}
		return login;
	}
	
	public void importPrices(){
		
	}
}
