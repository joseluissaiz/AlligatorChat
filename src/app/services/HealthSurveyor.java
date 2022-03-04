package app.services;


public class HealthSurveyor extends Thread {

  private final Connection connection;
  private final ClientConnector clientConnector;
  private long lastPing;
  private double latency;

  private final int timeOutMillis = 2000;

  public HealthSurveyor(ClientConnector clientConnector, Connection connection) {
    this.clientConnector = clientConnector;
    this.connection = connection;
  }

  @Override
  public void run() {
    super.run();
    lastPing = System.currentTimeMillis();
    latency = 0;

    while (true) {
      //normal execution of the program
      while (System.currentTimeMillis()-timeOutMillis < lastPing) {
        sendPing();
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      //if not responding try to reconnect
      while (System.currentTimeMillis()-(timeOutMillis*2) < lastPing) {
        if (System.currentTimeMillis()-(timeOutMillis*2) > lastPing) {
          connection.getTab().close();
        } else {
          try {
            connection.getTab().close();
            sendPing();
            clientConnector.connect(connection.getChannel().getInetAddress().getHostAddress());
            break;
          } catch (NullPointerException e) {
            //Throws exeption if both sides are disconnected (close connection)
            break;
          }
        }
        try {
          Thread.sleep(500);
        } catch (InterruptedException ignored) {
          //Throws exception if the socket is already closed, nothing to worry
          //about.
        }
      }
      if (connection.getTab() == null) {
        //if connection closed, break all loops and delete connection
        System.out.println("Connection closed.");
        clientConnector.getContext().getConnectionList().remove(connection);
        break;
      }
    }
  }

  private void sendPing() {
    connection.sendMessage("||ping||!poo");
    if (connection.getTab() != null) {
      connection.getTab().writeDebug(">>> sended");
    }
  }

  public void received() {
    latency = (double) ((System.currentTimeMillis() - lastPing)-500);
    lastPing = System.currentTimeMillis();
    if (connection.getTab() != null) {
      connection.getTab().setLatency(latency);
      connection.getTab().writeDebug("--------<<< received");
    }
  }

}
