package ds.project1.eventmanager;

import ds.project1.commondtos.ConnectionDetails;
import ds.project1.commondtos.Packet;

public interface CallBack {
	public void newConnection(ConnectionDetails connectionDetails);

	public Packet handlePacket(Packet packet);
}
