import java.io.*;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

public class AvailableRoomsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String roomType = request.getParameter("roomType");
        if (roomType == null || roomType.trim().isEmpty()) roomType = "Single";  // Capital S for DB match

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Available Rooms</title>");
            out.println("<link rel=\"stylesheet\" href=\"css/style.css\">");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>Available " + roomType + " Rooms (Stored Procedure Call)</h2>");

            Connection conn = null;
            CallableStatement cstmt = null;
            ResultSet rs = null;
            try {
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                
                String dbPath = getServletContext().getRealPath("/WEB-INF/HotelDB.accdb");
                dbPath = dbPath.replace('\\', '/');
                String url = "jdbc:ucanaccess://" + dbPath;
                
                conn = DriverManager.getConnection(url);
                cstmt = conn.prepareCall("{call [SINGLE ROOMS](?)}");  
                cstmt.setString(1, roomType);
                rs = cstmt.executeQuery();

                out.println("<table>");
                out.println("<tr><th>Room ID</th><th>Number</th><th>Type</th><th>Status</th></tr>");
                boolean hasRows = false;
                while (rs.next()) {
                    hasRows = true;
                    out.println("<tr>");
                    out.println("<td>" + rs.getInt("room_id") + "</td>");
                    out.println("<td>" + rs.getInt("room_number") + "</td>");
                    out.println("<td>" + rs.getString("room_type") + "</td>");
                    out.println("<td>" + rs.getString("status") + "</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
                if (!hasRows) {
                    out.println("<p>No available " + roomType + " rooms found.</p>");
                }

            } catch (ClassNotFoundException e) {
                out.println("<p><strong>Error:</strong> Driver not found.</p><pre>" + e.getMessage() + "</pre>");
            } catch (SQLException e) {
                out.println("<p><strong>Error:</strong> Procedure call failed.</p><pre>" + e.getMessage() + "</pre>");
            } finally {
                if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
                if (cstmt != null) try { cstmt.close(); } catch (SQLException ignore) {}
                if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
            }

            out.println("<p><a href=\"index.html\">Back to Home</a></p>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}