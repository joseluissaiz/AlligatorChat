package app.ui;

import app.App;
import app.services.Connection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class ClientTab extends JPanel {

  private Connection connection;
  private RFrame frame;

  private JTextArea textArea;
  private JTextField messageField;
  private JButton sendButton;

  private JScrollPane healthScrollPane;
  private JTextArea healthTextArea;
  private JLabel latencyLabel;

  public ClientTab(Connection connection, RFrame frame) {
    this.connection = connection;
    this.frame = frame;
    // Set panel params
    setLayout(new GridBagLayout());

    //Create health area
    JPanel healthPanel = new JPanel();
    healthPanel.setLayout(new BoxLayout(healthPanel, BoxLayout.Y_AXIS));
    healthPanel.setPreferredSize(new Dimension(200,200));
    healthPanel.setMinimumSize(new Dimension(200,200));
    healthPanel.setBorder(new EmptyBorder(0,20,20,20));
    {
      //Create health label
      JLabel healthLabel = new JLabel("Health monitor");
      healthPanel.add(healthLabel);

      //Create health text area
      healthScrollPane = new JScrollPane();
      healthScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
      healthTextArea = new JTextArea();
      healthTextArea.setEditable(false);
      healthTextArea.setBackground(Color.BLACK);
      healthTextArea.setForeground(Color.GREEN);
      healthTextArea.setFont(healthTextArea.getFont().deriveFont(10f));
      healthScrollPane.getViewport().add(healthTextArea);
      healthPanel.add(healthScrollPane);

      //Create latency label
      latencyLabel = new JLabel("Latency:                    0 ms");
      healthPanel.add(latencyLabel);
    }
    GridBagConstraints healthPanelConstraints = new GridBagConstraints();
    healthPanelConstraints.gridy = 1;
    healthPanelConstraints.gridx = 0;
    healthPanelConstraints.weightx = 0f;
    healthPanelConstraints.weighty = 1f;
    healthPanelConstraints.anchor = GridBagConstraints.NORTH;
    this.add(healthPanel, healthPanelConstraints);

    //Create close button
    JButton closeButton = new JButton("X");
    closeButton.addActionListener(e -> close());
    GridBagConstraints closeButtonConstraints = new GridBagConstraints();
    closeButtonConstraints.gridx = 0;
    closeButtonConstraints.gridy = 0;
    closeButtonConstraints.weightx = 0f;
    closeButtonConstraints.weighty = 0f;
    closeButtonConstraints.gridwidth = 2;
    closeButtonConstraints.anchor = GridBagConstraints.EAST;
    this.add(closeButton, closeButtonConstraints);

    //Create textArea
    JScrollPane textScrollPane = new JScrollPane();
    textArea = new JTextArea();
    textArea.setEditable(false);
    textScrollPane.getViewport().add(textArea);
    GridBagConstraints textAreaConstraints = new GridBagConstraints();
    textAreaConstraints.gridx = 1;
    textAreaConstraints.gridy = 1;
    textAreaConstraints.weightx = 1f;
    textAreaConstraints.weighty = 1f;
    textAreaConstraints.fill = GridBagConstraints.BOTH;
    this.add(textScrollPane, textAreaConstraints);

    //Create sendMessage panel
    JPanel sendMessagePanel = new JPanel();
    sendMessagePanel.setLayout(new GridBagLayout());
    sendMessagePanel.setBorder(new EmptyBorder(0,0,50,0));
    sendMessagePanel.setBackground(new Color(0xC2C2C2));
    {
      //Create messageField
      messageField = new JTextField();
      messageField.setPreferredSize(new Dimension(0,25));
      messageField.setMaximumSize(new Dimension(0, 25));
      messageField.addActionListener(e -> sendMessage());
      GridBagConstraints messageFieldConstraints = new GridBagConstraints();
      messageFieldConstraints.gridx = 0;
      messageFieldConstraints.gridy = 0;
      messageFieldConstraints.weighty = 0;
      messageFieldConstraints.weightx = 0.9f;
      messageFieldConstraints.fill = GridBagConstraints.BOTH;
      //add to send panel
      sendMessagePanel.add(messageField, messageFieldConstraints);

      //Create sendButton
      sendButton = new JButton("Send");
      sendButton.addActionListener(e -> sendMessage());
      GridBagConstraints sendButtonConstraints = new GridBagConstraints();
      sendButtonConstraints.gridx = 1;
      sendButtonConstraints.gridy = 0;
      sendButtonConstraints.weightx = 0.1f;
      sendButtonConstraints.weighty = 0f;
      sendButtonConstraints.fill = GridBagConstraints.BOTH;
      //add to send panel
      sendMessagePanel.add(sendButton, sendButtonConstraints);
    }
    GridBagConstraints sendMessagePanelConstraints = new GridBagConstraints();
    sendMessagePanelConstraints.gridx = 1;
    sendMessagePanelConstraints.gridy = 2;
    sendMessagePanelConstraints.weightx = 1f;
    sendMessagePanelConstraints.weighty = 0f;
    sendMessagePanelConstraints.fill = GridBagConstraints.BOTH;
    this.add(sendMessagePanel, sendMessagePanelConstraints);

  }

  private void sendMessage() {
    connection.sendMessage(messageField.getText());
    textArea.setText(textArea.getText()+
            "Me: "+messageField.getText()+"\n"
    );
    writeDebug("-------------------<s>");
    messageField.setText("");
  }

  public void receiveMessage(String message) {
    textArea.setText(textArea.getText()+
            connection.getChannel().getInetAddress().getHostAddress()+": "+message+"\n"
    );
    writeDebug("-------------------<r>");
  }

  public void close() {
    try {
      connection.setTab(null);
      connection.getChannel().close();
      frame.getTabPane().remove(this);
    } catch (IOException ignored) {
      //Throws exception if the socket is already closed, not a problem.
    }
  }

  public void writeDebug(String message) {
    healthTextArea.setText(healthTextArea.getText()+message+"\n");
    healthScrollPane.getVerticalScrollBar().setValue(
            healthScrollPane.getVerticalScrollBar().getMaximum()
    );
  }

  public void setLatency(double latency) {
    latencyLabel.setText("Latency:                    "+latency+" ms");
  }

  public App getContext() {
    return frame.getContext();
  }

}
