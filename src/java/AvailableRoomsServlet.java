import java.io.*;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
public class AvailableRoomsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Available Rooms</title>");
            out.println("<link rel=\"stylesheet\" href=\"css/style.css\">");
            out.println("<style> table { border-collapse: collapse; width: 100%; } th, td { border: 1px solid #ddd; padding: 8px; } th { background: #f2f2f2; } img { max-width: 100px; height: auto; } .price { font-weight: bold; color: green; } .desc { max-width: 200px; word-wrap: break-word; } </style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>Available Rooms by Type</h2>");
            out.println("<p><a href=\"index.html\">Back to Home</a></p>");
            Connection conn = null;
            CallableStatement cs = null;
            ResultSet rs = null;
            try {
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                String dbPath = getServletContext().getRealPath("/WEB-INF/HotelDB.accdb");
                dbPath = dbPath.replace('\\', '/');
                String url = "jdbc:ucanaccess://" + dbPath + ";single=true"; // Prevents locks
                conn = DriverManager.getConnection(url);
              
                String roomType = request.getParameter("roomType");
                if (roomType == null || roomType.trim().isEmpty()) {
                    out.println("<p><strong>Error:</strong> Add ?roomType=Single (or Double) to URL.</p>");
                    return;
                }
                out.println("<p>Searching for '" + roomType + "' rooms...</p>"); // Debug
                // FIXED: Escape syntax for saved SELECT query (no brackets since no space)
                cs = conn.prepareCall("{call SingleRooms(?)}"); // Matches renamed query
                cs.setString(1, roomType);
                rs = cs.executeQuery();
                out.println("<h3>Results for '" + roomType + "' Rooms</h3>");
                out.println("<table>");
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();
              
                // Headers
                out.print("<tr>");
                for (int i = 1; i <= colCount; i++) {
                    out.print("<th>" + meta.getColumnName(i) + "</th>");
                }
                out.println("</tr>");
                // Data rows
                boolean hasData = false;
                int resultCount = 0;
                while (rs.next()) {
                    hasData = true;
                    resultCount++;
                    out.print("<tr>");
                    for (int i = 1; i <= colCount; i++) {
                        String value = rs.getObject(i) != null ? rs.getObject(i).toString() : "";
                        String colName = meta.getColumnName(i).toLowerCase();
                        if (colName.equals("imageurl") && !value.isEmpty()) { // Render image (case-insensitive)
                            String fullPath = "images/" + value;  // Prepend for DB relative paths (e.g., double-101.png → images/double-101.png)
                            String imgSrc = value.startsWith("http") ? value : request.getContextPath() + "/" + fullPath;  // Full: /HotelManagement/images/double-101.png
                            String roomId = rs.getString("room_id");
                            out.print("<td><img src=\"" + imgSrc + "\" alt=\"Room " + roomId + " Image\" style=\"max-width:100px; height:auto; border:1px solid #ddd;\" " +
                                      "onerror=\"this.src='https://via.placeholder.com/100x75/FF6B6B/white?text=Load+Error&text=Room+" + roomId + "';\"></td>");
                        } else if (colName.equals("price")) { // Format price
                            out.print("<td class=\"price\">" + value + "</td>");
                        } else if (colName.equals("description")) { // Wrap desc
                            out.print("<td class=\"desc\">" + value + "</td>");
                        } else {
                            out.print("<td>" + value + "</td>");
                        }
                    }
                    out.println("</tr>");
                }
                if (!hasData) {
                    out.println("<tr><td colspan=\"" + colCount + "\">No available " + roomType + " rooms.</td></tr>");
                }
                out.println("</table>");
                if (hasData) {
                    out.println("<p>Query executed successfully! Found " + resultCount + " available " + roomType + " rooms.</p>"); // Enhanced debug with count
                }
                // Week 13: Serialize proc session to disk (multi-threaded logger sim)
                serializeProcLog(roomType, resultCount);
            } catch (ClassNotFoundException e) {
                out.println("<p><strong>Error:</strong> Driver not found.</p><pre>" + e.getMessage() + "</pre>");
            } catch (SQLException e) {
                out.println("<p><strong>Error:</strong> Procedure call failed.</p><pre>" + e.getMessage() + "</pre>");
            } finally {
                if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
                if (cs != null) try { cs.close(); } catch (SQLException ignore) {}
                if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
            }
            out.println("</body>");
            out.println("</html>");
        }
    }

    // Week 13: Serialize proc session to disk (append for concurrent requests)
    private void serializeProcLog(String roomType, int resultCount) {
        try (FileWriter writer = new FileWriter("available_rooms_log.txt", true)) {  // Append mode
            writer.write("Proc Call [" + new java.util.Date() + "]: " + roomType + " rooms - Found " + resultCount + "\n");
            System.out.println("Proc logged to available_rooms_log.txt");  // Console debug
        } catch (IOException e) {
            System.err.println("Log error: " + e.getMessage());
        }
    }
}