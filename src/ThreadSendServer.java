import java.io.PrintWriter;

public class ThreadSendServer {

	ThreadSendServer(){
		PrintWriter out = new PrintWriter(new OutputStreamWriter(serverOut));
		for (int i = 0; i < requete.size(); ++i)
			out.println(requete.get(i));
		out.println(); // Envoyer une ligne vierge -> fin de la requête
		out.flush();
	}
}
