package ds.project1.commondtos;

import java.io.Serializable;

public class Event implements Serializable {
	private static final long serialVersionUID = -2896628416781183580L;

	private int id;
	private Topic topic;
	private String title;
	private String content;

	private String type = "Event";

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
