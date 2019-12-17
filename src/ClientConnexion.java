import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class ClientConnexion implements Runnable {
	private Socket connexion = null;
	private PrintWriter writer = null;
	private BufferedInputStream reader = null;

	//Our command list
	private String[] listCommands = {"FULL", "DATE", "HOUR", "NONE"};
	private static int count = 0;
	private String name = "Client-";   

	public ClientConnexion(String host, int port){
		name += ++count;
		try {
			connexion = new Socket(host, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	   	   

	public void run(){
		//5 requests per thread
		for(int i =0; i < 5; i++){
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {	            
				writer = new PrintWriter(connexion.getOutputStream(), true);
				reader = new BufferedInputStream(connexion.getInputStream());

				//Send command to server            
				String commande = getCommand();
				writer.write(commande);
				//ALWAYS USE FLUSH!!!
				writer.flush();  

				System.out.println("Command " + commande + " sent to server");	            

				//Wait for answer
				String response = read();
				System.out.println("\t * " + name + " : Answer received " + response);	            
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		writer.write("CLOSE");
		writer.flush();
		writer.close();
	}

	//Randomly sending command
	private String getCommand(){
		Random rand = new Random();
		return listCommands[rand.nextInt(listCommands.length)];
	}	   

	//Read server answers
	private String read() throws IOException{      
		String response = "";
		int stream;
		byte[] b = new byte[4096];
		stream = reader.read(b);
		response = new String(b, 0, stream);      
		return response;
	}   
}
