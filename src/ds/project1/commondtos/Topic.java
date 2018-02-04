package ds.project1.commondtos;

import java.io.Serializable;
import java.util.List;

public class Topic implements Serializable {
	private static final long serialVersionUID = 1545135876274538840L;

	private int id;
	private List<String> keywords;
	private String name;

	public Topic(int id, List<String> keywords, String name) {
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Topic other = (Topic) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Topic [id=" + id + ", keywords=" + keywords + ", name=" + name + "]";
	}

	public void setName(String name) {
		this.name = name;
	}
}
