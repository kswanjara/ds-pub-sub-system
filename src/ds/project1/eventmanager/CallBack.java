package ds.project1.eventmanager;

import ds.project1.commondtos.ConnectionDetails;
import ds.project1.commondtos.Packet;

public interface CallBack {
	void newConnection(ConnectionDetails connectionDetails);

	Packet handlePacket(Packet packet);
}
