package ds.project1.eventmanager;

import ds.project1.commondtos.ConnectionDetails;
import ds.project1.commondtos.Event;
import ds.project1.commondtos.Topic;

public class EventManager implements CallBack {

	@Override
	public void newConnection(ConnectionDetails connectionDetails) {
		System.out.println("Got new connection details!");
	}

	/*
	 * Start the repo service
	 */
	private void startService() {
		Thread connectionThread = new Thread(new ConnectionManager(new EventManager()));
		connectionThread.start();
	}

	/*
	 * notify all subscribers of new event
	 */
	private void notifySubscribers(Event event) {

	}

	/*
	 * add new topic when received advertisement of new topic
	 */
	private void addTopic(Topic topic) {

	}

	/*
	 * add subscriber to the internal list
	 */
	private void addSubscriber() {

	}

	/*
	 * remove subscriber from the list
	 */
	private void removeSubscriber() {

	}

	/*
	 * show the list of subscriber for a specified topic
	 */
	private void showSubscribers(Topic topic) {

	}

	public static void main(String[] args) {
		new EventManager().startService();
		while(true) {
			
		}
	}

}
