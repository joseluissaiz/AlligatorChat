package app.services;

import app.App;
import app.ui.ClientTab;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ClientConnector {

  private App app;

  private List<Connection> connectionList;

  private int portNumber;

  public ClientConnector(App app, int portNumber) {
    this.app = app;
    this.connectionList = app.getConnectionList();
    this.portNumber = portNumber;
  }

  public synchronized void connect(String ip) {
    // Launch a new thread to connect in background.
    new Thread(() -> {
      try {
        // Create a channel with the provided IP address.
        Socket client = new Socket(ip, portNumber);
        // If connected, establish a new full-duplex channel
        Connection channel = new Connection(client);
        //Add the connection into the connections list
        connectionList.add(channel);
        //Insert new tab inside the frame
        ClientTab clientTab = new ClientTab(channel, app.getFrame());
        channel.setTab(clientTab);
        channel.start();
        app.getFrame().createTab(client.getInetAddress().getHostAddress(), clientTab);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();
  }

  public App getContext() {
    return app;
  }

}
