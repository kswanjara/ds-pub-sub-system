package ds.project1.eventmanager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RequestListeningThread implements Runnable {

	private String port;

	private static EventManager manager;

	public RequestListeningThread(EventManager callBack, String port) {
		this.manager = callBack;
		this.port = port;
	}

	@Override
	public void run() {
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(Integer.parseInt(this.port));
			Socket socket;

			while (true) {
				System.out.println("RequestListeningThread: Ready to accept the connections !");
				socket = serverSocket.accept();
				System.out.println("Got packet on : " + this.port);
				// String[] ports = props.getProperty("server.multi.ports").split(",");
				Thread t = new Thread(new EventManagerHelper(manager, socket));
				t.start();
			}

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			System.err.println("RequestListeningThread: Exception occured: " + e.getMessage());
			// e.printStackTrace();
		}
	}

}
