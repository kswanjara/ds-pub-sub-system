package ds.project1.eventmanager;

import ds.project1.commondtos.ConnectionDetails;
import ds.project1.commondtos.Event;
import ds.project1.commondtos.Packet;
import ds.project1.commondtos.Topic;
import ds.project1.ds.project1.common.enums.PacketConstants;
import ds.project1.eventmanager.dto.AbstractPubSubDto;
import ds.project1.eventmanager.dto.DataManagerDto;
import ds.project1.eventmanager.dto.PublisherDto;
import ds.project1.eventmanager.dto.SubscriberDto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.stream.Collectors;

public class EventManager implements CallBack {

	private static DataManagerDto allData = new DataManagerDto();

	@Override
	public void newConnection(ConnectionDetails connectionDetails) {
		System.out.println("Got new connection details!");
		try {
			// ObjectOutputStream outputStream = new
			// ObjectOutputStream(connectionDetails.getSocket().getOutputStream());
			ObjectInputStream inputStream = new ObjectInputStream(connectionDetails.getSocket().getInputStream());

			String type = (String) inputStream.readObject();
			System.out.println("Connection from :" + type);
			connectionDetails.setType(type);

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Start the repo service
	 */
	private void startService() {
		Thread connectionThread = new Thread(new ConnectionManager(new EventManager()));
		connectionThread.start();
	}

	/*
	 * notify all subscribers of new event
	 */
	private void notifySubscribers(Event event) {
		List<Event> tempList = new ArrayList<Event>();
		tempList.add(event);
		new Thread(new EventNotifier(new EventManager(), tempList, getAllData().getTopicDetails().get(event.getTopic()),
				getAllData().getSubscriberList())).start();
	}

	/*
	 * add new topic when received advertisement of new topic
	 */
	private void addTopic(Topic topic) {
		Map<Topic, List<SubscriberDto>> map = getAllData().getTopicDetails();
		map.put(topic, new ArrayList<SubscriberDto>());
		getAllData().setTopicDetails(map);
	}

	/*
	 * add subscriber to the internal list
	 */
	private Packet addSubscriber(AbstractPubSubDto abstractPubSubDto, Packet packet) {
		SubscriberDto dto = (SubscriberDto) abstractPubSubDto;
		if (!getAllData().getSubscriberList().contains(dto)) {
			dto.setSelfQueue(new ArrayDeque<Event>());
			getAllData().getSubscriberList().add(dto);
		} else {
			List<SubscriberDto> list = getAllData().getSubscriberList().stream()
					.filter(p -> p.getGuid().equals(abstractPubSubDto.getGuid())).collect(Collectors.toList());
			if (list.size() == 1) {

				SubscriberDto existingDto = list.get(0);
				list.remove(0);

				getAllData().getSubscriberList().remove(existingDto);

				existingDto.setIp(dto.getIp());
				existingDto.setPort(dto.getPort());
				existingDto.setOnline(dto.isOnline());

				list.add(existingDto);

				if (dto.isOnline() && !existingDto.getSelfQueue().isEmpty()) {
					Event temp[] = {};
					List<Event> eventList = Arrays.asList(existingDto.getSelfQueue().toArray(temp));
					packet.setEventList(eventList);
					// new Thread(new EventNotifier(new EventManager(), eventList, list)).start();
				}
				getAllData().getSubscriberList().add(existingDto);
			}
		}
		return packet;
	}

	/*
	 * remove subscriber from the list
	 */
	private void removeSubscriber(AbstractPubSubDto abstractPubSubDto) {
		getAllData().getSubscriberList().remove((SubscriberDto) abstractPubSubDto);
	}

	/*
	 * show the list of subscriber for a specified topic
	 */
	private void showSubscribers(Topic topic) {
		List<SubscriberDto> sublist = getAllData().getTopicDetails().get(topic);
		System.out.println("Subscribers for Topic : " + topic.getName());
		int counter = 1;
		for (SubscriberDto subscriberDto : sublist) {
			System.out.println(counter++ + subscriberDto.getGuid());
		}
	}

	public static void main(String[] args) {
		new EventManager().startService();
	}

	private static DataManagerDto getAllData() {
		return allData;
	}

	private static void subscribeToTopic(AbstractPubSubDto abstractPubSubDto, Topic topic) {
		List<SubscriberDto> list = getAllData().getSubscriberList().stream()
				.filter(p -> p.getGuid().equals(abstractPubSubDto.getGuid())).collect(Collectors.toList());
		if (list.size() == 1) {
			SubscriberDto dto = list.get(0);
			list = getAllData().getTopicDetails().get(topic);
			list.add(dto);
			getAllData().getTopicDetails().put(topic, list);
		}
	}

	private static Set<Topic> getAllTopics() {
		return getAllData().getTopicDetails().keySet();
	}

	@Override
	public Packet handlePacket(Packet packet) {
		if (packet != null) {
			if (packet.getType() != null && !packet.getType().trim().equals("")) {
				if (packet.getType().trim().equals(PacketConstants.Topic.toString())) {
					// check what is the type of object in abstractPubSubDto
					if (packet.getAbstractPubSubDto() != null
							&& packet.getAbstractPubSubDto() instanceof PublisherDto) {
						// if Pub - advertise
						addTopic(packet.getTopic());
					} else if (packet.getAbstractPubSubDto() != null
							&& packet.getAbstractPubSubDto() instanceof SubscriberDto) {
						// if Sub - subscribe
						subscribeToTopic(packet.getAbstractPubSubDto(), packet.getTopic());
					}
				} else if (packet.getType().trim().equals(PacketConstants.Event.toString())) {
					// Event published - Notify subscriber
					notifySubscribers(packet.getEvent());
				} else if (packet.getType().trim().equals(PacketConstants.PublisherDto.toString())) {
					// Publisher notifying about the status
					addPublisher(packet.getAbstractPubSubDto());
				} else if (packet.getType().trim().equals(PacketConstants.SubscriberDto.toString())) {
					// Subscriber notifying about the status
					packet = addSubscriber(packet.getAbstractPubSubDto(), packet);
				} else if (packet.getType().trim().equals(PacketConstants.UnsubscribeTopic.toString())) {
					// remove subscriber from a particular topic
					unsubscribeTopic(packet.getTopic(), (SubscriberDto) packet.getAbstractPubSubDto());
				} else if (packet.getType().trim().equals(PacketConstants.UnsubscribeAll.toString())) {
					// remove subscriber from a all topics
					unsubscribeAll((SubscriberDto) packet.getAbstractPubSubDto());
				} else if (packet.getType().trim().equals(PacketConstants.SubscribedTopics.toString())) {
					// return the list of all subscribed topics
					packet.setTopicList(getSubscribedTopicsList((SubscriberDto) packet.getAbstractPubSubDto()));
				} else if (packet.getType().trim().equals(PacketConstants.TopicList.toString())) {
					// return the list of all topics
					Set<Topic> set = getAllTopics();
					List<Topic> list = new ArrayList<>(set);
					packet.setTopicList(list);
				}
			}
		}
		return packet;
	}

	private List<Topic> getSubscribedTopicsList(SubscriberDto subDto) {
		// TODO Auto-generated method stub
		Set<Topic> set = getAllTopics();
		List<Topic> subscribedTopics = new ArrayList<>();

		for (Topic topic : set) {
			if (getAllData().getTopicDetails().get(topic).contains(subDto)) {
				subscribedTopics.add(topic);
			}
		}

		return subscribedTopics;
	}

	private void unsubscribeAll(SubscriberDto subDto) {
		// TODO Auto-generated method stub
		Set<Topic> set = getAllTopics();
		List<Topic> subscribedTopics = new ArrayList<>();

		for (Topic topic : set) {
			getAllData().getTopicDetails().get(topic).remove(subDto);
		}
	}

	private void unsubscribeTopic(Topic topic, SubscriberDto subDto) {
		// TODO Auto-generated method stub
		getAllData().getTopicDetails().get(topic).remove(subDto);
	}

	private void addPublisher(AbstractPubSubDto abstractPubSubDto) {
		// TODO Auto-generated method stub
	}

	@Override
	public void cacheEventForSubscriber(List<Event> events, SubscriberDto dto) {
		if (getAllData().getSubscriberList().contains(dto)) {
			List<SubscriberDto> list = getAllData().getSubscriberList().stream()
					.filter(p -> p.getGuid().equals(dto.getGuid())).collect(Collectors.toList());
			if (list.size() == 1) {
				SubscriberDto existingDto = list.get(0);

				getAllData().getSubscriberList().remove(existingDto);

				existingDto.setOnline(false);
				existingDto.getSelfQueue().addAll(events);

				getAllData().getSubscriberList().add(existingDto);
			}
		}
	}

}
