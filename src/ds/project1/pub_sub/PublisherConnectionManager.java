/*
 * PublisherConnectionManager.java
 *
 * Version:
 *     $Id$: v 1.1
 *
 * Revisions:
 *     $Log$: Initial Revision
 */

package ds.project1.pub_sub;

import ds.project1.commondtos.ConnectionDetails;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 *  The connection manager creates a socket for the publisher. It
 */
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
			String appConfigPath = System.getProperty("java.class.path") + System.getProperty("file.separator")
					+ "application.properties";

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
