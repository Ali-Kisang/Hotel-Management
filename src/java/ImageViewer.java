import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class ImageViewer {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Hotel Room Image Viewer - Client Request Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLayout(new BorderLayout());

            // Simulate "client request": Args (e.g., from servlet) or console input (DB ImageURL)
            String imagePath = (args.length > 0) ? args[0] : getClientRequest();
            String roomId = extractRoomId(imagePath);  // Mock from DB (e.g., "101" for double-101.jpg)

            // Serialize session data to disk (capstone: multi-threaded logger sim)
            serializeSession(roomId, imagePath);

            // Load image (local relative or remote URL)
            try {
                URL imageUrl;
                if (imagePath.startsWith("http")) {
                    imageUrl = new URL(imagePath);  // Remote
                } else {
                    // Local: Adjust path for NetBeans project (Web Pages/images/)
                    String basePath = System.getProperty("user.dir").replace("\\", "/") + "/web/images/";
                    imageUrl = new URL("file://" + basePath + imagePath);
                }
                BufferedImage original = ImageIO.read(imageUrl);
                if (original != null) {
                    // Resize for display
                    Image resized = original.getScaledInstance(250, 150, Image.SCALE_SMOOTH);
                    ImageIcon icon = new ImageIcon(resized);
                    JLabel label = new JLabel(icon);
                    frame.add(new JLabel("Loaded Room " + roomId + ": " + imagePath), BorderLayout.NORTH);
                    frame.add(label, BorderLayout.CENTER);
                } else {
                    frame.add(new JLabel("Failed to load: " + imagePath + " - Check path/network"), BorderLayout.CENTER);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error loading image: " + e.getMessage() + "\nExample: images/double-101.jpg");
                frame.add(new JLabel("Load failed - See console for details"), BorderLayout.CENTER);
            }
            frame.setVisible(true);  // Window appears!
        });
    }

    // Get client request (console sim for DB/servlet param)
    private static String getClientRequest() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter room image path (e.g., images/double-101.jpg or https://via.placeholder.com/...): ");
        return scanner.nextLine().trim();
    }

    // Mock extract room ID from path (integrate with DB query in real app)
    private static String extractRoomId(String path) {
        if (path.contains("double")) return "101";
        if (path.contains("single")) return "202";
        return "Unknown";  // Fallback
    }

    // Serialize session to disk (append for multi-threaded logging)
    private static void serializeSession(String roomId, String imagePath) {
        try (FileWriter writer = new FileWriter("session_log.txt", true)) {  // Append mode
            writer.write("Session [" + new java.util.Date() + "]: Room ID=" + roomId + ", Image Path=" + imagePath + "\n");
            System.out.println("Session serialized to session_log.txt");
        } catch (IOException e) {
            System.err.println("Serialize error: " + e.getMessage());
        }
    }
}