package ds.project1.eventmanager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import ds.project1.commondtos.Event;
import ds.project1.commondtos.Packet;
import ds.project1.ds.project1.common.enums.PacketConstants;
import ds.project1.eventmanager.dto.SubscriberDto;

public class EventNotifier implements Runnable {

	private static CallBack manager;
	private Event event;
	private List<SubscriberDto> subscribers;

	public EventNotifier(CallBack manager, Event event, List<SubscriberDto> subscribers) {
		this.manager = manager;
		this.event = event;
		this.subscribers = subscribers;
	}

	@Override
	public void run() {
		try {
			Packet packet = new Packet(null, event, PacketConstants.Event.toString(), null);
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
