package ds.project1.eventmanager;

import java.util.Scanner;

/**
 * Class is used to display all the subscribers on evnt manager
 */
public class EventManagerCommunicator implements Runnable {

	private static EventManager manager;

	public EventManagerCommunicator(EventManager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		while (true) {
			System.out.println("Press list to list all the subscribers");
			Scanner sc = new Scanner(System.in);
			if (sc.next().equals("list")) {
				manager.showSubscriberList();
			}
		}
	}

}
