package ds.project1.pub_sub;

import java.io.*;
import java.net.Proxy;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;
import ds.project1.commondtos.*;
import ds.project1.ds.project1.common.enums.PacketConstants;
import ds.project1.eventmanager.dto.*;

public class PubSubAgent {

	private static Socket socket;

	private static Properties props;

	private static Packet connectToEventManager(Packet packet) {
		 Packet replyFromServer = null;
		try {
			Socket socket = new Socket(props.getProperty("server.ip"),
					Integer.parseInt(props.getProperty("server.port.number")));

			ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

			try {
				replyFromServer = (Packet) inputStream.readObject();
			} catch (IOException e) {
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
		return replyFromServer;
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
		
		System.out.println("What am I? \n1. Publisher 2. Subscriber 3. Advertise");
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

	public static void publish_helper() {
		PubSubAgent pubAgent = new PubSubAgent();
		Event E = new Event();
		Scanner pub_helper_sc= new Scanner(System.in);
		Packet publisher_helper = new Packet();
		
		
		publisher_helper.setType(PacketConstants.TopicList.toString());
		Packet list_of_objects_of_topics = connectToEventManager(publisher_helper);
		System.out.println("Enter the name of topic you want to publish under");
		for(Topic item : list_of_objects_of_topics.getTopicList()){
			System.out.println(item.getName());
		}
		String selected_topic = pub_helper_sc.next();
		Topic object_of_selected_topic = null;
		for(Topic item : list_of_objects_of_topics.getTopicList()) {
			if (item.getName().equals(selected_topic))
			{
				object_of_selected_topic=item;
			}
		}
		E.setTopic(object_of_selected_topic);

		System.out.println("Enter the title of article");
		E.setTitle(pub_helper_sc.next());

		System.out.println("Enter the content you would like to publish");
		E.setContent(pub_helper_sc.next());
		pubAgent.publish(E);
	}

	public void publish(Event event) {
		PublisherEventManager pem = new PublisherEventManager();
		pem.startThread();
		PublisherDto publisherDto = new PublisherDto();
		Packet response_from_server_to_publisher=null;
		Packet packet_from_publisher = new Packet();
		packet_from_publisher.setTopic(event.getTopic());
		packet_from_publisher.setEvent((event));
		packet_from_publisher.setType(PacketConstants.PublisherDto.toString());
		packet_from_publisher.setAbstractPubSubDto(publisherDto);
		response_from_server_to_publisher = connectToEventManager((packet_from_publisher));
	}

	public void advertise(Topic newTopic) {
		loadProperties();
		PublisherDto publisherDto = new PublisherDto();
		Packet response_from_server = null;
		Packet packet_from_advertiser=new Packet(); //how to pass parameters here
		packet_from_advertiser.setTopic(newTopic);
		packet_from_advertiser.setType(PacketConstants.Topic.toString());
		packet_from_advertiser.setAbstractPubSubDto(publisherDto);
		response_from_server = connectToEventManager(packet_from_advertiser); // need to set connectiontoeventmanager to accept packet instead of string
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
