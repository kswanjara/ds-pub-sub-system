/*
 * PublisherEventManager.java
 *
 * Version:
 *     $Id$: v 1.1
 *
 * Revisions:
 *     $Log$: Initial Revision
 */

package ds.project1.pub_sub;

import java.io.IOException;
import java.io.ObjectInputStream;

import ds.project1.commondtos.ConnectionDetails;

/**
 * This program implements the callback interface. a object of this method is used to start a thread,
 * which opens up the socket of the publisher for eventmanager to communicate anytime.
 */
public class PublisherEventManager implements CallBack{

    /**
     * It creates an input stream for the publisher socket & implements the callback interface.
     * @param connectionDetails the object of the class Connection details.
     */
    public void newConnection(ConnectionDetails connectionDetails) {
        System.out.println("Got new connection details!");
        try {
            // ObjectOutputStream outputStream = new
            // ObjectOutputStream(connectionDetails.getSocket().getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(connectionDetails.getSocket().getInputStream());
            System.out.println("Connection from Event Manager");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * it creates a thread and starts it.
     */
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
