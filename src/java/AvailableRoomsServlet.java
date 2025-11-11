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
            out.println("<style> table { border-collapse: collapse; width: 100%; } th, td { border: 1px solid #ddd; padding: 8px; } th { background: #f2f2f2; } img { max-width: 100px; } </style>");
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
                String url = "jdbc:ucanaccess://" + dbPath + ";single=true";  // Prevents locks
                conn = DriverManager.getConnection(url);
                
                String roomType = request.getParameter("roomType");
                if (roomType == null || roomType.trim().isEmpty()) {
                    out.println("<p><strong>Error:</strong> Add ?roomType=Single (or Double) to URL.</p>");
                    return;
                }
                out.println("<p>Searching for '" + roomType + "' rooms...</p>");  // Debug

                // FIXED: Escape syntax for saved SELECT query (no brackets since no space)
                cs = conn.prepareCall("{call SingleRooms(?)}");  // Matches renamed query
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
                while (rs.next()) {
                    hasData = true;
                    out.print("<tr>");
                    for (int i = 1; i <= colCount; i++) {
                        String value = rs.getObject(i) != null ? rs.getObject(i).toString() : "";
                        out.print("<td>" + value + "</td>");
                    }
                    out.println("</tr>");
                }
                if (!hasData) {
                    out.println("<tr><td colspan=\"" + colCount + "\">No available " + roomType + " rooms.</td></tr>");
                }
                out.println("</table>");
                if (hasData) {
                    out.println("<p>Query executed successfully!</p>");  // Success debug
                }

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
}