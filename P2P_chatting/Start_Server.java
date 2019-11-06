package comnet;

import java.net.*;


public class Start_Server extends Thread{
	int port;
	public Start_Server(int port){
		this.port=port;
	}
public void run(){
   try{
      ServerSocket server_socket = new ServerSocket(this.port);
      while(true){
         Socket soc = server_socket.accept();
         TCP_Server server = new TCP_Server(soc);
         server.start();
      }
      //server_socket.close();
   }
   catch (Exception e){
      System.out.println("Err @ Start_Server");
   }
}
}
