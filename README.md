# Hotel Management System: Advanced Web Demo

![Hotel Room Preview](https://via.placeholder.com/800x400/4A90E2/white?text=Hotel+Management+System)  
*(Placeholder: Replace with a screenshot of your servlet table for GitHub flair.)*

## Overview
This is a comprehensive Java-based web application demonstrating key concepts from Weeks 10–13 of the course: Java I/O, JDBC database operations with UCanAccess for Microsoft Access, Jakarta Servlets for dynamic web content, java.net for networking (URL/URLConnection), and multimedia handling (ImageIO/ImageIcon in Swing). Built as a mini-capstone for hotel room management, it integrates:

- **Database**: Query rooms from `HotelDB.accdb` (schema: room_id, room_number, room_type, status, Price, Description, ImageURL).
- **Web**: Dynamic HTML tables with metadata, full traversal, stored proc calls, and image rendering.
- **Networking**: Parse/fetch external content (e.g., example.com HTML snippet).
- **Multimedia**: Web <img> tags + standalone Swing GUI for client-requested images.
- **Week 13 Integration**: File logging for queries/sessions, multi-thread sim, cross-group demos (I/O from networking, images from DB).

Deployable on Apache Tomcat; tested with Java 17 & NetBeans 21.

## Features
- **ConnectionTestServlet**: Validates DB connectivity + room count.
- **RoomListServlet**: Metadata + first 3 rows (sample traversal).
- **AdvancedRoomListServlet**: Full table display (total count, images, optional type filter), networking fetch, multimedia placeholder.
- **AvailableRoomsServlet**: Stored proc (`SingleRooms`) for type-specific queries (e.g., ?roomType=Double), with images/pricing.
- **UrlBasics**: Console networking demo (URL parse, GET, error handling, logging).
- **ImageViewer**: Swing GUI loads images from paths/URLs (client request via args/input), serializes sessions to disk.
- **Logging**: All servlets/utils append to files (e.g., `room_list_log.txt`, `network_log.txt`) for persistence/multi-thread sim.
- **Styling**: Responsive CSS (style.css) for tables/images (hover, mobile).

| Feature | Servlet/Util | Description |
|---------|--------------|-------------|
| DB Metadata | RoomListServlet | Column names/types table. |
| Full Traversal | AdvancedRoomListServlet | All rooms with images ($/desc formatted). |
| Stored Proc | AvailableRoomsServlet | Available rooms by type (images/pricing). |
| Networking | UrlBasics | Parse/fetch/log (404 handling). |
| GUI Image Load | ImageViewer | Dynamic load + session serialize. |

## Prerequisites
- **Java**: JDK 8+ (tested 17).
- **IDE**: NetBeans 21 (or Eclipse/IntelliJ).
- **Server**: Apache Tomcat 9+ (embedded in NetBeans or standalone).
- **DB Driver**: UCanAccess JDBC (`ucanaccess-5.0.1.jar` + deps: hsqldb, jackcess—add to WEB-INF/lib).
- **Database**: `HotelDB.accdb` (place in WEB-INF; schema/data as per screenshots).
- **Images**: JPG/PNG files in `Web Pages/images/` (e.g., double-101.png).

No Maven/Gradle—manual compile/WAR.

## Setup
1. **Clone/Download**: GitHub repo or unzip project folder.
2. **Add JARs**: Download UCanAccess [here](https://ucanaccess.sourceforge.net/site.html) > Extract > Copy JARs to `WEB-INF/lib/`.
3. **DB**: Create/edit `HotelDB.accdb` in Access (or copy sample). Sample data:
   | room_id | room_number | room_type | status | Price | Description | ImageURL |
   |---------|-------------|-----------|--------|-------|-------------|----------|
   | 1 | 101 | Double | Available | $150.00 | Cozy double with balcony... | double-101.png |
   | ... | ... | ... | ... | ... | ... | ... |
4. **Images**: Add files to `Web Pages/images/` (e.g., download stock hotel rooms).
5. **NetBeans**: Open project > Right-click > Clean and Build.

## Deployment
1. **NetBeans**: Right-click project > Run (deploys to localhost:8080/HotelManagement).
2. **Tomcat Standalone**: Export WAR (NetBeans: Right-click > Build > webapps/HotelManagement.war) > Copy to Tomcat webapps/ > Start server.
3. **Access**: http://localhost:8080/HotelManagement/ (adjust port/context).

## Usage
- **Base**: `/` → index.html (links to servlets).
- **Endpoints**:
  - `/connectionTest` → DB connect + count (e.g., "4 rooms").
  - `/roomList` → Metadata + first 3 rows (images/pricing).
  - `/advancedRoomList` → Full table (filter ?roomType=Single) + networking/multimedia.
  - `/availableRooms?roomType=Double` → Proc results (Available Doubles with images).
- **Console Utils**:
  - `UrlBasics` → Run File: Parses/fetches/logs (args for custom URL).
  - `ImageViewer` → Run File: GUI prompt/args for path (e.g., "double-101.png") → Loads + logs session.
- **Logs**: Check project root (e.g., `network_log.txt`, `room_list_log.txt`) for serialized data.

Example: Browse `/advancedRoomList`—see styled table with thumbnails, then run ImageViewer "double-101.png" for GUI.

## Project Structure
```
HotelManagement/
├── Source Packages/
│   └── default package/
│       ├── AdvancedRoomListServlet.java
│       ├── AvailableRoomsServlet.java
│       ├── ConnectionTestServlet.java
│       ├── RoomListServlet.java
│       ├── ImageViewer.java
│       └── UrlBasics.java
├── Web Pages/
│   ├── css/
│   │   └── style.css
│   ├── images/  (add JPG/PNG here)
│   ├── index.html
│   └── WEB-INF/
│       ├── web.xml  (servlet mappings)
│       ├── lib/  (UCanAccess JARs)
│       └── HotelDB.accdb
└── README.md
```

## Week 13 Integration & Demos
- **Cross-Demo**: Networking fetch (UrlBasics) logs to file (I/O) → Servlet displays snippet → GUI loads DB image from results.
- **Mini-Capstone**: 
  - File logger: All queries append to logs (multi-thread via concurrent runs).
  - Serialize session: ImageViewer writes room/path to disk.
  - GUI load: Client "request" (args/input) pulls DB path → ImageIO resizes/displays.
- **Hands-On**: Modify servlet to log fetches; class feedback on error handling.

## Known Issues & Improvements
- **Images 404**: Ensure exact filenames/ext in DB/images/ (case-sensitive).
- **Proc Errors**: Update SingleRooms in Access for status filter.
- **Production**: Add connection pooling (HikariCP), HTTPS, auth (HttpSession).
- **Future**: REST API (JAX-RS), multi-thread bookings, real hotel API (e.g., Booking.com).

## Submission
- **Group**: [Group 3].
- **Repo**: [https://github.com/Ali-Kisang/Hotel-Management] or zipped folder.
- **Slides**: 8-slide deck (recap, architecture, demos, integration, challenges, exercise, future).
- **License**: MIT (free for edu).

## License
MIT License—use/modify freely for educational purposes.

---

*Built with ❤️ for [Course Name], November 2025. Questions? @AliKisang.*  

*(Copy-paste this to README.md—add your GitHub link/screenshots. Done! 🚀)*
