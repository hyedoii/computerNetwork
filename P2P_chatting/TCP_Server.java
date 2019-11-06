package comnet;

import java.io.*;
import java.net.*;

public class TCP_Server extends Thread{
	Socket soc;
	DataOutputStream outToClint;
	public TCP_Server(Socket soc) throws IOException{
		this.soc =soc;
	}
	public void run(){
		try{
			String clientSentence;
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			DataOutputStream outToClient=new DataOutputStream(soc.getOutputStream());
      
			while(true){
				clientSentence=inFromClient.readLine();
				System.out.println(clientSentence);
			}
		}
		catch(IOException e){
			System.out.println("Exception Err");
		}
	}
}