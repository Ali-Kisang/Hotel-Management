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
                        out.print("<td>" + rs.getObject(i) + "</td>");
                    }
                    out.println("</tr>");
                    rowCount++;
                }
                out.println("</table>");

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
}