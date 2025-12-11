# Networked Task Tracker (Java + Sockets + JavaFX + SQLite)

## Overview

This project is a **network-based client–server Task Tracker** implemented in Java.

- The **server** (TaskServer) listens on a TCP port, manages tasks in memory + SQLite, and handles commands from multiple clients.
- The **clients** connect over the network to the server and send simple text commands (`ADD`, `LIST`, `UPDATE`, `DELETE`).
- The main client for the project is a **JavaFX GUI** (`GuiClientMain`), and we also include a **console client** (`TaskClient`) for testing.

### Main Components

- `Task`  
  Represents a single task (id, title, description, priority, status, assignedTo, createdAt).

- `TaskManager`  
  Manages the collection of tasks. Provides methods:
    - `addTask(...)`
    - `updateTaskStatus(id, newStatus)`
    - `deleteTask(id)`
    - `getAllTasks()`

- `TaskServer`
    - Opens a `ServerSocket` on port `5050`
    - Accepts incoming client connections in a loop
    - For each client, starts a `ClientHandler` thread
    - Understands commands:
        - `ADD|title|description|priority|assignedTo`
        - `LIST`
        - `UPDATE|id|status`
        - `DELETE|id`

- `GuiClientMain` (JavaFX)
    - Connects to `ServerConnection("localhost", 5050)`
    - Displays tasks in a `TableView<String>`
    - Provides fields for Title, Description, Priority, Assigned To, Status
    - Buttons:
        - **Add** → sends `ADD`
        - **Update** → sends `UPDATE` for selected row
        - **Delete** → sends `DELETE` for selected row
        - **Refresh** → sends `LIST`

- `TaskClient` (console client)
    - Connects to the same server using a socket
    - Provides a text menu in the terminal to:
        - Add, list, update, delete tasks

- `ServerConnection`
    - Small wrapper around a `Socket`, `PrintWriter`, and `BufferedReader`
    - Provides `send(String command)` to send a line and read the server response.

---

## Networking / Architecture

- **Server process**:  
  `TaskServer` (or `Main` which calls it) creates a `ServerSocket` and listens for clients on port **5050**.

- **Client connections**:
    - `GuiClientMain` connects with `new ServerConnection("localhost", 5050);`
    - `TaskClient` connects with `new Socket(HOST, PORT);`
    - Multiple clients can run at the same time and see the same tasks from the server.

To run across 2 machines on the same network:

- On the machine running the server, find its IP (example: `192.168.1.20`).
- On the client machine, change the host string:
    - In `GuiClientMain` and `TaskClient`, replace `"localhost"` with `"192.168.1.20"`.
- Make sure firewalls allow traffic on port `5050`.


---

## Setup Instructions

### Requirements

- JDK 24 or 25 installed
- JavaFX SDK 25.x
- `sqlite-jdbc` JAR (for example: `sqlite-jdbc-3.51.1.0.jar`)
- IntelliJ IDEA (or another Java IDE)

### Step 1: Add JavaFX to the project

1. Download JavaFX SDK and unzip it.
2. In IntelliJ:
    - `File → Project Structure → Libraries → + → Java`
    - Select the `lib` folder inside the JavaFX SDK.
3. In your **Run Configuration** for `GuiClientMain`, add VM options (adjust path for your machine):

   ```text
   --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml

## Team Evaluation Jalal Mogalli

| Category                               | Jalal Mogalli | Zayn Siblani | Mohammed Alkhayat | Mohamad Zahr |
|----------------------------------------|:-------------:|:------------:|:------------------:|:------------:|
| **Quality of Work**                    | 3 | 3 | 3 | 3 |
| **Problem Solving Skills**             | 3 | 3 | 3 | 3 |
| **Teamwork**                           | 3 | 3 | 3 | 3 |
| **Initiative**                         | 3 | 3 | 3 | 3 |
| **Communication**                      | 3 | 3 | 3 | 3 |
| **Time Management**                    | 3 | 3 | 3 | 3 |
| **Overall Contribution (Total Score)** | **18** | **18** | **18** | **18** |

## Team Evaluation Zayn Siblani

| Category                               | Zayn Siblani | Jalal Mogalli | Mohammed Alkhayat | Mohamad Zahr |
|----------------------------------------|:------------:|:-------------:|:------------------:|:------------:|
| **Quality of Work**                    |      3       |       3       | 3 | 3 |
| **Problem Solving Skills**             |      3       |       3       | 3 | 3 |
| **Teamwork**                           |      3       |       3       | 3 | 3 |
| **Initiative**                         |      3       |       3       | 3 | 3 |
| **Communication**                      |      3       |       3       | 3 | 3 |
| **Time Management**                    |      3       |       3       | 3 | 3 |
| **Overall Contribution (Total Score)** |    **18**    |    **18**     | **18** | **18** |

## Team Evaluation Mohammed Alkhyat

| Category                               | Mohammed Alkhayat | Jalal Mogalli | Zayn Siblani | Mohamad Zahr |
|----------------------------------------|:------------:|:-------------:|:------------:|:------------:|
| **Quality of Work**                    |      3       |       3       |      3       | 3 |
| **Problem Solving Skills**             |      3       |       3       |      3       | 3 |
| **Teamwork**                           |      3       |       3       |      3       | 3 |
| **Initiative**                         |      3       |       3       |      3       | 3 |
| **Communication**                      |      3       |       3       |      3       | 3 |
| **Time Management**                    |      3       |       3       |      3       | 3 |
| **Overall Contribution (Total Score)** |    **18**    |    **18**     |    **18**    | **18** |

## Team Evaluation Mohamad Zahr

| Category                               | Mohamad Zahr | Jalal Mogalli | Zayn Siblani | Mohammed Alkhayat |
|----------------------------------------|:------------:|:-------------:|:------------:|:------------:|
| **Quality of Work**                    |      3       |       3       |      3       | 3 |
| **Problem Solving Skills**             |      3       |       3       |      3       | 3 |
| **Teamwork**                           |      3       |       3       |      3       | 3 |
| **Initiative**                         |      3       |       3       |      3       | 3 |
| **Communication**                      |      3       |       3       |      3       | 3 |
| **Time Management**                    |      3       |       3       |      3       | 3 |
| **Overall Contribution (Total Score)** |    **18**    |    **18**     |    **18**    | **18** |

