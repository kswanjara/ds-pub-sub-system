package ds.project1.eventmanager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import ds.project1.commondtos.Event;
import ds.project1.commondtos.Packet;
import ds.project1.ds.project1.common.enums.PacketConstants;
import ds.project1.eventmanager.dto.AbstractPubSubDto;
import ds.project1.eventmanager.dto.SubscriberDto;

public class EventNotifier implements Runnable {

	private CallBack manager;
	private List<Event> event;
	private List<SubscriberDto> subscribers;
	private List<SubscriberDto> allSubscribers;
	private String eventType = "Event";

	/**
	 * @param subscribers
	 *            the subscribers to set
	 */
	public void setSubscribers(List<SubscriberDto> subscribers) {
		this.subscribers = subscribers;
	}

	/**
	 * @param allSubscribers
	 *            the allSubscribers to set
	 */
	public void setAllSubscribers(List<SubscriberDto> allSubscribers) {
		this.allSubscribers = allSubscribers;
	}

	public EventNotifier(CallBack manager, List<Event> eventList, List<SubscriberDto> subscribers,
			List<SubscriberDto> allSubscribers, String eventType) {
		this.manager = manager;
		this.event = eventList;
		this.allSubscribers = allSubscribers;
		List<SubscriberDto> tempSubscribers = new ArrayList<>();
		if (subscribers != null)
			tempSubscribers.addAll(subscribers);
		this.subscribers = tempSubscribers;
		this.eventType = eventType;
	}

	@Override
	public void run() {
		if (this.eventType.equalsIgnoreCase("Topic")) {
			this.broadcast();
		} else {
			this.notityUser();
		}
	}

	private void broadcast() {
		AbstractPubSubDto dto1 = null;

		try {
			Packet packet = new Packet(null, null, PacketConstants.Topic.toString(), null);
			packet.setEventList(this.event);
			for (AbstractPubSubDto subscriberDto : this.subscribers) {
				dto1 = subscriberDto;
				if (subscriberDto instanceof SubscriberDto) {
					SubscriberDto dto = (SubscriberDto) subscriberDto;
					List<SubscriberDto> existing = allSubscribers.stream()
							.filter(p -> p.getGuid().equals(dto.getGuid())).collect(Collectors.toList());
					if (existing.size() > 0) {
						subscriberDto = existing.get(0);
						if (subscriberDto.isOnline()) {
							// push in the socket output stream
							Socket socket = new Socket(subscriberDto.getIp(), subscriberDto.getPort());
							ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
							outputStream.writeObject(packet);
							outputStream.close();
							socket.close();
						}
					}
				} else {
					Socket socket = new Socket(subscriberDto.getIp(), subscriberDto.getPort());
					ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
					outputStream.writeObject(packet);
					outputStream.close();
					socket.close();
				}
			}
		} catch (IOException e) {
			this.subscribers.remove(dto1);
			this.broadcast();
		}

	}

	public void notityUser() {
		SubscriberDto dto = null;

		Queue<Event> tempQueue = new ArrayDeque<>();
		try {
			Packet packet = new Packet(null, null, PacketConstants.Event.toString(), null);
			for (SubscriberDto subscriberDto : this.subscribers) {
				dto = subscriberDto;
				SubscriberDto dto1 = subscriberDto;
				List<SubscriberDto> existing = allSubscribers.stream().filter(p -> p.getGuid().equals(dto1.getGuid()))
						.collect(Collectors.toList());
				if (existing.size() > 0) {

					subscriberDto = existing.get(0);
					if (subscriberDto.isOnline()) {
						allSubscribers.remove(subscriberDto);

						Event temp[] = {};
						tempQueue = subscriberDto.getSelfQueue();

						List<Event> eventList = Arrays.asList(subscriberDto.getSelfQueue().toArray(temp));
						event.addAll(eventList);
						packet.setEventList(event);

						subscriberDto.setSelfQueue(new ArrayDeque<Event>());
						allSubscribers.add(subscriberDto);

						// push in the socket output stream
						Socket socket = new Socket(subscriberDto.getIp(), subscriberDto.getPort());
						ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
						outputStream.writeObject(packet);
						outputStream.close();
						socket.close();
					} else {
						this.allSubscribers = manager.cacheEventForSubscriber(event, subscriberDto);
					}
				}
			}
			manager.updateAllSubscribers(allSubscribers);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if (dto != null) {
				dto.setSelfQueue(tempQueue);
				this.allSubscribers = manager.cacheEventForSubscriber(event, dto);
				System.out.println("Following exception occured. Cached the event for subscriber.");
				this.subscribers.remove(dto);
			}
			e.printStackTrace();
			this.notityUser();
		}
	}

}
