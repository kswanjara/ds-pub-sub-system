package ds.project1.eventmanager;

import java.util.List;

import ds.project1.commondtos.ConnectionDetails;
import ds.project1.commondtos.Event;
import ds.project1.commondtos.Packet;
import ds.project1.eventmanager.dto.SubscriberDto;

public interface CallBack {
	public void newConnection(ConnectionDetails connectionDetails);

	public Packet handlePacket(Packet packet);

	public void cacheEventForSubscriber(List<Event> events, SubscriberDto subscriberDto);
}
