package ds.project1.pub_sub;

import java.io.IOException;
import java.io.ObjectInputStream;

import ds.project1.commondtos.ConnectionDetails;

public class PublisherEventManager {

    public void newConnection(ConnectionDetails connectionDetails) {
        System.out.println("Got new connection details!");
        try {
            // ObjectOutputStream outputStream = new
            // ObjectOutputStream(connectionDetails.getSocket().getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(connectionDetails.getSocket().getInputStream());
            System.out.println("Connection from the Event Manager");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startService() {
        Thread connectionThread = new Thread(new PublisherConnectionManager(new PublisherEventManager()));
        connectionThread.start();
    }

    public void startThread()
    {
        new PublisherEventManager().startService();
        while(true)
        {

        }
    }
}
