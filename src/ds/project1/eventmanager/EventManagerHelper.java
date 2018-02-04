package ds.project1.eventmanager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ds.project1.commondtos.Packet;

public class EventManagerHelper implements Runnable {

	private static CallBack manager;
	private static Socket socket;

	public EventManagerHelper(EventManager manager, Socket socket) {
		this.manager = manager;
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			ObjectInputStream inputStream = new ObjectInputStream(this.socket.getInputStream());
			Packet packet;
			packet = (Packet) inputStream.readObject();
			packet = manager.handlePacket(packet);
			ObjectOutputStream outputStream = new ObjectOutputStream(this.socket.getOutputStream());
			outputStream.writeObject(packet);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}