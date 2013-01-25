import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class ServiceChat extends Thread {
	int serverMode;
	String login;
	String line;
	Socket socket;
	PrintStream out;
	PrintStream tmp;
	Scanner in;
	static final HashMap<String, PrintStream> clients = new HashMap<String, PrintStream>();
	
	/* Redefined the I/O for the client socket */
	public ServiceChat(Socket s) {
		try {
			socket = s;
			out = new PrintStream(socket.getOutputStream());
			in = new Scanner((Readable) new BufferedReader(new InputStreamReader(socket.getInputStream())));
		} catch (Exception e) {
			System.out.println("erreur :" + e.getMessage());
		}
		this.start();
	}
	
	public void run() {
		try {
			out.print("nickname : ");
			login = in.next();
			while (clients.get(login) != null) {
				out.println(login + " is already connected");
				out.print("nickname : ");
				login = in.next();	
			}
			clients.put(login, out);
			
			for (Entry<String, PrintStream> currentEntry : clients.entrySet()) {
				 tmp = currentEntry.getValue();
				 tmp.println("[Info] " + login + " is connected");
			}

			while (!(line = in.nextLine()).equals("exit"));
			socket.close();
		} 
		catch (Exception e) {
			System.out.println("erreur :" + e.getMessage());
		}
	}
}
