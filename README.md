# inf142_ProxyServer

## INF142
## Obligatorisk oppgave 1
## 2017

Dette er den første av tre obligatoriske oppgaver i INF142 i løpet av vårsemesteret 2017. Studenter som ikke leverer eller som ikke får innleveringen godkjent, får ikke ta eksamen. To og to (men ikke fler enn to) studenter kan jobbe sammen og kan produsere en felles oppgave. I så fall skal begge studenter levere inn hver sin kopi av besvarelsen, som skal ha en eksplisitt henvisning til den andre studenten. Ut over dette er samarbeid ikke tillatt, og kopiering fra andre studenter, fra nettet eller fra andre kilder er selvfølgelig juks og fanteri og forbudt.  

Points explanation: xxxxxx

Frist: Monday 20. februar kl. 09.00.

Beskrivelse

A client should be able to connect and interact with a server which is only responsible for providing time or date.

The server, by definition, has no way to predict about which client will connect to interact and must be reachable at all time. For that purpose, Java provides a special class ServerSocket. In this exercise your are going to use this class to set up the time and date applicative server.

The class ServerSocket is part of the java.net API and you can find its documentation on https://docs.oracle.com/javase/8/docs/api/java/net/ServerSocket.html
It is designed with a constructor which allows to specify the listening server port and with a method accept() to wait for an ingoing client connection. 

Your applicative server process presentation:
-	A new ServerSocket instance is created on a specified port
-	This socket awaits for an ingoing client connection
-	Upon connection, it is possible to use the methods getInputStream() or getOutputStream() to interact with the server
-	The client and the server interact with one another
-	Either the client or the server closes the ongoing connection
In this exercise, the server will behave like a normal applicative server and should then be able to accept multiple simultaneous client connections. To ensure the latter, you will have to design a multithread server.
Technical help: the Java Threads
When you execute a program in Java a new thread is started and this thread will generate a new dedicated stack of executions. 
Once the thread object is created you can define the tasks that the thread will perform by implementing the interface Runnable (https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html). A thread is started with the method start() and upon starting the run() method. 
The operations you want to perform in another runtime stack must be placed in the run() method. A thread is declared dead when it has gone through the run() method and its execution stack.
Taking into account the previous, the body of a thread will look like the following:
```Java
ClientThread.java
public class ClientThread implements Runnable{
   public ClientThread(){
      //Class constructor
   }
   public void run(){
      // different executions in the run() method
   }
}

Main.java:
public class Main {
   public static void main(String[] args) {
      Thread thread = new Thread(ClientThread());
      thread.start();
   }
}
```

## Oppgaver

1. Taking into account the detailed presentation, write the java code to start the server side. You will design a Server.java class that is responsible for initializing our time server.
   - The socket port will be specified and should be an available one. ServerSocket socket = new ServerSocket(port) runs only if the port not taken by another connection.
   - The class constructor will be defined as follows: Server( String ipHost, int portNumber)
   - The Server class will have one method open() to start the server. 
      - As explained in the presentation, the server start will be in separate thread which runs an infine loop to wait for ongoin client connection
      - Upon client connection, the interaction will be managed by an external thread that you will have to define in a ClientProcessor class
      - Remember to actually your threads with start()
   - The Server class will have one method close() to close the connection between the client and the server
2. To process the client connection on the server side, you will design a ClientProcessor class 
   - The class will implement the class Runnable to be started as a thread
   - This thread has to run until the client connection is closed
   - The client request will be processed reagarding command words sent:
      - ”DATE” will send back the date to the client using the method getDateInstance()
      - ”TIME” will send back the time to theclient using the method getTimeInstance()
      - ”CLOSE” will terminate the client and server interaction
   - The class will also have one method read() that will be used in the ClientProcessor thread to read the reponses
      - The response is exchanged in bytes
      - One possibility is to use a BufferedInputStream to build the String that will be then processed as one of the commands from the client
3. On the client side, write the java code of the class Client.java. 
   - The class will implement the class Runnable to be started as a thread
      - This thread will consist in sending 5 requests randomly selected from the command words of the point 2)c. 
      - The different threads synchonisation will not be managed in this exercise. For that purpose, you will have to add 1ms of sleep() at the beginning and the end of the client thread
      - Hint: remember to flush() the writer after sending the command from the client to the server
   - You will have to print the answer received from the server to the client
      - Thus the class will also have one method read() that will be used in the ClientProcessor thread to read the responses
4. Finally, write the main program Main.java that will launch the server and the client connection threads
   - The main program will have to start 5 different client connections

Du kan se bort fra klient/tjener autentisering og andre sikkerhetsrelaterte aspekter.

Hint: For å unngå eventuelle brannmurproblemer kan du, for å testkjøre programmet ditt, kjøre tjener- og klientprosessen på samme maskin. IP-adressen «127.0.0.1» er nyttig å bruke for slik testing.

Maksimum to studenter kan samarbeide og levere inn identiske oppgaver. I så fall skal dere likevel hver for dere levere inn oppgavene på studentportalen, med en referanse til den du har samarbeidet med. 

