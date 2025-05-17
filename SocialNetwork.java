package socialMediaPlatform;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class SocialNetwork {
    private static final String DATA_FILE = "data/Data_Network.ser";
    private Map<String, UserDirectory> users;

    public Map<String, UserDirectory> getUsers() {
        return users;
    }

    public SocialNetwork() {

        File file = new File(DATA_FILE).getParentFile();
        if (file!=null && !file.exists()) {
            file.mkdirs();
        }

        if (!loadData()) {
            this.users = new HashMap<>();
        }
    }



    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(users);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());

        }
    }



    private boolean loadData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            users = (Map<String, UserDirectory>) in.readObject();
            System.out.println("Data loaded successfully.");
            return true;
        } catch (FileNotFoundException e ) {
            System.out.println("No data file found. Starting with an empty network.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
        return false;
    }



    // Add a user to the network
    public void addUser(String username, String name) {
        if (users.containsKey(username)) {
            System.out.println("Username already exists.");
            return;
        }
        users.put(username, new UserDirectory(username, name));
        saveData();
    }

    // Add a friend relationship
    public void addFriend(String username1, String username2) {
        if (username1.equals(username2)) {
            System.out.println("A user cannot be friends with themselves.");
            return;
        }

        UserDirectory userId1 = users.get(username1);
        UserDirectory userId2 = users.get(username2);

        if (userId1 == null || userId2 == null) {
            System.out.println("One or both users not found.");
            return;
        }

        if (userId1.getFriends().contains(userId2)) {
            System.out.println("Friendship already exists between " + userId1.getName() + " and " + userId2.getName());
            return;
        }

        userId1.addFriend(userId2);
        userId1.incrementFollowerCount();
        userId2.incrementFollowingCount();
        System.out.println("Friendship created between " + userId1.getName() + " and " + userId2.getName());
        saveData();
    }

    // Remove a friend relationship
    public void removeFriend(String username1, String username2) {
        UserDirectory userId1 = users.get(username1);
        UserDirectory userId2 = users.get(username2);

        if (userId1 == null || userId2 == null) {
            System.out.println("One or both users not found.");
            return;
        }

        if (!userId1.getFriends().contains(userId2)) {
            System.out.println(userId1.getName() + " and " + userId2.getName() + " are not friends.");
            return;
        }

        userId1.removeFriend(userId2);
        userId1.decrementFollowerCount();
        userId2.decrementFollowingCount();
        System.out.println("Friendship removed between " + userId1.getName() + " and " + userId2.getName());
        saveData();
    }

    // Get mutual friends between two users
    public Set<UserDirectory> getMutualFriends(String username1, String username2) {
        UserDirectory userId1 = users.get(username1);
        UserDirectory userId2 = users.get(username2);

        if (userId1 == null || userId2 == null) {
            System.out.println("One or both users not found.");
            return Set.of();
        }

        Set<UserDirectory> mutualFriends = new HashSet<>(userId1.getFriends());
        mutualFriends.retainAll(userId2.getFriends());

        if (mutualFriends.isEmpty()) {
            System.out.println("No mutual friends found.");
        }
        return mutualFriends;
    }

    // Suggest friends based on mutual connections
    public Set<UserDirectory> suggestFriends(String username) {
        UserDirectory user = users.get(username);

        if (user == null) {
            System.out.println("User not found.");
            return Set.of();
        }

        Map<UserDirectory, Integer> recommendationScores = new HashMap<>();

        for (UserDirectory friend : user.getFriends()) {
            for (UserDirectory potentialFriend : friend.getFriends()) {
                if (!potentialFriend.equals(user) && !user.getFriends().contains(potentialFriend)) {
                    recommendationScores.put(
                            potentialFriend,
                            recommendationScores.getOrDefault(potentialFriend, 0) + 1
                    );
                }
            }
        }

        return recommendationScores.entrySet().stream()
                .sorted((entry1, entry2) -> {
                    int mutualComparison = entry2.getValue().compareTo(entry1.getValue());
                    if (mutualComparison != 0) {
                        return mutualComparison;
                    }

                    int degree1 = entry1.getKey().getFriends().size();
                    int degree2 = entry2.getKey().getFriends().size();
                    if (degree1 != degree2) {
                        return Integer.compare(degree2, degree1);
                    }

                    return entry1.getKey().getUsername().compareTo(entry2.getKey().getUsername());
                })
                .map(Map.Entry::getKey)
                .limit(10)
                .collect(Collectors.toSet());
    }





    // Add a post to a user's profile
    public void addUserPost(String username, String post) {
        UserDirectory user = users.get(username);

        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        if (post == null || post.trim().isEmpty()) {
            System.out.println("Post content cannot be empty.");
            return;
        }

        user.addPost(post);
        System.out.println("Post added for " + user.getName());
        saveData();
    }
    // Display all users in the network
    public void displayAllUsers() {
        System.out.println("All Users in the Social Network:");
        for (UserDirectory user : users.values()) {
            System.out.println("- " + user.getName() + " (username: " + user.getUsername() + ")");
        }
    }
    // Display network connections (users and their friends)
    public void displayNetwork() {
        System.out.println("Social Network Connections:");
        for (UserDirectory user : users.values()) {
            System.out.print(user.getName() + " -> ");
            for (UserDirectory friend : user.getFriends()) {
                System.out.print(friend.getName() + " ");
            }
            System.out.println();
        }
    }
    // Display all posts by a user
    public void displayUserPosts(String username) {
        UserDirectory user = users.get(username);
        if (user != null) {
            System.out.println("Posts by " + user.getName() + ":");
            for (String post : user.getPosts()) {
                System.out.println("- " + post);
            }
        } else {
            System.out.println("User not found.");
        }
    }

    public void bfsTraversal(String startUserId) {
        UserDirectory startUser = users.get(startUserId);
        if (startUser == null) {
            System.out.println("User not found.");
            return;
        }

        Set<UserDirectory> visited = new HashSet<>();
        Queue<UserDirectory> queue = new LinkedList<>();
        queue.offer(startUser);
        visited.add(startUser);

        System.out.println("BFS Traversal from " + startUser.getName() + ":");
        while (!queue.isEmpty()) {
            UserDirectory current = queue.poll();
            System.out.print(current.getName() + " -> ");
            for (UserDirectory friend : current.getFriends()) {
                if (!visited.contains(friend)) {
                    queue.offer(friend);
                    visited.add(friend);
                }
            }
        }
        System.out.println();
    }


}
class pathfindersMain {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SocialNetwork network = new SocialNetwork();
        boolean running = true;

