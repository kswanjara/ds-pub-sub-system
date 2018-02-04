package ds.project1.commondtos;

import java.io.Serializable;
import java.net.Socket;

public class ConnectionDetails implements Serializable {
	private static final long serialVersionUID = 5587587988116112693L;

	public ConnectionDetails(Socket socket, String type) {
		this.socket = socket;
		this.type = type;
	}

	private Socket socket;
	private String type;

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
