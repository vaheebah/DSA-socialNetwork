package socialMediaPlatform;

import java.io.*;
import java.util.*;

class UserDirectory implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String username;
    private final String name;
    private final Set<UserDirectory> friends;
    private final Queue<String> posts;
    private int followerCount;
    private int followingCount;

    public UserDirectory(String username, String name) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be null or blank.");
        }

        this.username = username;
        this.name = name;
        this.friends = new HashSet<>();
        this.posts = new LinkedList<>();
    }

        public String getUsername() {
            return username;
        }


        public String getName() {
            return name;
        }


        public Set<UserDirectory> getFriends() {
            return friends;
        }


        public Queue<String> getPosts() {
            return posts;
        }

        public int getFollowerCount() {
            return followerCount;
        }

        public int getFollowingCount() {
            return followingCount;
        }

        public void incrementFollowerCount() {
            followerCount++;
        }


        public void incrementFollowingCount() {
            followingCount++;
        }


        public void decrementFollowerCount() {
            followerCount = Math.max(0, followerCount - 1);
        }


        public void decrementFollowingCount() {
            followingCount = Math.max(0, followingCount - 1);
        }
    public void addFriend(UserDirectory user) {
        this.friends.add(user);
        user.getFriends().add(this);
    }


    public void removeFriend(UserDirectory user) {
        this.friends.remove(user);
        user.getFriends().remove(this);
    }


    public void addPost(String post) {
        if (post == null || post.trim().isEmpty()) {
            System.out.println("Post content cannot be empty.");
            return;
        }
        if (posts.size() >= 5) {
            posts.poll();
        }
        posts.offer(post);
    }


    @Override
        public String toString() {
            return "UserDirectory" +
                    "username='" + username + '\'' +
                    ", name='" + name + '\'' +
                    ", friends=" + friends.size() +
                    ", posts=" + posts.size() +
                    ", followerCount=" + followerCount +
                    ", followingCount=" + followingCount;
        }
    }