        while (running) {
            System.out.println("\n---- PATHFINDERS MENU ----");
            System.out.println("1. Add User");
            System.out.println("2. Add Friend");
            System.out.println("3. Remove Friend");
            System.out.println("4. Display Mutual Friends");
            System.out.println("5. Suggest Friends");
            System.out.println("6. Display All Users");
            System.out.println("7. Display User Posts");
            System.out.println("8. Add User Post");
            System.out.println("9. Display Network");
            System.out.println("10. BFS Traversal");
            System.out.println("11. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter username: ");
                    String userId = scanner.nextLine();
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    network.addUser(userId, name);
                }
                case 2 -> {
                    System.out.print("Enter First username: ");
                    String userId1 = scanner.nextLine();
                    System.out.print("Enter Second  username: ");
                    String userId2 = scanner.nextLine();
                    network.addFriend(userId1, userId2);
                }
                case 3 -> {
                    System.out.print("Enter First Username: ");
                    String userId1 = scanner.nextLine();
                    System.out.print("Enter Second Username: ");
                    String userId2 = scanner.nextLine();
                    network.removeFriend(userId1, userId2);
                }
                case 4 -> {
                    System.out.print("Enter First Username: ");
                    String userId1 = scanner.nextLine();
                    System.out.print("Enter Second Username: ");
                    String userId2 = scanner.nextLine();
                    Set<UserDirectory> mutualFriends = network.getMutualFriends(userId1, userId2);
                    mutualFriends.forEach(user -> System.out.println(user.getName()));
                }
                case 5 -> {
                    System.out.print("Enter Username: ");
                    String userId = scanner.nextLine();
                    Set<UserDirectory> suggestions = network.suggestFriends(userId);
                    suggestions.forEach(user -> System.out.println(user.getName()));
                }
                case 6 -> network.displayAllUsers();
                case 7 -> {
                    System.out.print("Enter Username: ");
                    String userId = scanner.nextLine();
                    network.displayUserPosts(userId);
                }
                case 8 -> {
                    System.out.print("Enter Username: ");
                    String userId = scanner.nextLine();
                    System.out.print("Enter Post: ");
                    String post = scanner.nextLine();
                    network.addUserPost(userId, post);
                }
                case 9 -> network.displayNetwork();
                case 10 -> {
                    System.out.print("Enter Starting Username: ");
                    String startUserId = scanner.nextLine();
                    network.bfsTraversal(startUserId);
                }
                case 11 -> running = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        System.out.println("Exiting pathfinders. thanks for visiting us!");
    }
}





