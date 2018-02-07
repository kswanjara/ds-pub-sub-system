
package ds.project1.eventmanager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import ds.project1.commondtos.Packet;
import ds.project1.ds.project1.common.enums.PacketConstants;

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
				ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
				Packet packet = (Packet) inputStream.readObject();
				String[] ports = props.getProperty("server.multi.ports").split(",");
				String port = ports[(int) (Math.random() * ports.length)];
				packet.setType(PacketConstants.Port.toString());
				packet.setPort(port);
				System.out.println("ConnectionManagerThread: Forwarded to port : " + port);
				outputStream.writeObject(packet);
				// Thread t = new Thread(new EventManagerHelper(manager, socket));
				// t.start();
			}

		} catch (NumberFormatException | IOException | ClassNotFoundException e) {
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
