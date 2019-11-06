package comnet;

import java.io.*;
import java.net.*;

public class P2P_Server {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("ID : ");
		String name = inFromUser.readLine();
		System.out.print("Port : ");
		int port = Integer.parseInt(inFromUser.readLine());
		System.out.print("Other port : ");
		int rport = Integer.parseInt(inFromUser.readLine());
		
		Start_Server server = new Start_Server(port);
		server.start();
		
		System.out.println("server started successfully");
		
		String ipAddr = "127.0.0.1";
		TCP_Client client = new TCP_Client(new InetSocketAddress(ipAddr, rport), name);
		client.start();
		
		System.out.println("client started successfully");
	}

}
