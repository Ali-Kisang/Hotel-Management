import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlBasics {
    public static void main(String[] args) {
        try {
            // Step 1: Parse URL components
            URL url = new URL("https://www.example.com/rooms?type=Single&price=100");
            System.out.println("=== URL Parsing ===");
            System.out.println("Protocol: " + url.getProtocol());  // https
            System.out.println("Host: " + url.getHost());           // www.example.com
            System.out.println("Port: " + url.getPort());           // -1 (default)
            System.out.println("Path: " + url.getPath());           // /rooms
            System.out.println("Query: " + url.getQuery());         // type=Single&price=100
            System.out.println("Full: " + url.toString());

            // Step 2: Establish HTTP connection (no data fetch yet)
            System.out.println("\n=== HTTP Connection Setup ===");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);  // 3s timeout
            conn.setRequestProperty("User-Agent", "HotelApp/1.0");  // Custom header

            // Quick peek: Response code & headers
            int code = conn.getResponseCode();
            System.out.println("Response Code: " + code);  // 200 OK
            System.out.println("Content-Type: " + conn.getContentType());  // text/html

            // Optional: Read first line of stream (teaser for full fetch)
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                System.out.println("First Line: " + reader.readLine());  // <html>...
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());  // e.g., NoRouteToHost
        }
    }
}