import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HelloWorldSwing {

    /*
     * Create the GUI and show it. For Thread safety, this method should be invoked from
     * the event-dispatching thread
     */
    private static void createAndShowGUI() {
        // Create and set up window
        JFrame frame = new JFrame("Multithreading chat App");
        //Ask for window decorations provided by the look and feel.
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // Create the menu bar. 
        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque(true);
        menuBar.setBackground(new Color(154, 165, 127));
        menuBar.setPreferredSize(new Dimension(150, 40));

        // label to put into the content pane.
        JLabel label = new JLabel();
        label.setOpaque(true);
        label.setBackground(new Color(248, 213, 131));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        label.setPreferredSize(new Dimension(screenSize.width, screenSize.height));

        // Set the menu bar and add the label to the content pane.
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(label, BorderLayout.CENTER);


        // Dsiplay the window.
        frame.pack();
        frame.setVisible(true);
    }


    // main method
    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread;
        // Creating and showing this application's GUI
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

