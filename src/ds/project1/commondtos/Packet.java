package ds.project1.commondtos;

import ds.project1.eventmanager.dto.AbstractPubSubDto;
import ds.project1.eventmanager.dto.SubscriberDto;

import java.io.Serializable;
import java.util.List;

public class Packet implements Serializable {
	private static final long serialVersionUID = -5741160726800185379L;

	private Topic topic;
	private Event event;
	private String type;
	private AbstractPubSubDto abstractPubSubDto;
	private List<Topic> TopicList;
	private List<Event> eventList;
	private String port;
	private boolean acceptableGuid = false;

	public List<Topic> getTopicList() {
		return TopicList;
	}

	public void setTopicList(List<Topic> topicList) {
		TopicList = topicList;
	}

	public Packet() {

	}

	public Packet(Topic topic, Event event, String type, SubscriberDto subscriberDto) {
		this.event = event;
		this.topic = topic;
		this.type = type;
		this.abstractPubSubDto = subscriberDto;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AbstractPubSubDto getAbstractPubSubDto() {
		return abstractPubSubDto;
	}

	public void setAbstractPubSubDto(AbstractPubSubDto abstractPubSubDto) {
		this.abstractPubSubDto = abstractPubSubDto;
	}

	public List<Event> getEventList() {
		return eventList;
	}

	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public boolean isAcceptableGuid() {
		return acceptableGuid;
	}

	public void setAcceptableGuid(boolean acceptableGuid) {
		this.acceptableGuid = acceptableGuid;
	}
}
