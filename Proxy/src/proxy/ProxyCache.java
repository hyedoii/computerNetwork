package proxy;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

public class ProxyCache {
	private static int port;
	private static ServerSocket socket;
	private static Map cache;

	public static void main(String[] args) {
		int myPort = 0;

		try {
			myPort = Integer.parseInt(args[0]);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println("Need port number as argument");
			System.exit(-1);
		}
		catch(NumberFormatException e)
		{
			System.out.println("Please give port numbers as integer.");
			System.exit(-1);
		}
		System.out.println("port number : "+ myPort);
		init(myPort);
	
		/* Main loop Listen for incoming connections and spawm a new thread for hamdling them. */
		
		Socket client = null;
		while (true)
		{
			try
			{
				client = socket.accept();
				handle(client);
			}
			catch (IOException e)
			{
				System.out.println("Error reading request from client: " + e);
				// Definitely cannot continue processing this request, so skip to next iteration of while loop.
				continue;
			}
		}

	}
	
	//Create the ProxyCache object and the socket
	public static void init(int p)
	{
		port = p;
		try
		{
			socket = new ServerSocket (port);
		}
		catch (IOException e)
		{
			System.out.println("Error creating socket: " + e);
			System.exit(-1);
		}

	}
	
	public static void handle(Socket client)
	{
		Socket server = null;
		HttpRequest request = null;
		HttpResponse response = null;
		
		// Process request. If there are any exceptions, then simply return and end this request.
		// This unfortunately means the client will hang for a while, until it timeouts.
		
		// Read request
		try
		{
			BufferedReader fromClient = new BufferedReader (new InputStreamReader (client.getInputStream()));
			request = new HttpRequest (fromClient);
		}
		catch (IOException e)
		{
			System.out.println("Error reading request from client: " + e);
			return;
		}

		// Send request to server
		try
		{
			// Open socket and write request to socket
			server = new Socket ("192.168.1.155", 7777);
			DataOutputStream toServer = new DataOutputStream (server.getOutputStream());
			toServer.writeBytes (request.toString());
			System.out.println("------request-----------");
			System.out.println(request.toString());
			System.out.println("------------------------");
		}
		catch (UnknownHostException e)
		{
			System.out.println("Unknown host: " + request.getHost());
			System.out.println(e);
			return;
		}
		catch (IOException e)
		{
			System.out.println("Error writing request to server: " + e);
			return;
		}

		// Read response and forward it to client
		try
		{
			DataInputStream fromServer = new DataInputStream(server.getInputStream());
			response = new HttpResponse (fromServer);//!
			DataOutputStream toClient = new DataOutputStream (client.getOutputStream());
			System.out.print (response.toString());
			toClient.write(response.body);
			client.close();
			server.close();
		}
		catch (IOException e)
		{
			System.out.println("Error reading request line: " + e);
			e.getStackTrace();
		}
		catch(Exception e)
		{
			e.getStackTrace();
		}
	}
}



