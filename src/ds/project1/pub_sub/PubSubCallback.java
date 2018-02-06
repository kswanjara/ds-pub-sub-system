package ds.project1.pub_sub;

import ds.project1.commondtos.ConnectionDetails;
import ds.project1.commondtos.Packet;

public interface PubSubCallback {
	//void newConnection(ConnectionDetails connectionDetails);
	void handleEvent(Packet packet);
}
