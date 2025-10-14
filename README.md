# Simple Hotel Management System

A Java Web Application demonstrating JDBC integration with Microsoft Access (.accdb) using UCanAccess driver. Covers ResultSet traversal, ResultSetMetaData for dynamic analysis, and stored procedures with CallableStatement. Built for Group 3 project requirements.

**Date:** October 14, 2025  
**Group Members:** 
	24/00205	CHEMUTAI BERYL
	23/08010	HERJOK REBECCA ANGETH
	24/01301	KIPLAGAT MEVIS JEMUTAI
	23/01954	MACHARIA STEVE KELLY
	24/01649	MOHAMED SWALEH ABDULGAFUR
	22/03104	MUTUMA GAIL NKATHA- GROUP LEADER
	24/01195	MWANGI SHARON WANJIRU
	24/00576	MUNENE CHRISTINE SYOMBUA
	23/08323	KISEU BRENDA JOY
	22/03290	OBUCHI  NEVYLL KEMUNTO
	23/08334	ABDI ABDULHAKIM HASSAN
	18/02185	ABUGA KENAN JAPHETH
	24/02764	WANJOHI VICTOR MAINA
	23/08313	ETEMESI JACOBSON HAGGAI
	18/06864	KISANG  KIPKOSGEI ALEX
	20/04428	SALEE  IVY LYNN WAMBUI
	25/08754	MOHAMED MOHAMUD MUSE


## Features
- **Database Connection Test:** Verifies UCanAccess link to HotelDB.accdb.
- **ResultSet & Metadata Demo:** Dynamically lists column structures and traverses sample data (first 3 rows from Rooms table).
- **Stored Procedure Demo:** Calls parameterized query to fetch available rooms by type (e.g., Single/Double).

## Tech Stack
- **IDE:** Apache NetBeans 27
- **Server:** Apache Tomcat 11.0.13 (Jakarta EE 10)
- **Database:** MS Access (.accdb) with sample Rooms table
- **JDBC Driver:** UCanAccess 5.1.3 (pure Java, handles quirks like locking)
- **JDK:** OpenJDK 21.0.5 LTS

## Folder Structure
### Pushing Your Hotel Management System to GitHub

Congratulations on completing the project! Below is a step-by-step guide to initialize Git, create a repository on GitHub, and push your code. I'll assume you have Git installed (if not, download from https://git-scm.com/downloads). Also, I'll provide a ready-to-use `README.md` file content—copy it into a new file in your project root.

#### Step 1: Prepare Your Project for Git
1. **Open Command Prompt/Terminal in Project Root:**  
   - Navigate to your project folder: `cd C:\Users\user\OneDrive\Documents\NetBeansProjects\HotelManagement` (adjust path as needed).  

2. **Initialize Git Repository:**  
   - Run: `git init`  
   - This creates a `.git` folder.  

3. **Create .gitignore File:**  
   - Create a new file named `.gitignore` in the project root.  
   - Paste this content (ignores build artifacts, IDE files, and DB for security):  
     ```
     # NetBeans
     nbproject/private/
     nbbuild/
     nbdist/
     nbprocess/
     nbrun/
     build/
     dist/
     
     # Database (sensitive data)
     *.accdb
     *.mdb
     
     # Logs and temps
     *.log
     *.tmp
     .DS_Store
     
     # IDE
     .idea/
     *.iws
     *.iml
     .vscode/
     ```
   - Save it.  

4. **Add All Files:**  
   - Run: `git add .` (stages everything except .gitignore ignores).  
   - Run: `git status` to verify (should show staged files like src/, web/, nbproject/).  

5. **Initial Commit:**  
   - Run: `git commit -m "Initial commit: Simple Hotel Management System with MS Access JDBC demos"`  

