package ds.project1.eventmanager;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import ds.project1.commondtos.ConnectionDetails;

public class ConnectionManager implements Runnable {

	private static EventManager manager;

	private Properties props;

	public ConnectionManager(EventManager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		try {
			loadProperties();

			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(Integer.parseInt(props.getProperty("server.port.number")));
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
			String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			String appConfigPath = rootPath + "application.properties";

			Properties appProps = new Properties();
			appProps.load(new FileInputStream(appConfigPath));

			props = new Properties();
			props.load(new FileInputStream(appConfigPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
