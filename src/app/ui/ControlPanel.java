package app.ui;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ControlPanel extends JPanel {

  private RFrame frame;
  private JTextField clientField;
  private JButton connectButton;

  public ControlPanel(RFrame frame) {
    this.frame = frame;
    // Set panel params
    setBackground(new Color(0xC2C2C2));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setPreferredSize(new Dimension(300, 0));
    setMaximumSize(new Dimension(300, 0));
    setMinimumSize(new Dimension(300, 0));
    setBorder(new EmptyBorder(10,10,10,10));

    //Crate logo icon
    JLabel logoIconLabel = new JLabel();
    logoIconLabel.setBorder(new EmptyBorder(20,0,0,0));
    logoIconLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
    try {
      logoIconLabel.setIcon(
        new ImageIcon(
          ImageIO.read(
            new File(
                    "src/app/res/img/alligator_icon.png"
            )
          ).getScaledInstance(90,90, 0)
        )
      );
      this.add(logoIconLabel);
    } catch (IOException e) {
      e.printStackTrace();
    }

    //Create client connection fields
    JPanel clientFields = new JPanel();
    clientFields.setLayout(new BoxLayout(clientFields, BoxLayout.X_AXIS));
    clientFields.setBorder(new EmptyBorder(20,0,10,0));
    clientFields.setBackground(null);
    clientFields.setAlignmentX(JPanel.CENTER_ALIGNMENT);
    {
      //Create IP label
      JLabel ipLabel = new JLabel("IP : ");
      clientFields.add(ipLabel);

      //Create client field
      clientField = new JTextField();
      clientField.addActionListener(e -> connect());
      //-->> set min size
      clientField.setPreferredSize(new Dimension(200, 25));
      clientField.setMaximumSize(new Dimension(200,25));
      //Add into control panel
      clientFields.add(clientField);

      //Create connect button
      connectButton = new JButton("Connect");
      connectButton.addActionListener(e -> connect());
      //Add into control panel
      clientFields.add(connectButton);
    }
    //add client fields
    this.add(clientFields);

    //Create end encrypted label
    JLabel endEncryptedLabel = new JLabel("Connection end to end encrypted");
    endEncryptedLabel.setBorder(new EmptyBorder(10,0,0,0));
    endEncryptedLabel.setPreferredSize(new Dimension(200,200));
    endEncryptedLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
    this.add(endEncryptedLabel);

  }

  private void connect() {
    frame.getContext().getClientConnector().connect(clientField.getText());
  }

}
