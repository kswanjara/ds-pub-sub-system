package ds.project1.eventmanager.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ds.project1.commondtos.Topic;

public class DataManagerDto implements Serializable {
	private static final long serialVersionUID = 6066828238461586412L;

	private Map<Topic, List<SubscriberDto>> topicDetails = new HashMap<Topic, List<SubscriberDto>>();
	private List<SubscriberDto> subscriberList = new ArrayList<SubscriberDto>();
	private List<PublisherDto> publisherDto = new ArrayList<PublisherDto>();

	public Map<Topic, List<SubscriberDto>> getTopicDetails() {
		return topicDetails;
	}

	public void setTopicDetails(Map<Topic, List<SubscriberDto>> topicDetails) {
		this.topicDetails = topicDetails;
	}

	public List<SubscriberDto> getSubscriberList() {
		return subscriberList;
	}

	public void setSubscriberList(List<SubscriberDto> subscriberList) {
		this.subscriberList = subscriberList;
	}

	public List<PublisherDto> getPublisherDto() {
		return publisherDto;
	}

	public void setPublisherDto(List<PublisherDto> publisherDto) {
		this.publisherDto = publisherDto;
	}

}
