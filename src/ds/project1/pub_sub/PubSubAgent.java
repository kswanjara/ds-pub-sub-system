package ds.project1.pub_sub;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Proxy;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

import ds.project1.commondtos.Event;
import ds.project1.commondtos.Topic;

public class PubSubAgent {

	private static Socket socket;

	private static Properties props;

	public static void connectToEventManager(String type) {
		try {
			Socket socket = new Socket(props.getProperty("server.ip"),
					Integer.parseInt(props.getProperty("server.port.number")));

			ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.writeObject(type);

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void loadProperties() {
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

	public static void main(String[] args) {
		loadProperties();

		System.out.println("What am I? \n1. Publisher 2. Subscriber");
		Scanner sc = new Scanner(System.in);

		switch (sc.nextInt()) {
		case 1:
			connectToEventManager("Publisher");
			break;
		case 2:
			connectToEventManager("Subscriber");
			break;
		default:
			break;
		}

	}

	public void subscribe(Topic topic) {
	}

	public void subscribe(String keyword) {
	}

	public void unsubscribe(Topic topic) {
	}

	public void unsubscribe() {
	}

	public void listSubscribedTopics() {
	}

	public void publish(Event event) {
	}

	public void advertise(Topic newTopic) {
	}

}
