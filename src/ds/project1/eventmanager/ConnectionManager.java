
package ds.project1.eventmanager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class ConnectionManager implements Runnable {

	private static EventManager manager;

	private Properties props;

	public ConnectionManager(EventManager manager) {
		ConnectionManager.manager = manager;
	}

	@Override
	public void run() {
		try {
			loadProperties();

			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(Integer.parseInt(props.getProperty("server.port.number")));
			Socket socket;

			while (true) {
				System.out.println("ConnectionManagerThread: Ready to accept the connections !");
				socket = serverSocket.accept();
				System.out.println("ConnectionManagerThread: Got the connect request !");
				Thread t = new Thread(new EventManagerHelper(manager, socket));
				t.start();
			}

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			System.err.println("ConnectionManagerThread: Exception occured: " + e.getMessage());
			// e.printStackTrace();
		}
	}

	private void loadProperties() {
		try {
			props = new Properties();
			props.load(ConnectionManager.class.getClassLoader().getResourceAsStream("application.properties"));
		} catch (IOException e) {
			System.err.println("ConnectionManagerThread: Exception occured: " + e.getMessage());
			// e.printStackTrace();
		}
	}
}
