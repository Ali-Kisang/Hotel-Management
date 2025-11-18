import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlBasics {
    public static void main(String[] args) {
        String targetUrl = (args.length > 0) ? args[0] : "https://www.example.com/rooms?type=Single&price=100";  // Custom URL via args
        try {
            // Step 1: Parse URL components
            URL url = new URL(targetUrl);
            System.out.println("=== URL Parsing ===");
            System.out.println("Protocol: " + url.getProtocol()); // https
            System.out.println("Host: " + url.getHost()); // www.example.com
            System.out.println("Port: " + url.getPort()); // -1 (default)
            System.out.println("Path: " + url.getPath()); // /rooms
            System.out.println("Query: " + url.getQuery()); // type=Single&price=100
            System.out.println("Full: " + url.toString());

            // Step 2: Establish HTTP connection
            System.out.println("\n=== HTTP Connection Setup ===");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000); // 5s timeout (bumped for reliability)
            conn.setReadTimeout(5000);
            conn.setRequestProperty("User-Agent", "HotelApp/1.0"); // Custom header

            // Quick peek: Response code & headers
            int code = conn.getResponseCode();
            System.out.println("Response Code: " + code); // 404 or 200
            System.out.println("Content-Type: " + conn.getContentType()); // text/html

            // Read body (success or error stream)
            StringBuilder content = new StringBuilder();
            InputStream stream = (code >= 400) ? conn.getErrorStream() : conn.getInputStream();
            if (stream != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                }
            }
            // Truncate for console (full for log)
            String firstLine = content.toString().isEmpty() ? "Empty body" : content.toString().split("\n")[0];
            System.out.println("First Line: " + firstLine); // <!doctype html> or # Example Domain

            // Week 13: Serialize response session to disk (multi-threaded logger sim)
            serializeNetworkLog(targetUrl, code, firstLine);

            conn.disconnect();  // Cleanup
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage()); // Non-HTTP issues (e.g., timeout)
        }
    }

    // Serialize network session to disk (append for concurrent fetches)
    private static void serializeNetworkLog(String url, int code, String firstLine) {
        try (FileWriter writer = new FileWriter("network_log.txt", true)) {  // Append mode
            writer.write("Fetch [" + new java.util.Date() + "]: URL=" + url + ", Code=" + code + ", First Line='" + firstLine + "'\n");
            System.out.println("Network session logged to network_log.txt");
        } catch (IOException e) {
            System.err.println("Log error: " + e.getMessage());
        }
    }
}