package ds.project1.commondtos;

import ds.project1.eventmanager.dto.AbstractPubSubDto;
import ds.project1.eventmanager.dto.SubscriberDto;

import java.io.Serializable;
import java.util.List;

public class Packet implements Serializable{
    private Topic topic;
    private Event event;
    private String type;
    private AbstractPubSubDto abstractPubSubDto;

    public List<String> getTopicList() {
        return TopicList;
    }

    public void setTopicList(List<String> topicList) {
        TopicList = topicList;
    }

    private List<String> TopicList;

    public Packet(){

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
}
