/*
* PubSubAgent.java
*
* Version:
*     $Id$: v 1.1
*
* Revisions:
*     $Log$: Initial Revision
*/

package ds.project1.pub_sub;

import java.io.*;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import ds.project1.commondtos.*;
import ds.project1.ds.project1.common.enums.PacketConstants;
import ds.project1.eventmanager.ConnectionManager;
import ds.project1.eventmanager.dto.*;


/**
 * PubSub Agent consists of methods for advertising a topic, publishing a topic, subscribing a topic,
 * unsubscribing a topic. loadproperties() method initialises the server ip, server port number to an
 * object which is then used by connectToEventManager() to write a packet into the outputstream of
 * Event Manager.
 */
public class PubSubAgent {
	private static SubscriberDto subscriberDto;
	private static Socket socket;

	private static Properties props;

    /**
     *  This method establishes a connection with the event manager. It writes the Packet Object into the
     *  stream of EventManager.
     *
     * @param packet object consisiting details about topic, event, subscriber/publisher/online status
     * @return Packet object consisting of a topic list
     */
	private static Packet connectToEventManager(Packet packet) {
		Packet replyFromServer = null;
		try {
			Socket socket = new Socket(props.getProperty("server.ip"),
					Integer.parseInt(props.getProperty("server.port.number")));
			ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.writeObject(packet);
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

    /**
     * This method loads the properties of application. It insitialises the server.ip & server.port.number
     * in a object which is then used to redirect packet to eventManager.
     */
	private static void loadProperties() {
		try {
			props = new Properties();
			props.load(PubSubAgent.class.getClassLoader().getResourceAsStream("application.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    /**
     * The main method generates a menu on the terminal providing the user with options
     * to publish, advertise or subscribe. Appropriate calls to methods are done.
     * @param args ignored
     */
	public static void main(String[] args) {
		loadProperties();

		Scanner sc = new Scanner(System.in);
		boolean done = false;
		while (!(done)) {
			System.out.println("What am I? \n1. Publisher 2. Subscriber 3. Advertise 0. Quit");
			int ch = sc.nextInt();
			switch (ch) {
			case 1:
				publish_helper();
				break;
			case 2:
				// connectToEventManager("Subscriber");
				PubSubAgent pubSubAgent = new PubSubAgent();
				try {
					loadSubscriberDto();

				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				Packet packet = new Packet(null, null, PacketConstants.SubscriberDto.toString(), subscriberDto);
				Packet replyFromServer = connectToEventManager(packet);
				pubSubAgent.subscribe_helper();
				break;
			case 3:
				advertise_helper();
				break;
			case 0:
				done = true;
				System.out.println("You have quit successfully");
				break;
			default:
				break;
			}
		}
	}

    /**
     *  This method creates and instantiates the object of SubscriberDto class
     * @throws UnknownHostException
     */
	private static void loadSubscriberDto() throws UnknownHostException {
		subscriberDto = new SubscriberDto();
		subscriberDto.setPort(8881);
		subscriberDto.setOnline(true);
		subscriberDto.setGuid("DNS");
		// subscriberDto.setIp(InetAddress.getLocalHost());
	}

    /**
     * This method lets the user subscribe to a topic using the name of topic
     * @param topic object of Topic class consisting of topic name
     */
	private void subscribe(Topic topic) {
		Packet packet = new Packet(topic, null, PacketConstants.TopicList.toString(), subscriberDto);
		Packet replyFromServer = connectToEventManager(packet);
	}

    /**
     * This helper method accepts the user input from subscriber of funcnalities it wants to utilize. viz.,
     * subscribing with a topic name, with keywords, listing all published topics, unsubscribing to one or all topics,
     * The calls to appropriate methods are implemented for execution of the commands.
     *
     */
	private void subscribe_helper() {
		System.out.println("Select 1 of these tasks that you want to do:");
		System.out.println(
				"Press 1 for subscribing to a topic using keywords \nPress 2 for subscribing directly to a topic using it's name \nPress 3 for unsubscribing from a topic \nPress 4 to show all the topics \n Press 5 to unsubscribe from all the topics that you have subscribed for");
		Scanner sc = new Scanner(System.in);
		while (sc.hasNextInt()) {
			switch (sc.nextInt()) {
			case 1:
				System.out.println(
						"Enter the keywords for the topic that you are interested in a single line separated with a comma:");
				String keywords = null;
				while (sc.hasNext()) {
					keywords = sc.nextLine();
				}
				assert keywords != null;
				List<String> items = Arrays.asList(keywords.split("\\s*,\\s*"));
				subscribe(items);

			case 2:
				Packet newPacket = new Packet(null, null, PacketConstants.TopicList.toString(), null);
				Packet recievedPacket = connectToEventManager(newPacket);
				List<Topic> topicList = recievedPacket.getTopicList();

				System.out.println("These are the topics available for you to subscribe :\n");
				for (Topic topic : topicList) {
					System.out.println(topic.getName());
				}
				System.out.println("Enter the topic name you want to subscribe to:");
				String input_topic = sc.next();
				List<Topic> temp_list = topicList.stream().filter(p -> p.getName().equals(input_topic))
						.collect(Collectors.toList());
				if (temp_list.size() > 0) {
					Topic topicSelected = temp_list.get(0);
					subscribe(topicSelected);
				} else {
					System.out.println("Incorrect topic name. Please Try again...");
					subscribe_helper();
				}

			case 3:
				Packet packet = new Packet(null, null, PacketConstants.SubscribedTopics.toString(), subscriberDto);
				Packet subscribedTopics = connectToEventManager(packet);
				List<Topic> subscribedTopicList = subscribedTopics.getTopicList();

				System.out.println("These are the topics you are to subscribed to :\n");
				for (Topic topic : subscribedTopicList) {
					System.out.println(topic.getName());
				}
				System.out.println("Enter the topic you want to unsubscribe from:\n");

				String unsubscribeTopic = null;
				while (sc.hasNext()) {
					unsubscribeTopic = sc.next();
				}
				String finalUnsubscribeTopic = unsubscribeTopic;
				List<Topic> temporary_list = subscribedTopicList.stream()
						.filter(p -> p.getName().equals(finalUnsubscribeTopic)).collect(Collectors.toList());
				if (temporary_list.size() > 0) {
					Topic topicSelected = temporary_list.get(0);
					unsubscribe(topicSelected);
				} else {
					System.out.println(
							"Incorrect topic name. Topic name not in the list of topics that you are subscribed to.\nPlease Try again...");
					subscribe_helper();
				}

			case 4:
				Packet tempPacket = new Packet(null, null, PacketConstants.SubscribedTopics.toString(), subscriberDto);
				Packet allSubscribedTopics = connectToEventManager(tempPacket);
				List<Topic> allSubscribedTopicList = allSubscribedTopics.getTopicList();

				System.out.println("These are the topics you are to subscribed to :\n");
				for (Topic topic : allSubscribedTopicList) {
					System.out.println(topic.getName());
				}
			case 5:
				unsubscribe();
			default:
				System.out.println("Enter correct number again please:");
				subscribe_helper();
			}

		}
	}

    /**
     * This method lets the user subscribe to the topic using a list of interest keywords. A topic list is
     * fetched from the event manager which consists of all topic objects. This list is traversed to fetch
     * only those topic objects which have keywords of interest. The subscriber is provided the option to choose
     * one of the filtered listed topics.
     *
     * @param keyword list of interested keywords
     */
	public void subscribe(List<String> keyword) {
		Packet packet = new Packet(null, null, PacketConstants.TopicList.toString(), null);
		Packet recievedPacket = connectToEventManager(packet);
		List<Topic> topicList = recievedPacket.getTopicList();
		List<Topic> filteredTopic = topicList.stream().filter(p -> !Collections.disjoint(p.getKeywords(), keyword))
				.collect(Collectors.toList());

		System.out.println("These are the topics that you can select from your keywords : ");
		for (Topic topic : filteredTopic) {
			System.out.println(topic.getName());
		}
		System.out.println("Please select one of the topics from this list \n");
		Scanner scanner = new Scanner(System.in);
		String topicSelected = null;
		while (scanner.hasNext()) {
			topicSelected = scanner.next();
		}

		String finalTopicSelected = topicSelected;
		List<Topic> temporary_list = filteredTopic.stream().filter(p -> p.getName().equals(finalTopicSelected))
				.collect(Collectors.toList());
		if (temporary_list.size() > 0) {
			Topic subscribeToThisTopic = temporary_list.get(0);
			Packet packet1 = new Packet(subscribeToThisTopic, null, PacketConstants.SubscriberDto.toString(),
					subscriberDto);
			Packet reply = connectToEventManager(packet1);
		} else {
			System.out.println(
					"Incorrect topic name. Topic name not yet created. Try advertising that topic if you want to. \nPlease Try again...");
			subscribe_helper();
		}
	}

    /**
     * This method lets the user unsubscribe to a topic using the name of the topic
     * @param topic Object of Topic class consisting of topic name to be unsubscribed
     */
	private void unsubscribe(Topic topic) {
		Packet packet = new Packet(topic, null, PacketConstants.UnsubscribeTopic.toString(), subscriberDto);
		Packet replyFromServer = connectToEventManager(packet);
	}

    /**
     * This method unsubscribes all the topics by a subscriber
     */
	private void unsubscribe() {
		Packet unsubscribeAllTopics = new Packet(null, null, PacketConstants.UnsubscribeAll.toString(), subscriberDto);
		connectToEventManager(unsubscribeAllTopics);
	}

	public void listSubscribedTopics() {
	}

    /**
     * This method is a helper publisher method. It generates the object of Event class.
     * It takes the input from the user about the topic details, i.e. the title, content and keywords
     * and instantiates the Event object with  these details. It then calls the publisher method to
     * publish the topic.
     */
	private static void publish_helper() {
		PubSubAgent pubAgent = new PubSubAgent();
		Event E = new Event();
		Scanner pub_helper_sc = new Scanner(System.in);
		Packet publisher_helper = new Packet();

		publisher_helper.setType(PacketConstants.TopicList.toString());
		Packet list_of_objects_of_topics = connectToEventManager(publisher_helper);
		System.out.println("Enter the name of topic you want to publish under");
		for (Topic item : list_of_objects_of_topics.getTopicList()) {
			System.out.println(item.getName());
		}
		String selected_topic = pub_helper_sc.next();
		Topic object_of_selected_topic = null;
		for (Topic item : list_of_objects_of_topics.getTopicList()) {
			if (item.getName().equals(selected_topic)) {
				object_of_selected_topic = item;
			}
		}
		E.setTopic(object_of_selected_topic);

		System.out.println("Enter the title of article");
		E.setTitle(pub_helper_sc.next());

		System.out.println("Enter the content you would like to publish");
		E.setContent(pub_helper_sc.next());
		pubAgent.publish(E);
	}

    /**
     * This method generates a object of PublisherEventManager which opens up a socket for
     * event manager to communicate anytime with the publisher. It then generates the packet
     * out of the event details passed to it from helper function.
     * @param event object with topic details
     */
	private void publish(Event event) {
		PublisherEventManager pem = new PublisherEventManager();
		pem.startThread();
		PublisherDto publisherDto = new PublisherDto();
		Packet response_from_server_to_publisher = null;
		Packet packet_from_publisher = new Packet();
		packet_from_publisher.setTopic(event.getTopic());
		packet_from_publisher.setEvent((event));
		packet_from_publisher.setType(PacketConstants.PublisherDto.toString());
		packet_from_publisher.setAbstractPubSubDto(publisherDto);
		response_from_server_to_publisher = connectToEventManager((packet_from_publisher));
	}

    /**
     * it receives the topic object from the helper function. It then generates the packet object
     *  which is then sent over to event manager.
     * @param newTopic object of class topic which consists of topic details
     */
	private void advertise(Topic newTopic) {
		loadProperties();
		PublisherDto publisherDto = new PublisherDto();
		Packet response_from_server = null;
		Packet packet_from_advertiser = new Packet();
		packet_from_advertiser.setTopic(newTopic);
		packet_from_advertiser.setType(PacketConstants.Topic.toString());
		packet_from_advertiser.setAbstractPubSubDto(publisherDto);
		response_from_server = connectToEventManager(packet_from_advertiser);
	}

	/**
	 * This method creates and instantiates the object of class topic and aceepts user input
     * for topic details. it then passes on the topic object to advertise method.
	 */
	private static void advertise_helper() {
		String topic_name;
		String keyword = null;
		List<String> temp_list = new ArrayList<String>();
		PubSubAgent pba = new PubSubAgent();
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the name of Topic");
		topic_name = sc.next();
		Topic T = new Topic();
		T.setName(topic_name);
		System.out.println("Enter the related keywords");
		do {
			keyword = sc.next();
			if (keyword.equals("done"))
				break;
			else
				temp_list.add(keyword);
		} while (true);
		T.setKeywords(temp_list);
		pba.advertise(T);
	}
}
