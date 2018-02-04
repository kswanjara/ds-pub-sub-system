package ds.project1.pub_sub;

import ds.project1.commondtos.ConnectionDetails;

public interface CallBack {
	public void newConnection(ConnectionDetails connectionDetails);
}
