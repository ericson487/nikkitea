import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Amain {
    /** Starts the application on Swing's event-dispatch thread. */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Matches Swing controls to the operating system's appearance.
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception exception) {
                    System.err.println("Could not apply the system look and feel: " + exception.getMessage());
                }
                // Shows the login window first.
                new LoginFrame().setVisible(true);
            }
        });
    }
}



