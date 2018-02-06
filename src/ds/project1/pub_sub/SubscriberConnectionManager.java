package ds.project1.pub_sub;

import ds.project1.commondtos.ConnectionDetails;
import ds.project1.commondtos.Packet;
import ds.project1.eventmanager.CallBack;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Properties;

public class SubscriberConnectionManager implements Runnable{
    private PubSubCallback manager;
    private Properties props;

    public SubscriberConnectionManager(PubSubCallback manager){
        this.manager = manager;
    }

	@Override
	public void run() {
		try {
			loadProperties();

			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(Integer.parseInt(props.getProperty("subscriber.port.number")));
			Socket socket;

            while (true) {
                socket = serverSocket.accept();
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                System.out.println("Connection from Event Manager");
                Packet packet;
                packet = (Packet) inputStream.readObject();
                PubSubAgent pubSubAgent = new PubSubAgent();
                pubSubAgent.handleEvent(packet);
                inputStream.close();
                socket.close();
            }

		} catch (NumberFormatException | IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadProperties() {
		try {
			props = new Properties();
			props.load(
					SubscriberConnectionManager.class.getClassLoader().getResourceAsStream("application.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
