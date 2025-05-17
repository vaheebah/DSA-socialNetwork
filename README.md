
# **SOCIAL NETWORK – Pathfinders**

## **1. Introduction**

**Pathfinders** is an innovative social networking application designed specifically for travellers and adventure enthusiasts. The project aims to create a digital platform that mirrors the experience of shared journeys and connections made during travel.

### **Key Objectives:**

* Facilitate connections between like-minded travellers
* Provide a platform for sharing travel experiences and stories
* Encourage the discovery of new connections based on mutual interests and shared networks

The project is implemented in **Java**, utilizing **JavaFX** for the graphical user interface, and incorporates various **data structures and algorithms** to manage the social network efficiently. It offers a range of features from basic user management to sophisticated friend recommendations and network analysis.

---

## **2. Features and Functionalities**

The Pathfinders social network offers a comprehensive set of features designed to create an interactive experience for users:

### **a) User Management:**

* User registration with unique username and full name
* Secure login system
* User profiles storing personal information, friend connections, and posts

### **b) Connection Management:**

* Add friends: Users can add other users
* Remove friends: Option to disconnect from other users
* View all users: Browse through the entire network of travellers
* Display network connections: Visualize the interconnections between users

### **c) Social Interaction:**

* Share posts: Users can create and share their travel stories, limited to the 5 most recent posts
* View posts: Read stories shared by other travellers
* Mutual friends: Discover common connections between two users

### **d) Network Analysis and Recommendations:**

* Friend suggestions: Algorithm to recommend new connections based on friends of friends and network structure
* Breadth-First Search (BFS) traversal: Explore the network structure and connections
* Follower and Following counts: Track user popularity and degree of connection (helps in recommendation scores)

### **e) User Interface:**

Graphical User Interface built with **JavaFX**

Themed tabs for organized access to different functionalities:

* **Travelers:** For adding new users to the network
* **Connections:** For managing friend relationships
* **Network:** To view all users and network connections
* **Discover:** For finding new connections and friend suggestions
* **Share Story:** To add new posts
* **Common Connections:** To find mutual friends between users
* **Traveler Stories:** To view posts from specific users

Responsive alert dialogs for user feedback and notifications
Background images and icons reinforcing the travel theme

### **f) Data Persistence:**

* Serialization of user data and network structure for persistent storage
* Automatic loading and saving of data to maintain user information across sessions

---

## **3. Data Structures (Linear / Non-linear) Used**

### **a) Linear Data Structures:**

* **LinkedList:**
  Employed in the BFS traversal algorithm (`Queue<UserDirectory>`) and for storing user posts (`Queue<String> posts`).

  * In the `UserDirectory` class:
    `private final Queue<String> posts;`
  * In the `bfsTraversal` method:
    `Queue<UserDirectory> queue = new LinkedList<>();`

* **Queue:**
  Utilized in the BFS traversal for managing the order of node visits, ensuring a level-order traversal of the network.

### **b) Non-linear Data Structures:**

* **HashMap:**
  The primary structure for storing user data in the `SocialNetwork` class:
  `private Map<String, UserDirectory> users;`
  This provides O(1) average-case time complexity for user lookup operations.

* **HashSet:**
  Used for storing friends lists in the `UserDirectory` class:
  `private final Set<UserDirectory> friends;`
  Also used in BFS traversal to keep track of visited nodes:
  `Set<UserDirectory> visited = new HashSet<>();`

* **Graph:**
  The social network itself is conceptually represented as an undirected graph, with users as nodes and friendships as edges.
  This is implemented using the `UserDirectory` class and its relationships.

---

## **4. Tools and Technologies Used**

### **a) Programming Language:**

* **Java:**
  Used for implementing the core logic of the application

### **b) GUI Framework:**

* **JavaFX:**
  Provides a rich set of UI controls and layout managers
  Used for creating the graphical user interface of the application

#### **Key Components Used:**

* `Stage` and `Scene` for the main application window
* `VBox`, `HBox`, and `BorderPane` for layout management
* `TextField`, `PasswordField`, and `TextArea` for user input
* `Button` for user actions
* `TabPane` and `Tab` for organizing different functionalities
* `Alert` for displaying messages to the user

### **c) Data Storage:**

* **Java Serialization:**
  Used for saving and loading the application state
  Implemented in the `SocialNetwork` class:

```java
private void saveData() {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
        out.writeObject(users);
    }
}

private boolean loadData() {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
        users = (Map<String, UserDirectory>) in.readObject();
    }
}
```

### **d) File Handling:**

* **Java's built-in file I/O classes** (`FileInputStream`, `FileOutputStream`)
  Used for reading from and writing to the data file

### **e) Collections Framework:**

* Extensive use of Java's **Collections Framework**, including `Map`, `Set`, `Queue`, and their implementations

### **f) Algorithms:**

* Implementation of graph algorithms like **Breadth-First Search (BFS)** for network traversal
* Custom algorithm for **friend suggestions** based on mutual connections

### **g) IDE:**

* **IntelliJ IDEA:**
  Offers robust support for Java and JavaFX development

