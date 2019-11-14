package webClient;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class WebClient {

	public  String getWebContentByGet(String urlString,	final String charset, int timeout) throws IOException {
		if (urlString == null || urlString.length() == 0) {
			return null;
		}
		urlString = (urlString.startsWith("http://") || urlString.startsWith("https://")) ? urlString : ("http://" + urlString).intern();
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		//create http connection
		
		conn.setRequestProperty("User-Agent","2017029716/HYEJEONGPARK/WEBCLIENT/ComputerNetwork");
		conn.setRequestProperty("Accept", "text/html");
		//setting request headers
		conn.setConnectTimeout(timeout);
		
		try {
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		//reading the response
		InputStream input = conn.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input,	charset));
		String line = null;
		StringBuffer sb = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\r\n");
		}
		if (reader != null) {
			reader.close();
		}
		if (conn != null) {
			conn.disconnect();
		}
		return sb.toString();
	}

	public  String getWebContentByPost(String urlString, String data, final String charset, int timeout) throws IOException {
		if (urlString == null || urlString.length() == 0) {
			return null;
		}
		urlString = (urlString.startsWith("http://") || urlString.startsWith("https://")) ? urlString 
				: ("http://" + urlString).intern();
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		
		connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
		connection.setRequestProperty("User-Agent","2017029716/HYEJEONGPARK/WEBCLIENT/ComputerNetwork");
		connection.setRequestProperty("Accept", "text/xml");
		connection.setConnectTimeout(timeout);
		
		connection.connect();

		DataOutputStream out = new DataOutputStream(connection.getOutputStream());
		byte[] content = data.getBytes("UTF-8");
		System.out.println(content);
		out.write(content);
		out.flush();
		out.close();
		try {
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
		String line;
		StringBuffer sb = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\r\n");
		}
		if (reader != null) {
			reader.close();
		}
		if (connection != null) {
			connection.disconnect();
		}
		return sb.toString();
	}

	public static void main(String[] args) throws IOException {
		WebClient client = new WebClient();
		Scanner sc = new Scanner(System.in);
		
		String first = sc.nextLine();
		String s1 = client.getWebContentByGet(first,"UTF-8" , 10000000);
		System.out.println(s1);
		
		String data  = "2017029716";
		String picNum = sc.nextLine();
		String data2 = data + '/'+ picNum;
		
		String second = sc.nextLine();
		String s2 = client.getWebContentByPost(second, data2, "UTF-8", 1000000);
		
		String third = sc.nextLine();
		String s3 = client.getWebContentByPost(third, data, "UTF-8", 1000000);
		System.out.println(s3);
		
		String forth = sc.nextLine();
		String s4 = client.getWebContentByPost(forth, data, "UTF-8", 1000000);
		
		try {
			File outputFile = new File("test.jpg");
			BufferedImage image = ImageIO.read(new URL(forth));
			ImageIO.write(image, "jpg", outputFile);
			
			} catch(Exception e) {
				e.printStackTrace();
			}
		
		sc.close();
	}
	
}
