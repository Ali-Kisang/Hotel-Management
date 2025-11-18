import java.io.*;
import java.net.*;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
public class AdvancedRoomListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Advanced Rooms List</title>");
            out.println("<link rel=\"stylesheet\" href=\"css/style.css\">");
            out.println("<style>");
            out.println("table { border-collapse: collapse; width: 100%; }");
            out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            out.println("th { background-color: #f2f2f2; }");
            out.println("img { max-width: 100px; height: auto; }");
            out.println(".section { margin: 20px 0; }");
            out.println(".price { font-weight: bold; color: green; }");
            out.println(".desc { max-width: 200px; word-wrap: break-word; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Advanced Hotel Rooms Demo</h1>");
            out.println("<p><a href=\"index.html\">Back to Home</a> | <a href=\"connectionTest\">Test Connection</a> | <a href=\"availableRooms?roomType=Double\">Available Doubles</a></p>");
            // Database Section: Enhanced with full data display and total count
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            int totalRooms = 0;
            try {
                // Explicit driver load
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
              
                String dbPath = getServletContext().getRealPath("/WEB-INF/HotelDB.accdb");
                dbPath = dbPath.replace('\\', '/');
                String url = "jdbc:ucanaccess://" + dbPath;
              
                conn = DriverManager.getConnection(url);
                stmt = conn.createStatement();
                // Get total count first (or filter if param)
                String roomType = request.getParameter("roomType");
                String query = (roomType != null && !roomType.isEmpty()) ? 
                               "SELECT COUNT(*) FROM Rooms WHERE room_type = '" + roomType + "'" : 
                               "SELECT COUNT(*) FROM Rooms";
                ResultSet countRs = stmt.executeQuery(query);
                countRs.next();
                totalRooms = countRs.getInt(1);
                countRs.close();
                // Full query (filter if param)
                query = (roomType != null && !roomType.isEmpty()) ? 
                        "SELECT * FROM Rooms WHERE room_type = '" + roomType + "'" : 
                        "SELECT * FROM Rooms";
                rs = stmt.executeQuery(query);
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();
                // Week 13: Serialize session/query log to disk
                serializeQueryLog(totalRooms, roomType);
                // Section 1: Metadata - Column Names and Types (unchanged for reference)
                out.println("<div class=\"section\">");
                out.println("<h2>Table Structure (ResultSetMetaData)</h2>");
                out.println("<table>");
                out.println("<tr><th>Index</th><th>Name</th><th>Type</th></tr>");
                for (int i = 1; i <= columnCount; i++) {
                    out.println("<tr><td>" + i + "</td><td>" + meta.getColumnName(i) + "</td><td>" + meta.getColumnTypeName(i) + "</td></tr>");
                }
                out.println("</table>");
                out.println("</div>");
                // Section 2: Full ResultSet Traversal - All Rows (advanced: no limit, added total count)
                out.println("<div class=\"section\">");
                String filterText = (roomType != null && !roomType.isEmpty()) ? " (" + roomType + " only)" : "";
                out.println("<h2>All Rooms Data (Total: " + totalRooms + " rooms" + filterText + ")</h2>");
                out.println("<table>");
                // Headers from metadata
                out.print("<tr>");
                for (int i = 1; i <= columnCount; i++) {
                    out.print("<th>" + meta.getColumnName(i) + "</th>");
                }
                out.println("</tr>");
                // Assume Rooms table has columns like RoomID, RoomType, Price, Description, ImageURL (add if not present)
                // For image column (last column assumed as ImageURL), render as <img> tag
                while (rs.next()) {
                    out.print("<tr>");
                    for (int i = 1; i <= columnCount; i++) {
                        String value = rs.getObject(i) != null ? rs.getObject(i).toString() : "";
                        String colName = meta.getColumnName(i).toLowerCase();
                        if (colName.equals("imageurl") && !value.isEmpty()) {  // ImageURL column; skip empties
                            String fullPath = "images/" + value;  // Prepend for DB relative paths (e.g., double-101.png → images/double-101.png)
                            String imgSrc = value.startsWith("http") ? value : request.getContextPath() + "/" + fullPath;  // Full: /HotelManagement/images/double-101.png
                            String roomId = rs.getString("room_id");
                            out.print("<td><img src=\"" + imgSrc + "\" alt=\"Room " + roomId + " Image\" style=\"max-width:100px; height:auto; border:1px solid #ddd;\" " +
                                      "onerror=\"this.src='https://via.placeholder.com/100x75/FF6B6B/white?text=Load+Error&text=Room+" + roomId + "';\"></td>");
                        } else if (colName.equals("price")) {  // Format price
                            out.print("<td class=\"price\">" + value + "</td>");  // $150.00 from DB
                        } else if (colName.equals("description")) {  // Wrap desc
                            out.print("<td class=\"desc\">" + value + "</td>");
                        } else {
                            out.print("<td>" + value + "</td>");  // Other columns as text
                        }
                    }
                    out.println("</tr>");
                }
                out.println("</table>");
                out.println("</div>");
            } catch (ClassNotFoundException e) {
                out.println("<div class=\"section\"><p><strong>Error:</strong> Driver not found.</p><pre>" + e.getMessage() + "</pre></div>");
            } catch (SQLException e) {
                out.println("<div class=\"section\"><p><strong>Error:</strong> Query failed.</p><pre>" + e.getMessage() + "</pre></div>");
            } finally {
                if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
                if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
                if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
            }
            // New Section 3: URL/URLConnection Demo - Fetch Website Data (Week 12 Group 3 Responsibility)
            out.println("<div class=\"section\">");
            out.println("<h2>Live Demo: Fetching Data from Website (java.net.URL & URLConnection)</h2>");
            out.println("<p>Connecting to <strong>https://www.example.com</strong> and displaying raw HTML response.</p>");
            out.println("<p><em>This demonstrates HTTP connection setup, request method, input streams, and response handling.</em></p>");
            try {
                URL fetchUrl = new URL("https://www.example.com");
                HttpURLConnection httpConn = (HttpURLConnection) fetchUrl.openConnection();
                httpConn.setRequestMethod("GET");
                httpConn.setConnectTimeout(5000); // 5s timeout
                httpConn.setReadTimeout(5000);
                int responseCode = httpConn.getResponseCode();
                out.println("<p><strong>Response Code:</strong> " + responseCode + "</p>");
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()))) {
                        StringBuilder content = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            content.append(line).append("\n");
                        }
                        // Truncate for display (full HTML might be long)
                        String truncated = content.toString().length() > 2000 ? content.toString().substring(0, 2000) + "..." : content.toString();
                        out.println("<pre style=\"background: #f5f5f5; padding: 10px; overflow: auto;\">" + truncated.replaceAll("<", "&lt;").replaceAll(">", "&gt;") + "</pre>");
                    }
                } else {
                    out.println("<p><strong>Error:</strong> Failed to fetch (Code: " + responseCode + ").</p>");
                }
            } catch (IOException e) {
                out.println("<p><strong>Network Error:</strong> " + e.getMessage() + "</p>");
                out.println("<p><em>Common issues: Firewall, no internet, invalid URL. In production, add retries and logging.</em></p>");
            }
            out.println("</div>");
            // New Section 4: Multimedia Demo - Image Handling (Web-Adapted for Servlet; See separate GUI code below)
            out.println("<div class=\"section\">");
            out.println("<h2>Live Demo: Image Display (Multimedia in Web Context)</h2>");
            out.println("<p>Displaying a remote image using HTML &lt;img&gt; tag (simulates ImageIcon loading).</p>");
            out.println("<p><em>For full GUI demo (JFrame/ImageIcon), see the separate ImageViewer.java class.</em></p>");
            out.println("<img src=\"https://via.placeholder.com/300x200/4A90E2/white?text=Sample+Hotel+Room\" alt=\"Placeholder Room Image\" style=\"border: 1px solid #ddd;\">");
            out.println("<p><em>This loads from a public URL. In a real app, images could be served from /images/ folder or CDN.</em></p>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // Week 13: Serialize query session to disk (multi-threaded logger sim)
    private void serializeQueryLog(int totalRooms, String roomType) {
        try (FileWriter writer = new FileWriter("room_list_log.txt", true)) {  // Append for concurrent requests
            writer.write("Query [" + new java.util.Date() + "]: Loaded " + totalRooms + " rooms" + (roomType != null ? " for " + roomType : "") + "\n");
            System.out.println("Query logged to room_list_log.txt");  // Console for debug
        } catch (IOException e) {
            System.err.println("Log error: " + e.getMessage());
        }
    }
}