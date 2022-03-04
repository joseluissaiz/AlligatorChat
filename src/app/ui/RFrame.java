package app.ui;


import app.App;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class RFrame extends JFrame {

  private App app;

  private ControlPanel controlPanel;
  private JTabbedPane tabbedPane;

  public RFrame(App app) {
    this.app = app;
    // Decoration config
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setTitle("Alligator v1.0");
    try {
      setIconImage(ImageIO.read(new File("src/app/res/img/alligator_icon.png")));
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Frame params
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int frameWidth = (int) (screenSize.width * 0.8);
    int frameHeight = (int) (screenSize.height * 0.8);
    setSize(frameWidth, frameHeight);
    setLocationRelativeTo(null);
    setLayout(new GridBagLayout());

    //Tabbed pane
    tabbedPane = new JTabbedPane();
    GridBagConstraints tabbedPaneConstraints = new GridBagConstraints();
    tabbedPaneConstraints.gridx = 0;
    tabbedPaneConstraints.gridy = 0;
    tabbedPaneConstraints.weightx = 1f;
    tabbedPaneConstraints.weighty = 1f;
    tabbedPaneConstraints.fill = GridBagConstraints.BOTH;
    //add inside frame
    this.add(tabbedPane, tabbedPaneConstraints);

    // Create control panel
    controlPanel = new ControlPanel(this);
    GridBagConstraints cpanelConstraints = new GridBagConstraints();
    cpanelConstraints.gridx = 1;
    cpanelConstraints.gridy = 0;
    cpanelConstraints.weightx = 0f;
    cpanelConstraints.weighty = 1f;
    cpanelConstraints.fill = GridBagConstraints.BOTH;
    cpanelConstraints.anchor = GridBagConstraints.EAST;
    //add inside frame
    this.add(controlPanel, cpanelConstraints);
  }

  public synchronized void createTab(String clientIP, ClientTab clientTab) {
    tabbedPane.addTab(clientIP, clientTab);
  }

  public App getContext() {
    return this.app;
  }

  public JTabbedPane getTabPane() {
    return this.tabbedPane;
  }

}
