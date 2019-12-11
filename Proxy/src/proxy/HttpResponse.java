package proxy;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpResponse {
	final static String CRLF = "\r\n";
	private static final int BUF_SIZE = 1000000;
	private static final int MAX_OBJECT_SIZE = 1000000;
	
	String statusLine = "";
	String headers = "";
	byte[] body = null;
	
	// Read response from server
	public HttpResponse(DataInputStream fromServer)
	{
		// Length of the object
		int length = -1;
		boolean gotStatusLine = false;
		System.out.println("\nhttp response");
		
		// First read status line and response headers
		try
		{
			BufferedReader fs = new BufferedReader(new InputStreamReader(fromServer));
			String line = fs.readLine();

			while (line.length() != 0)
			{
				if (!gotStatusLine)
				{
					statusLine = line;
					gotStatusLine = true;
				}
				else
				{
					headers += line + CRLF;
				}

				// Get length of content as indicated by Content-Length header.
				// Unfortunately this is not present in every response.
				// Some servers return the header "Content-Length", others return "Content-length".
				// You need to check for both here.
				if (line.startsWith ("Content-Length:") || line.startsWith ("Content-length:"))
				{
					String[] tmp = line.split(" ");
					length = Integer.parseInt(tmp[1]);
				}
				line = fs.readLine();
			}
		}
		catch (IOException e)
		{
			System.out.println("Error reading headers from server: " + e);
			return;
		}

		try
		{
			int bytesRead = 0;
			byte[] buf = new byte[BUF_SIZE];
			boolean loop = false;
			System.out.println("length : " + length);
			
			// If we didn't get Content-Length header, just loop until the connection is closed.
			if (length == -1)
			{
				loop = true;
			}

			// Read the body in chunks of BUF_SIZE copy the chunk into body.
			// Usually replies come back in smaller chunks than BUF_SIZE.
			// The while-loop ends when either we have read Content-Length bytes or when the connection is closed
			// (when there is no Connection-Length in the response.)
			while (bytesRead < length || loop)
			{
				int res = fromServer.read(buf);
				if (res == -1)
				{
					break;
				}
				
				// Copy the bytes into body.
				// Make sure we don't exceed the maximum object size.
				for (int i = 0; i < res && (i + bytesRead) < MAX_OBJECT_SIZE; i++)
				{
					body [bytesRead + i] = buf[i];
				}
				bytesRead += res;
			}
		}
		catch (IOException e)
		{
			System.out.println("Error reading response body: " + e);
			return;
		}
	}
	
	public String toString() {
		String res = "";
		res = statusLine + CRLF;
		res += headers;
		res += CRLF;
		return res;
	}

}
