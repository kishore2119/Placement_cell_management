import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import gui.LoginFrame;

/**
 * Main entry point for the Placement Cell Management System.
 * Launches the login screen.
 */
public class Main {
    public static void main(String[] args) {
        // Set system look and feel for native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fall back to default Swing look and feel
        }

        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
