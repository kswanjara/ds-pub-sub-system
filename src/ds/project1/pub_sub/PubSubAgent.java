package ds.project1.pub_sub;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

import ds.project1.commondtos.*;
import ds.project1.ds.project1.common.enums.PacketConstants;
import ds.project1.eventmanager.dto.*;

public class PubSubAgent implements PubSubCallback {
    private static SubscriberDto subscriberDto;
    private static Socket socket;

    private static Properties props;

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
            } catch (IOException | ClassNotFoundException e) {
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
            String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(""))
                    .getPath();
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
                        listenToEventManager();
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

    private static void listenToEventManager() {
        new Thread(new SubscriberConnectionManager(new PubSubAgent())).start();
    }

    private static void loadSubscriberDto() throws UnknownHostException {
        subscriberDto = new SubscriberDto();
        subscriberDto.setPort(8881);
        subscriberDto.setOnline(true);
        subscriberDto.setGuid("DNS");
        subscriberDto.setIp(InetAddress.getLocalHost());
    }

    private void subscribe(Topic topic) {
        Packet packet = new Packet(topic, null, PacketConstants.TopicList.toString(), subscriberDto);
        Packet replyFromServer = connectToEventManager(packet);
        if(replyFromServer != null){
            System.out.println("Successfully subscribed to"+ topic.getName());
        }else{
            System.out.println("Some error occurred :( \n Try again ...");
            subscribe_helper();
        }
    }

    private void subscribe_helper() {
        boolean done = false;
        while (!done) {
            System.out.println("Select 1 of these tasks that you want to do:");
            System.out.println(
                    "Press 1 for subscribing to a topic using keywords \n" +
                            "Press 2 for subscribing directly to a topic using it's name \n" +
                            "Press 3 for unsubscribing from a topic \n" +
                            "Press 4 to show all the topics \n" +
                            "Press 5 to unsubscribe from all the topics that you have subscribed for \n" +
                            "Press 6 to quit \n");
            Scanner sc = new Scanner(System.in);
            while (sc.hasNextInt()) {
                switch (sc.nextInt()) {
                    case 1:
                        System.out.println(
                                "Enter the keywords for the topic that you are interested in a single line separated with a comma:");
                        String keywords = null;
                        while (sc.hasNext()) {
                            keywords = sc.nextLine().toLowerCase();
                        }
                        assert keywords != null;
                        List<String> items = Arrays.asList(keywords.split("\\s*,\\s*"));
                        subscribe(items);

                    case 2:
                        Packet newPacket = new Packet(null, null, PacketConstants.TopicList.toString(), null);
                        Packet recievedPacket = connectToEventManager(newPacket);
                        List<Topic> topicList = recievedPacket.getTopicList();

                        System.out.println("These are the topics available for you to subscribe :");
                        for (Topic topic : topicList) {
                            System.out.println(topic.getName());
                        }
                        System.out.println("Enter the topic name you want to subscribe to:");
                        String input_topic = sc.next().toLowerCase();
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
                        List<Topic> subscribedTopicList = listSubscribedTopics();

                        System.out.println("Enter the topic you want to unsubscribe from:\n");

                        String unsubscribeTopic = null;
                        while (sc.hasNext()) {
                            unsubscribeTopic = sc.next().toLowerCase();
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
                        listSubscribedTopics();
                        subscribe_helper();
                    case 5:
                        unsubscribe();
                        subscribe_helper();
                    case 6:
                        SubscriberDto subscriberDto1 = new SubscriberDto();
                        subscriberDto1.setOnline(false);
                        Packet packet = new Packet(null, null, PacketConstants.SubscriberDto.toString(), subscriberDto1);
                        done = true;
                        System.out.println("Bye Bye Subscriber...");
                    default:
                        System.out.println("Enter correct number again please:");
                        subscribe_helper();
                }

            }
        }
    }

    private void subscribe(List<String> keyword) {
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

    private void unsubscribe(Topic topic) {
        Packet packet = new Packet(topic, null, PacketConstants.UnsubscribeTopic.toString(), subscriberDto);
        Packet replyFromServer = connectToEventManager(packet);
    }

    private void unsubscribe() {
        Packet unsubscribeAllTopics = new Packet(null, null, PacketConstants.UnsubscribeAll.toString(), subscriberDto);
        connectToEventManager(unsubscribeAllTopics);
    }

    private List<Topic> listSubscribedTopics() {
        Packet tempPacket = new Packet(null, null, PacketConstants.SubscribedTopics.toString(), subscriberDto);
        Packet allSubscribedTopics = connectToEventManager(tempPacket);
        List<Topic> allSubscribedTopicList = allSubscribedTopics.getTopicList();

        System.out.println("These are the topics you are to subscribed to :\n");
        for (Topic topic : allSubscribedTopicList) {
            System.out.println(topic.getName());
        }
        return allSubscribedTopicList;
    }

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

    private void advertise(Topic newTopic) {
//        loadProperties();
        PublisherDto publisherDto = new PublisherDto();
        Packet response_from_server = null;
        Packet packet_from_advertiser = new Packet(); // how to pass parameters here
        packet_from_advertiser.setTopic(newTopic);
        packet_from_advertiser.setType(PacketConstants.Topic.toString());
        packet_from_advertiser.setAbstractPubSubDto(publisherDto);
        response_from_server = connectToEventManager(packet_from_advertiser); // need to set connectiontoeventmanager to
        // accept packet instead of string
    }

    private static void advertise_helper() {
        String topic_name;
        String keyword;
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

    @Override
    public void handleEvent(Packet packet) {
        List<Event> eventList = packet.getEventList();
        if (eventList != null && eventList.size() > 0) {
            System.out.println("Event Notifications : \n");
            for (Event event : eventList) {
                System.out.println("Title : " + event.getTitle());
                System.out.println("Content : " + event.getContent());
            }
        }
    }
}
