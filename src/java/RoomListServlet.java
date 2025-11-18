import java.io.*;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
public class RoomListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Rooms List</title>");
            out.println("<link rel=\"stylesheet\" href=\"css/style.css\">");
            out.println("<style>");
            out.println("table { border-collapse: collapse; width: 100%; }");
            out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            out.println("th { background-color: #f2f2f2; }");
            out.println("img { max-width: 100px; height: auto; }");
            out.println(".price { font-weight: bold; color: green; }");
            out.println(".desc { max-width: 200px; word-wrap: break-word; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>Rooms Table Demo (ResultSet & Metadata)</h2>");
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                // Explicit driver load
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
               
                String dbPath = getServletContext().getRealPath("/WEB-INF/HotelDB.accdb");
                dbPath = dbPath.replace('\\', '/');
                String url = "jdbc:ucanaccess://" + dbPath;
               
                conn = DriverManager.getConnection(url);
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM Rooms");
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();
                // Section 1: Metadata - Column Names and Types
                out.println("<h3>Table Structure (ResultSetMetaData)</h3>");
                out.println("<table>");
                out.println("<tr><th>Index</th><th>Name</th><th>Type</th></tr>");
                for (int i = 1; i <= columnCount; i++) {
                    out.println("<tr><td>" + i + "</td><td>" + meta.getColumnName(i) + "</td><td>" + meta.getColumnTypeName(i) + "</td></tr>");
                }
                out.println("</table><br>");
                // Section 2: ResultSet Traversal - Sample Data (First 3 Rows)
                out.println("<h3>Sample Data (ResultSet Cursor - First 3 Rows)</h3>");
                out.println("<table>");
                // Headers from metadata
                out.print("<tr>");
                for (int i = 1; i <= columnCount; i++) {
                    out.print("<th>" + meta.getColumnName(i) + "</th>");
                }
                out.println("</tr>");
                int rowCount = 0;
                while (rs.next() && rowCount < 3) {
                    out.print("<tr>");
                    for (int i = 1; i <= columnCount; i++) {
                        String value = rs.getObject(i) != null ? rs.getObject(i).toString() : "";
                        String colName = meta.getColumnName(i).toLowerCase();
                        if (colName.equals("imageurl") && !value.isEmpty()) {  // Render ImageURL as img
                            String fullPath = "images/" + value;  // Prepend for relative paths
                            String imgSrc = value.startsWith("http") ? value : request.getContextPath() + "/" + fullPath;
                            String roomId = rs.getString("room_id");
                            out.print("<td><img src=\"" + imgSrc + "\" alt=\"Room " + roomId + " Image\" style=\"max-width:100px; height:auto; border:1px solid #ddd;\" " +
                                      "onerror=\"this.src='https://via.placeholder.com/100x75/FF6B6B/white?text=Load+Error&text=Room+" + roomId + "';\"></td>");
                        } else if (colName.equals("price")) {  // Format Price
                            out.print("<td class=\"price\">" + value + "</td>");
                        } else if (colName.equals("description")) {  // Wrap Description
                            out.print("<td class=\"desc\">" + value + "</td>");
                        } else {
                            out.print("<td>" + value + "</td>");
                        }
                    }
                    out.println("</tr>");
                    rowCount++;
                }
                out.println("</table>");
                // Week 13: Serialize sample query log
                serializeQueryLog(3);  // First 3 rows
            } catch (ClassNotFoundException e) {
                out.println("<p><strong>Error:</strong> Driver not found.</p><pre>" + e.getMessage() + "</pre>");
            } catch (SQLException e) {
                out.println("<p><strong>Error:</strong> Query failed.</p><pre>" + e.getMessage() + "</pre>");
            } finally {
                if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
                if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
                if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
            }
            out.println("<p><a href=\"index.html\">Back to Home</a></p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // Week 13: Serialize query session to disk (multi-threaded logger sim)
    private void serializeQueryLog(int sampleRows) {
        try (FileWriter writer = new FileWriter("room_sample_log.txt", true)) {  // Append for concurrent requests
            writer.write("Sample Query [" + new java.util.Date() + "]: Loaded first " + sampleRows + " rooms\n");
            System.out.println("Sample query logged to room_sample_log.txt");  // Console for debug
        } catch (IOException e) {
            System.err.println("Log error: " + e.getMessage());
        }
    }
}