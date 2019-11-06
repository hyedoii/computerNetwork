package comnet;

import java.io.*;
import java.net.*;

public class TCP_Client extends Thread{
   String userName;
   Socket clientSocket = new Socket();
   int timeout=5000;
   BufferedReader inFromUser;
   DataOutputStream outToServer;
   BufferedReader inFromServer;
   
   public TCP_Client(SocketAddress soc, String userName){
	   while (true){
		   try{
			   this.clientSocket.connect(soc, 100000);
			   this.userName=userName;
			   break;
		   }
		   catch(Exception e){
			   clientSocket = new Socket();
		   }
	   }
   }
   
   public void run(){
	   String sentence;
	   try {
		   inFromUser=new BufferedReader(new InputStreamReader(System.in));
		   outToServer=new DataOutputStream(clientSocket.getOutputStream());
		   inFromServer=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		   while(true){
			   sentence =inFromUser.readLine();
			   outToServer.writeBytes(userName + " : " + sentence + '\n');
		   }
	   }
	   catch(Exception e){
		   System.out.printf("Err");
	   }
   }
}