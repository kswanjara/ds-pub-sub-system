package ds.project1.commondtos;

import ds.project1.eventmanager.dto.AbstractPubSubDto;
import ds.project1.eventmanager.dto.SubscriberDto;

import java.io.Serializable;

public class Packet implements Serializable{
    private Topic topic;
    private Event event;
    private String type;
    private AbstractPubSubDto abstractPubSubDto;

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
}
