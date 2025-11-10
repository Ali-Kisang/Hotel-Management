import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class ImageViewer {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Hotel Room Image Viewer Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLayout(new BorderLayout());

            // Load image from URL (demo URLConnection indirectly via ImageIO)
            try {
                URL imageUrl = new URL("https://via.placeholder.com/300x200/4A90E2/white?text=Sample+Hotel+Room");
                BufferedImage original = ImageIO.read(imageUrl);
                if (original != null) {
                    // Resize for display (basic processing)
                    Image resized = original.getScaledInstance(250, 150, Image.SCALE_SMOOTH);
                    ImageIcon icon = new ImageIcon(resized);

                    JLabel label = new JLabel(icon);
                    frame.add(label, BorderLayout.CENTER);

                    // Error handling: Fallback if load fails (this won't trigger if image loads)
                } else {
                    frame.add(new JLabel("Failed to load image - Check network/firewall"), BorderLayout.CENTER);
                }
            } catch (IOException e) {
                // Show error dialog if URL fetch or read fails
                JOptionPane.showMessageDialog(frame, "Error loading image: " + e.getMessage());
                // Add a fallback label
                frame.add(new JLabel("Image load failed - See console for details"), BorderLayout.CENTER);
            }

            frame.setVisible(true);  // This makes the window appear!
        });
    }
}