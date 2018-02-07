package ds.project1.eventmanager.dto;

import java.io.Serializable;
import java.util.Queue;

import ds.project1.commondtos.Event;

public class SubscriberDto extends AbstractPubSubDto implements Serializable {
	private static final long serialVersionUID = -3040596971573938805L;

	private Queue<Event> selfQueue;

	public Queue<Event> getSelfQueue() {
		return selfQueue;
	}

	public void setSelfQueue(Queue<Event> selfQueue) {
		this.selfQueue = selfQueue;
	}

	@Override
	public String toString() {
		return "SubscriberDto [ Guid=" + super.getGuid() + "]";
	}

}
