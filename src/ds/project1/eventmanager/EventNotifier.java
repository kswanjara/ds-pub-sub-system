package ds.project1.eventmanager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ds.project1.commondtos.Event;
import ds.project1.commondtos.Packet;
import ds.project1.ds.project1.common.enums.PacketConstants;
import ds.project1.eventmanager.dto.SubscriberDto;

public class EventNotifier implements Runnable {

	private CallBack manager;
	private List<Event> event;
	private List<SubscriberDto> subscribers;
	private List<SubscriberDto> allSubscribers;

	public EventNotifier(CallBack manager, List<Event> eventList, List<SubscriberDto> subscribers,
			List<SubscriberDto> allSubscribers) {
		this.manager = manager;
		this.event = eventList;
		this.subscribers = subscribers;
		this.allSubscribers = allSubscribers;
	}

	@Override
	public void run() {
		SubscriberDto dto = null;
		try {
			Packet packet = new Packet(null, null, PacketConstants.Event.toString(), null);
			packet.setEventList(event);
			for (SubscriberDto subscriberDto : this.subscribers) {
				dto = subscriberDto;
				SubscriberDto dto1 = subscriberDto;
				List<SubscriberDto> existing = allSubscribers.stream().filter(p -> p.getGuid().equals(dto1.getGuid()))
						.collect(Collectors.toList());
				if (existing.size() > 0) {
					subscriberDto = existing.get(0);
					if (subscriberDto.isOnline()) {
						// push in the socket output stream
						Socket socket = new Socket(subscriberDto.getIp(), subscriberDto.getPort());
						ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
						outputStream.writeObject(packet);
						outputStream.close();
						socket.close();
					} else {
						manager.cacheEventForSubscriber(event, subscriberDto);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if (dto != null) {
				manager.cacheEventForSubscriber(event, dto);
				System.out.println("Following exception occured. Cached the event for subscriber.");
			}
			e.printStackTrace();
		}
	}

}
