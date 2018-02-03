package ds.project1.commondtos;

import ds.project1.eventmanager.dto.SubscriberDto;

import java.io.Serializable;

public class Packet implements Serializable{
    private Topic topic;
    private Event event;
    private String type;
    private SubscriberDto subscriberDto;

    public Packet(Topic topic, Event event, String type, SubscriberDto subscriberDto) {
        this.topic = topic;
        this.event = event;
        this.type = type;
        this.subscriberDto = subscriberDto;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public SubscriberDto getSubscriberDto() {
        return subscriberDto;
    }

    public void setSubscriberDto(SubscriberDto subscriberDto) {
        this.subscriberDto = subscriberDto;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
