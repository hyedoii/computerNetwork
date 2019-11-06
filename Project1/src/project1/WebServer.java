package project1;

import java.io.*;
import java.net.*;
import java.util.*;

public final class WebServer {
	public static void main(String[] args)throws Exception
	{
		//Use any port higher than 1024
		//Must you the same port number when making requests to my web from my browser
		//Set the port number. int port = 6789;
		int port = 9030;
		ServerSocket listenSocket = new ServerSocket(port);
		System.out.println("WebServer Socket Created");
		Socket connectionSocket;
		//Process HTTP service requests in an infinite loop.
		while(true)
		{
			connectionSocket = listenSocket.accept();
			//Listen for a TCP connection request.
			//create a new thread to process the request.
			Thread thread = new Thread(new HttpRequest(connectionSocket));
			//start the thread.
			thread.start();
		}
	}
}

