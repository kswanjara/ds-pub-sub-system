package ds.project1.pub_sub;

import java.io.*;
import java.net.Proxy;
import java.net.Socket;
import java.util.*;

import ds.project1.commondtos.*;
import ds.project1.ds.project1.common.enums.PacketConstants;
import ds.project1.eventmanager.dto.*;

public class PubSubAgent {

	private static Socket socket;

	private static Properties props;

	private static Packet connectToEventManager(Packet packet) {
		try {
			Socket socket = new Socket(props.getProperty("server.ip"),
					Integer.parseInt(props.getProperty("server.port.number")));

			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

			try {
				return (Packet) inputStream.readObject();
			}
			catch (IOException e) {
				System.out.println(e);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			socket.close();
			inputStream.close();

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void loadProperties() {
		try {
			String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
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
			//connectToEventManager("Publisher");
			break;
		case 2:
			//connectToEventManager("Subscriber");
			PubSubAgent pubSubAgent = new PubSubAgent();
			pubSubAgent.subscribe_helper();
			break;
		case 3:
			advertise_helper();
			break;
		default:
			break;
		}

	}

	public void subscribe(Topic topic) {

	}

	public void subscribe_helper() {

		SubscriberDto subscriberDto = new SubscriberDto();
		subscriberDto.setGuid("1");
		subscriberDto.setIp("192.168.sub.jeet");
		subscriberDto.setOnline(true);
		subscriberDto.setPort(8888);
		Packet packet = new Packet(null, null, PacketConstants.SubscriberDto.toString(), subscriberDto);
		Packet replyFromServer = connectToEventManager(packet);
		System.out.println("Select 1 of these tasks that you want to do:");
		System.out.println("Press 1 for subscribing to a topic using keywords \nPress 2 for subscribing directly to a topic using it's name \nPress 3 for unsubscribing from a topic \nPress 4 to show all th topics");
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextInt()) {
			switch (sc.nextInt()){
				case 1:
					System.out.println("Enter the keywords for the topic that you are interested in a single line separated with a comma:");
					String keywords = null;
					while (sc.hasNext()){
						keywords = sc.nextLine();
					}
					assert keywords != null;
					List<String> items = Arrays.asList(keywords.split("\\s*,\\s*"));
					subscribe(items);
					/*int id = 1;
					System.out.println("Please enter the topic name:");
					String topic_name = sc.next();
					Topic topic = new Topic(id, topic_name);*/


				case 2:

				case 3:

				default:
					System.out.println("Enter correct number again please:");
			}

		}
	}
	public void subscribe(List<String> keyword) {
		Topic topic = new Topic(2, keyword, null);
		Packet packet = new Packet(topic, null, PacketConstants.TopicList.toString(), null);
		connectToEventManager(packet);

		//return topic names from event manager
		//select the topic name from the given topics
	}

	public void unsubscribe(Topic topic) {

	}

	public void unsubscribe() {
	}

	public void listSubscribedTopics() {
	}

	public static void publish_helper(){

	}
	public void publish(Event event) {
	}

	public void advertise(Topic newTopic) {
		loadProperties();
		Packet packet_from_advertiser=new Packet(); //how to pass parameters here
		packet_from_advertiser.setTopic(newTopic);
		packet_from_advertiser.setType("advertiser");
		connectToEventManager(packet_from_advertiser); // need to set connectiontoeventmanager to accept packet instead of string
	}

	public static void advertise_helper()
	{
		String topic_name;
		String keyword=null;
		List<String> temp_list = null;
		PubSubAgent pba = new PubSubAgent();
		Scanner topic=new Scanner(System.in);
		System.out.println("Enter the name of Topic");
		topic_name=topic.next();
		Topic T = new Topic();
		T.setName(topic_name);
		while (!(Objects.equals(keyword, "done")))
		{
			System.out.println("Enter the related keywords");
			temp_list.add(keyword);
		}
		T.setKeywords(temp_list);
		pba.advertise(T);
	}
}
