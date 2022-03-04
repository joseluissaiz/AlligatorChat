package app.services;

import app.ui.ClientTab;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Connection extends Thread {

  private Socket channel;

  private DataInputStream inBand;
  private DataOutputStream outBand;

  private ClientTab tab;

  private HealthSurveyor healthSurveyor;

  private CryptoResolver cryptoResolver;
  private final String key = "<<@@@p>)ñ(¨[3X3c=tp][90q]M0'?'v3(39)@&¬;{@3x1T)/}!poo";


  public Connection(Socket channel) {
    this.channel = channel;
    cryptoResolver = new CryptoResolver();
  }

  @Override
  public void run() {
    super.run();
    try {
      // Creating input data receiver and output data sender
      inBand = new DataInputStream(channel.getInputStream());
      outBand = new DataOutputStream(channel.getOutputStream());
      //Creating health surveyor
      healthSurveyor = new HealthSurveyor(tab.getContext().getClientConnector(), this);
      healthSurveyor.start();
      // While channel is opened, wait until receive data
      while (!channel.isClosed()) {
        String message = inBand.readUTF();
        // interrupt //
        String decryptedMessage = cryptoResolver.decrypt(message, key);
        // if message is ping, advise the health surveyor
        if (decryptedMessage.equals("||ping||!poo")) {
          healthSurveyor.received();
        } else {
          // When data received, display message
          if (tab != null) {
            tab.receiveMessage(decryptedMessage);
          }
        }
      }
    } catch (IOException ignored)
    {
      //Error while receiving if socket is already closed, this means that
      //the connection was closed and just let the program decide what happens
      //later.
    } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
      e.printStackTrace();
    }
  }

  public void sendMessage(String message) {
    try {
      // Send data through the output band
      String encryptedMessage = cryptoResolver.encrypt(message, key);
      outBand.writeUTF(encryptedMessage);
      outBand.flush();
    } catch (IOException ignored) {
      //Cannot send the message, just skip...
    } catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException ea) {
      ea.printStackTrace();
    }
  }

  public void setTab(ClientTab tab) {
    this.tab = tab;
  }

  public Socket getChannel() {
    return this.channel;
  }

  public ClientTab getTab() {
    return this.tab;
  }

}
