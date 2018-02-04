package ds.project1.commondtos;

import java.io.Serializable;
import java.util.List;

public class Topic implements Serializable{


	private int id;
	private List<String> keywords;
	private String name;

	public Topic(int id, List<String> keywords, String name){
	    this.id = id;
	    this.keywords = keywords;
	    this.name = name;
    }

    public Topic() {

    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Topic other = (Topic) obj;
        return id == other.id;
    }

	@Override
	public String toString() {
		return "Topic [id=" + id + ", keywords=" + keywords + ", name=" + name + "]";
	}

	public void setName(String name) {
		this.name = name;
	}
}