#### Step 2: Create GitHub Repository
1. Go to https://github.com/new (log in if needed).  
2. **Repository Name:** `HotelManagementSystem` (or your preference).  
3. **Description:** "Java Web App demoing JDBC with MS Access using UCanAccess (ResultSet, Metadata, Stored Procedures)".  
4. **Public/Private:** Public for sharing.  
5. **Do not initialize with README** (we'll push ours).  
6. Click **Create Repository**.  

#### Step 3: Connect and Push
1. Copy the commands from your new repo page (under "…or push an existing repository").  
2. Run in terminal:  
   ```
   git remote add origin https://github.com/yourusername/HotelManagementSystem.git
   git branch -M main
   git push -u origin main
   ```  
   - Replace `yourusername` with your GitHub username.  
   - Enter GitHub credentials if prompted (use PAT for security: Settings > Developer settings > Personal access tokens > Generate new).  

3. **Verify:** Refresh GitHub repo—files should appear.  

#### Step 4: Add README.md
- Create `README.md` in project root (NetBeans: Right-click project > New > Other > File > Empty File > Name: README.md).  
- Paste the content below.  
- Commit and push: `git add README.md` > `git commit -m "Add README.md"` > `git push`.  

### README.md Content
Copy this exactly into your README.md:

```markdown
# Simple Hotel Management System

A Java Web Application demonstrating JDBC integration with Microsoft Access (.accdb) using UCanAccess driver. Covers ResultSet traversal, ResultSetMetaData for dynamic analysis, and stored procedures with CallableStatement. Built for Group 3 project requirements.

**Date:** October 14, 2025  
**Group Members:** [Insert your 3 group member names here]

## Features
- **Database Connection Test:** Verifies UCanAccess link to HotelDB.accdb.
- **ResultSet & Metadata Demo:** Dynamically lists column structures and traverses sample data (first 3 rows from Rooms table).
- **Stored Procedure Demo:** Calls parameterized query to fetch available rooms by type (e.g., Single/Double).

## Tech Stack
- **IDE:** Apache NetBeans 27
- **Server:** Apache Tomcat 11.0.13 (Jakarta EE 10)
- **Database:** MS Access (.accdb) with sample Rooms table
- **JDBC Driver:** UCanAccess 5.1.3 (pure Java, handles quirks like locking)
- **JDK:** OpenJDK 21.0.5 LTS

HotelManagement/
├── src/                          # Java sources (servlets)
│   └── ConnectionTestServlet.java
│   └── RoomListServlet.java
│   └── AvailableRoomsServlet.java
├── web/                          # Web resources
│   ├── WEB-INF/
│   │   ├── lib/                  # UCanAccess JARs (5 files)
│   │   ├── web.xml               # Servlet mappings
│   │   └── HotelDB.accdb         # Database (add to .gitignore for security)
│   ├── css/
│   │   └── style.css             # Basic styling (tables, responsive)
│   └── index.html                # Home page with links
├── nbproject/                    # NetBeans metadata (.gitignore)
└── build/                        # Compiled (auto-generated, .gitignore)
    └── web/                      # Deployed WAR contents

## Setup Instructions
1. **Install Prerequisites:**  
   - JDK 21: https://adoptium.net/temurin/releases/?version=21  
   - NetBeans 27: https://netbeans.apache.org/download/nb27/  
   - Tomcat 11: https://tomcat.apache.org/download-11.cgi  
   - MS Access (Office 365 or Runtime): https://www.microsoft.com/en-us/microsoft-365/access  
   - UCanAccess JARs (5 files): Download from Maven Central (ucanaccess-5.1.3.jar, jackcess-4.0.5.jar, hsqldb-2.7.1.jar, commons-lang3-3.14.0.jar, commons-logging-1.3.4.jar). Add to Libraries in NetBeans.

2. **Open in NetBeans:**  
   - File > Open Project > Select `HotelManagement`.  
   - Add UCanAccess JARs: Right-click project > Properties > Libraries > Add JAR/Folder.  
   - Place `HotelDB.accdb` in `web/WEB-INF/` (create Rooms table with sample data: see Database Setup below).

3. **Database Setup:**  
   - In Access: Create `Rooms` table (room_id AutoNumber PK, room_number Number, room_type Short Text, status Short Text).  
   - Sample Data:  
     | room_id | room_number | room_type | status     |  
     |---------|-------------|-----------|------------|  
     | 1       | 101         | Double    | Available  |  
     | 2       | 102         | Single    | Occupied   |  
     | 3       | 201         | Double    | Available  |  
     | 4       | 202         | Single    | Available  |  
   - Create Parameter Query: Create > Query Design > SQL View > Paste:  
     ```
     PARAMETERS roomType Text (255);
     SELECT * FROM Rooms WHERE room_type = [roomType] AND status = 'Available';
     ```  
     - Save as `GetAvailableRoomsByType`. Test with "Single" (returns 1 row).

4. **Run:**  
   - Right-click project > Run (deploys to Tomcat).  
   - Access: http://localhost:8080/HotelManagement/  
   - Demos:  
     - /ConnectionTestServlet – Connection success (room count: 4).  
     - /rooms – Metadata table + sample data.  
     - /available?roomType=Single – Available Singles (row 4).  

## Usage
- **Home Page:** Links to all demos.  
- **Quirks Handling:** Close Access before running (avoids locking). Use `;single=true` in URL for exclusive access if concurrent issues.  

## Screenshots
- Home: [Insert screenshot]  
- Rooms List: [Insert screenshot]  
- Available Rooms: [Insert screenshot]  

## Troubleshooting
- **Driver Error:** Ensure all 5 JARs in lib/. Restart NetBeans.  
- **Connection Fail:** Check dbPath in output; move from OneDrive if sync locks.  
- **Procedure Not Found:** Rename query no spaces; uppercase in CALL. Compact/Repair DB.  

## License
MIT License – Feel free to fork/extend.

## Contact
[Kipkosgeialex3@gmail.com] – Questions? Open an issue!
```

This README is comprehensive yet concise—add screenshots via GitHub (drag-drop images to issues or use ![] syntax). After pushing, your repo is live! If issues, describe.
