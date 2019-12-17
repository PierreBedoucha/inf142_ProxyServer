import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

public class MainProg {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// PROXY INIT: 
		// Proxy always listen to browser and server. Initialized via a thread in main
		ServerSocket server = new ServerSocket("8080");
		while (!interrupted()) 
		{
			Socket client = server.accept();
			ProxyServerClientThread proxyServerClientThread = new ProxyServerClientThread(client);	
			Thread thProxyServerClient = new Thread(proxyServerClientThread);
			thProxyServerClient.start();
			sleep(5);
		}
		
		
		// SERVER THREAD:
		// Listens the browser. Initialize listenning on local port. Waits for browser interruption
		// Then initialize client thread
		ServerSocket server = new ServerSocket("8080");
		while (!interrupted()) 
		{
			Socket client = server.accept();
			ProxyServerClientThread proxyServerClientThread = new ProxyServerClientThread(client);	
			Thread thProxyServerClient = new Thread(proxyServerClientThread);
			thProxyServerClient.start();
			sleep(5);
		}
		
		// CLINET THREAD:
		// INIT FLUX
		// Datastreams

		DataInputStream clientIn, serverIn;
		DataOutputStream clientOut, serverOut;
		
		// Sockets send back bytes got via getStream to be buffered
		clientIn = new DataInputStream(new BufferedInputStream(client.getInputStream()));
		clientOut = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
		serverIn = new DataInputStream(new BufferedInputStream(socketServer.getInputStream()));
		serverOut = new DataOutputStream(new BufferedOutputStream(socketServer.getOutputStream()));

		// REQUEST
		// Unknown byte number, unknown type
		// The flux is destroyed upon reading, need to reconstruct the request after reading to transmit it to the server
		// the request is read byte by byte
		DataInputStream clientInSave = new DataInputStream(new BufferedInputStream(clientIn));
		String line = "";
		String car = "";
		int bytesR;
		byte[] request = new byte[1];
		int nbrBytes = clientInSave.available();
		for (int index = 0; index < nbrBytes;index++)
		{
			bytesR = clientInSave.read(request);
			car = Character.toString((char)request[0]);
			line = line + car;
		}
		// each request line is stored into an array
		ArrayList<String> requestArray  = new ArrayList<String>();
		String[] tabLine = line.split("\r\n");
		int nbrLine = tabLine.length;
		for (int index = 0; index < nbrLine;index++)
			requestArray.add(tabLine[index]);
		
		//Extract GET, POST
		String strUrl = requestArray.get(0);
		if (strUrl.startsWith("GET"))
		{
			String sub = strUrl.substring(4);
			int posFin = sub.indexOf("HTTP");
			sub = sub.substring(0,posFin);
			urlSend = TrouveUrlBase(sub);
		}			
		if (strUrl.startsWith("POST"))
		{
			String sub = strUrl.substring(5);
			int posFin = sub.indexOf("HTTP");
			sub = sub.substring(0,posFin);
			urlSend = TrouveUrlBase(sub);
		}
		URL url = new URL(urlSend);
		Socket socketServer = new Socket(url.getHost(),80);
		
		// SEND REQUEST TO SERVER
		// Not to block app, request is sent through thread
		// params : the request, output flux towards thread
		ThreadSendServer threadSendServer = new ThreadSendServer(serverOut,requestArray);
		Thread thSendServer = new Thread(threadSendServer);
		thSendServer.start();
		
		// the thread waits for server answer
		byte[] reply = new byte[4096];
		int bytesRead;	
		while ((bytesRead = serverIn.read(reply)) != -1){
			clientOut.write(reply, 0, bytesRead);
			clientOut.flush();
		}
	}

}
