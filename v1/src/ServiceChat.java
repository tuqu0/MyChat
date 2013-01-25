import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class ServiceChat extends Thread {
	static final int nb_users_max = 3;
	static int nb_users = 0;
	String login;
	String line;
	Socket socket;
	PrintStream out;
	PrintStream tmp;
	Scanner in;
	static final HashMap<String, PrintStream> clients = new HashMap<String, PrintStream>();
	
	public ServiceChat(Socket s) {
		try {
			socket = s;
		} catch (Exception e) {
			System.out.println("erreur :" + e.getMessage());
		}
		this.start();
	}
	
	@Override
	public void run() {
		if (init())
			loop();
	}

	public boolean init() {
		try {
			out = new PrintStream(socket.getOutputStream());
			in = new Scanner((Readable) new BufferedReader(new InputStreamReader(socket.getInputStream())));

			/* Vérification que le nombre max de clients autorisés n'est pas atteint */
			if (nb_users >= nb_users_max) {
				out.println("Le nombre maximum d'utilisateurs est atteint (" + nb_users + ")");
				System.out.println(socket.getRemoteSocketAddress() + " : Le nombre maximum d'utilisateurs est atteint (" + nb_users + ")");
				socket.close();
				return false;
			}
			
			/* Récupération du pseudo du client */
			out.print("nickname : ");
			login = in.next();	
			while (clients.get(login) != null) {
				out.println("[Info] " + login + " is already connected");
				out.print("nickname : ");
				login = in.next();	
			}
		
			synchronized(clients) {
				/* Ajout du pseudo et du flux de sortie du client dans la HashMap */
				clients.put(login, out);
			
				/* Incrémentation du nombre de clients */
				nb_users++;
			}
			
			/* Informe tous les clients qu'un nouvel utilisateur est connecté */
			messageAll("[Info] " + login + " is connected");
			System.out.println("[Info] " + login + " is connected");
			
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public void loop() {
		String msg;
		
		/* Prompt  */
		out.print(login + " : ");
		msg = in.nextLine();
		while (in.hasNextLine()) {
			msg = in.nextLine();
			msg.trim();
			if (msg.equals("exit"))
				break;
			/* Envoie du message à tous les clients connectés */
			messageAll(login + " : " + msg);
			out.print(login + " : ");
		}
		
		/* Deconnection du client */
		disconnect();
	}

	public void disconnect() {
			synchronized(clients) {
				/* On retire le pseudo et le flux de sortie du client de la ashMap */
				clients.remove(login);
			
				/* Décrémentation du nombre de client */
				nb_users--;
			}

			/* Fermeture de la connection */
			try {
				socket.close();
			}
			catch (Exception e) {
				e.printStackTrace(System.out);
			}
			
			/* Informe tous les clients qu'un client s'est déconnecté */
			messageAll("[Info] " + login + " s'est déconnecté");
			System.out.println("[Info] " + login + " s'est déconnecté");		
	}

	public void messageAll(String msg) {
		for (Entry<String, PrintStream> currentEntry : clients.entrySet()) {
			 if (currentEntry.getKey() != login) {
				 tmp = currentEntry.getValue();
				 tmp.println();
				 tmp.println(msg);
				 tmp.print(currentEntry.getKey() + " : ");
			 }
		}
	}

}

