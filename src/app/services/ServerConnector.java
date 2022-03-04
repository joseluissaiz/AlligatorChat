package app.services;


import app.App;
import app.ui.ClientTab;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerConnector extends Thread {

  private App app;
  private ServerSocket serverSocket;
  private List<Connection> connectionList;
  private int portNumber;

  public ServerConnector(App app, int portNumber) {
    this.app = app;
    this.connectionList = app.getConnectionList();
    this.portNumber = portNumber;
  }

  @Override
  public void run() {
    super.run();
    try {
      // Open server socket
      serverSocket = new ServerSocket(portNumber);
      // While socket opened, listen to clients
      while (!serverSocket.isClosed()) {
        Socket client = serverSocket.accept();
        // interrupt //
        // When client connected, establish a full-duplex channel
        Connection channel = new Connection(client);
        //Add the connection into the connections list
        connectionList.add(channel);
        //Insert new tab inside the frame
        ClientTab clientTab = new ClientTab(channel, app.getFrame());
        channel.setTab(clientTab);
        channel.start();
        app.getFrame().createTab(client.getInetAddress().getHostAddress(), clientTab);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
