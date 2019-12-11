package proxy;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpRequest {
	final static String CRLF = "\r\n";
	private static final int HTTP_PORT = 80;
	
	String host = "";
	String headers = "";
	int port = 0;
	String method = "";
	String URI = "";
	String version = "";

	public HttpRequest(BufferedReader from)
	{
		String firstLine = "";
		try	
		{
			firstLine = from.readLine();
		}
		catch (IOException e)
		{
			System.out.println("Error reading request line: " + e);
		}
		
		String[] tmp = firstLine.split(" ");
		method = tmp [0];
		URI = tmp [1];
		version = tmp [2];
		
		System.out.println("\nCreate HttpRequest by reading it from the client socket:");
		System.out.println("Method is: " + method);
		System.out.println("URI is: " + URI);
		System.out.println("Version is: " + version);
		
		if (!method.equals("GET"))
		{
			System.out.println("Error: Method not GET");
		}
		try
		{
			String line = from.readLine();
			while (line.length() != 0)
			{
				headers += line + CRLF;
				// We need to find host header to know which server to contact in case the request URI is not complete.
				if (line.startsWith("Host:"))
				{
					tmp = line.split(" ");
					if (tmp[1].indexOf(':') > 0){
						String[] tmp2 = tmp[1].split(":");
						host = tmp2[0];
						port = Integer.parseInt(tmp2[1]);
					}
					else{
						host = tmp[1];
						port = HTTP_PORT;
					}
				}
				line = from.readLine();
			}
		}
		catch (IOException e)
		{
			System.out.println("Error reading from socket: " + e);
			return;
		}
		host = "192.168.1.155";
		port = 7777;
		System.out.println("Host to contact is: " + host + " at port " + port);
	}
	
	// Return host for which this request is intended
	public String getHost() {
		return host;
	}
	
	// Return port for server
	public int getPort() {
		return port;
	}
	
	// Convert request into a string for easy re-sending.
	public String toString() {
		String req = "";
		
		req = method + " " + URI + version + CRLF ;
		req += headers;
		//this proxy does not support persistent connections
		req += "Connection: close" + CRLF;
		req += CRLF;
		
		return req;
	}
	

}
