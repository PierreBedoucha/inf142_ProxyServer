import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.util.Date;

public class ClientProcessor implements Runnable{
	private Socket sock;
	private PrintWriter writer = null;
	private BufferedInputStream reader = null;
	
	public ClientProcessor(Socket pSock){
		sock = pSock;
	}

	//Processing is launched in a separate thread
	public void run(){
		System.err.println("Client connection is being processed");
		boolean closeConnexion = false;

		//While the connexion is active, request are being processed
		while(!sock.isClosed()){
			try {
				//Other objects are used for method flush() instead of close()

				writer = new PrintWriter(sock.getOutputStream());
				reader = new BufferedInputStream(sock.getInputStream());

				//Wait for client request           
				String response = read();
				InetSocketAddress remote = (InetSocketAddress)sock.getRemoteSocketAddress();

				//Debug infos
				String debug = "";
				debug = "Thread : " + Thread.currentThread().getName() + ". ";
				debug += "Address request : " + remote.getAddress().getHostAddress() +".";
				debug += " On port : " + remote.getPort() + ".\n";
				debug += "\t -> Command received : " + response + "\n";
				System.err.println("\n" + debug);

				//Client request processed regarding sent command
				String toSend = "";

				switch(response.toUpperCase()){
				case "FULL":
					toSend = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM).format(new Date());
					break;
				case "DATE":
					toSend = DateFormat.getDateInstance(DateFormat.FULL).format(new Date());
					break;
				case "HOUR":
					toSend = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date());
					break;
				case "CLOSE":
					toSend = "Closed communication"; 
					closeConnexion = true;
					break;
				default : 
					toSend = "Unknown command !";                     
					break;
				}

				//Response to client sent
				writer.write(toSend);
				//MANDATORY TO USE FLUSH()
				writer.flush();

				if(closeConnexion){
					System.err.println("CLOSE COMMAND DETECTED ! ");
					writer = null;
					reader = null;
					sock.close();
					break;
				}
			}catch(SocketException e){
				System.err.println("CONNECTION DISRUPTED ! ");
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}         
		}
	}

	//Responses reading method
	private String read() throws IOException{      
		String response = "";
		int stream;
		byte[] b = new byte[4096];
		stream = reader.read(b);
		response = new String(b, 0, stream);
		return response;
	}
}
