package gui;

import serialization.WindowStorage;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram {

    private static final String WINDOW_PATH = "window.ser";

    public static void main(String[] args) {
      try {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      } catch (Exception e) {
        e.printStackTrace();
      }
      var storage = new WindowStorage(WINDOW_PATH);
      SwingUtilities.invokeLater(() -> new MainApplicationFrame(storage).setVisible(true));
      }
    }
