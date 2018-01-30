package ds.project1.eventmanager;

import ds.project1.commondtos.ConnectionDetails;

public interface CallBack {
	public void newConnection(ConnectionDetails connectionDetails);
}
