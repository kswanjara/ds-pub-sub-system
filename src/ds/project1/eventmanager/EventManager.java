package ds.project1.eventmanager;

import java.io.IOException;
import java.io.ObjectInputStream;

import ds.project1.commondtos.ConnectionDetails;
import ds.project1.commondtos.Event;
import ds.project1.commondtos.Topic;
import ds.project1.eventmanager.dto.DataManagerDto;

public class EventManager implements CallBack {

	private static DataManagerDto allData = new DataManagerDto();

	@Override
	public void newConnection(ConnectionDetails connectionDetails) {
		System.out.println("Got new connection details!");
		try {
			// ObjectOutputStream outputStream = new
			// ObjectOutputStream(connectionDetails.getSocket().getOutputStream());
			ObjectInputStream inputStream = new ObjectInputStream(connectionDetails.getSocket().getInputStream());

			String type = (String) inputStream.readObject();
			System.out.println("Connection from :" + type);
			connectionDetails.setType(type);

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
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
		while (true) {

		}
	}

	public static DataManagerDto getAllData() {
		return allData;
	}

}
