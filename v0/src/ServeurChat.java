import java.net.ServerSocket;
import java.net.Socket;

public class ServeurChat {
	private int port;
	private ServerSocket serverSocket;

	/* Default constructor */
	ServeurChat() {
		port = 1212;
	}

	/* Launch server and listen for clients connections */
	public void start() {
		Socket socketClient;

		try {
			serverSocket = new ServerSocket(port);
			while (true) {
				socketClient = serverSocket.accept();
				new ServiceChat(socketClient);
			}
		}
		catch (Exception e) {
			System.out.println("erreur : " + e.getMessage());
		}
	}

	/* Entry point : launch the chat server */
	public static void main(String[] args) {
		ServeurChat server = new ServeurChat();
		server.start();
	}

}
