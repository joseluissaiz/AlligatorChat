package app;

import app.services.ClientConnector;
import app.services.Connection;
import app.services.ServerConnector;
import app.ui.RFrame;

import java.util.ArrayList;
import java.util.List;

public class App {

  private final int portNumber = 7070;

  private ServerConnector serverConnector;
  private ClientConnector clientConnector;
  private List<Connection> connectionList;
  private RFrame frame;

  public App() {
    // Creating services
    connectionList = new ArrayList<>();
    serverConnector = new ServerConnector(this, portNumber);
    clientConnector = new ClientConnector(this, portNumber);

    // Creating window
    frame = new RFrame(this);
  }

  public void run() {
    // Run all services
    serverConnector.start();
    // Show window
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    // Create app instance and run
    App app = new App();
    app.run();
  }

  public List<Connection> getConnectionList() {
    return this.connectionList;
  }

  public RFrame getFrame() {
    return this.frame;
  }

  public ClientConnector getClientConnector() {
    return clientConnector;
  }

}
