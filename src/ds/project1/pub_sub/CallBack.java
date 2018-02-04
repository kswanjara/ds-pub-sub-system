package ds.project1.pub_sub;

import ds.project1.commondtos.ConnectionDetails;

public interface CallBack {
	void newConnection(ConnectionDetails connectionDetails);
}
