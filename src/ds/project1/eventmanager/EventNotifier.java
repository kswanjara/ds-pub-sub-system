package ds.project1.eventmanager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import ds.project1.commondtos.Event;
import ds.project1.commondtos.Packet;
import ds.project1.ds.project1.common.enums.PacketConstants;
import ds.project1.eventmanager.dto.SubscriberDto;

public class EventNotifier implements Runnable {

	private CallBack manager;
	private List<Event> event;
	private List<SubscriberDto> subscribers;

	EventNotifier(CallBack manager, List<Event> eventList, List<SubscriberDto> subscribers) {
		this.manager = manager;
		this.event = eventList;
		this.subscribers = subscribers;
	}

	@Override
	public void run() {
		try {
			Packet packet = new Packet(null, null, PacketConstants.Event.toString(), null);
			packet.setEventList(event);
			for (SubscriberDto subscriberDto : this.subscribers) {
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
