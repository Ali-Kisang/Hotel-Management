import java.io.*;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

public class ConnectionTestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Connection Test</title>");
            out.println("<link rel=\"stylesheet\" href=\"css/style.css\">");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>Database Connection Test</h2>");

            Connection conn = null;
            try {
                // Explicit driver load - fixes "no suitable driver" in Tomcat
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                
                String dbPath = getServletContext().getRealPath("/WEB-INF/HotelDB.accdb");
                dbPath = dbPath.replace('\\', '/'); // Normalize Windows paths
                String url = "jdbc:ucanaccess://" + dbPath;
                
                conn = DriverManager.getConnection(url);
                conn.setReadOnly(false);
                
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Rooms");
                rs.next();
                int roomCount = rs.getInt(1);
                
                out.println("<p><strong>Success!</strong> Connected to HotelDB.accdb via UCanAccess.</p>");
                out.println("<p>Number of rooms in database: " + roomCount + "</p>");
                
                rs.close();
                stmt.close();
            } catch (ClassNotFoundException e) {
                out.println("<p><strong>Error:</strong> Driver class not found (check JARs).</p>");
                out.println("<pre>" + e.getMessage() + "</pre>");
            } catch (SQLException e) {
                out.println("<p><strong>Error:</strong> Connection failed.</p>");
                out.println("<pre>" + e.getMessage() + "</pre>");
            } finally {
                if (conn != null) {
                    try { conn.close(); } catch (SQLException ignore) {}
                }
            }
            
            out.println("<p><a href=\"index.html\">Back to Home</a></p>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}