package ds.project1.pub_sub;

import ds.project1.commondtos.ConnectionDetails;
import ds.project1.eventmanager.ConnectionManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class PublisherConnectionManager implements Runnable {

	private PublisherEventManager manager;

	private Properties props;

	public PublisherConnectionManager(PublisherEventManager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		try {
			loadProperties();

			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(Integer.parseInt(props.getProperty("publisher.port.number")));
			Socket socket;

			while (true) {
				socket = serverSocket.accept();
				manager.newConnection(new ConnectionDetails(socket, "New Connection !"));
			}

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadProperties() {
		try {
			props = new Properties();
			props.load(PublisherConnectionManager.class.getClassLoader().getResourceAsStream("application.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
