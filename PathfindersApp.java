
package socialMediaPlatform;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.DropShadow;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class PathfindersApp extends Application {
    private SocialNetwork socialNetwork;
    private static final String PRIMARY_COLOR = "#1E88E5";    // Deep blue
    private static final String SECONDARY_COLOR = "#FFA000";  // Amber
    private static final String ACCENT_COLOR = "#4CAF50";     // Green
    private static final String TEXT_COLOR = "#212121";       // Dark gray
    private static final String BACKGROUND_COLOR = "#FFFFFF"; // White
    private Stage primaryStage;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        socialNetwork = new SocialNetwork();
        primaryStage.setTitle("Pathfinders - Shared Roads, Shared Stories");


        showLoginPage();
    }

    private void showLoginPage() {

        StackPane rootLayout = new StackPane();

        Image backgroundImage = new Image(getClass().getResourceAsStream("background.jpg"));
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        rootLayout.setBackground(new Background(background));

        VBox overlay = new VBox(20);
        overlay.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 20;");
        overlay.setMaxWidth(400);
        overlay.setMaxHeight(500);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPadding(new Insets(40));
        overlay.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.2)));

        ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("logos.jpg")));
        logoView.setFitWidth(150);
        logoView.setFitHeight(150);

        Label title = createTitle("PATHFINDERS");
        Label subtitle = new Label("Shared Roads, Shared Stories");
        subtitle.setStyle("-fx-font-size: 18px; -fx-text-fill: " + TEXT_COLOR + "; -fx-font-style: italic;");

        TextField usernameField = createStyledTextField("Username");
        PasswordField passwordField = createStyledPasswordField("Password");

        Button loginButton = createStyledButton("Begin Your Journey", PRIMARY_COLOR);
        loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText()));

        Button createAccountButton = createStyledButton("Join Our Community", SECONDARY_COLOR);
        createAccountButton.setOnAction(e -> showCreateAccountPage());

        overlay.getChildren().addAll(
                logoView,
                title,
                subtitle,
                usernameField,
                passwordField,
                loginButton,
                createAccountButton
        );

        rootLayout.getChildren().add(overlay);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), overlay);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        Scene loginScene = new Scene(rootLayout, 800, 600);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void showCreateAccountPage() {
        VBox createAccountLayout = new VBox(20);
        createAccountLayout.setAlignment(Pos.CENTER);
        createAccountLayout.setPadding(new Insets(40));
        createAccountLayout.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 20;");

        Image backgroundImage = new Image(getClass().getResourceAsStream("background.jpg"));
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        createAccountLayout.setBackground(new Background(background));

        Label title = createTitle("Join Our Community");

        TextField newUsernameField = createStyledTextField("Choose Your Username");
        TextField newNameField = createStyledTextField("Enter Your Full Name");
        PasswordField newPasswordField = createStyledPasswordField("Create Your Password");

        Button createButton = createStyledButton("Start Your Adventure", SECONDARY_COLOR);
        createButton.setOnAction(e -> handleCreateAccount(newUsernameField.getText(), newNameField.getText(), newPasswordField.getText()));

        Button backButton = createStyledButton("Back to Login", ACCENT_COLOR);
        backButton.setOnAction(e -> showLoginPage());

        createAccountLayout.getChildren().addAll(title, newUsernameField, newNameField, newPasswordField, createButton, backButton);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), createAccountLayout);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        Scene createAccountScene = new Scene(createAccountLayout, 800, 600);
        primaryStage.setScene(createAccountScene);
    }

    private void handleLogin(String username, String password) {
        System.out.println("Attempting login for username: " + username);
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Failed", "Username and password cannot be empty.", Alert.AlertType.ERROR);
            return;
        }

        if (socialNetwork.getUsers().containsKey(username)) {

            System.out.println("Login successful for user: " + username);
            showAlert("Welcome Back!", "Login successful. Enjoy your journey!", Alert.AlertType.INFORMATION);
            showMainApplication(username);
        } else {
            showAlert("Login Failed", "User not found. Please check your username or create an account.", Alert.AlertType.ERROR);
        }
    }

    private void handleCreateAccount(String username, String name, String password) {
        if (username.isEmpty() || name.isEmpty() || password.isEmpty()) {
            showAlert("Oops!", "Username, name, and password are essential for your journey.", Alert.AlertType.ERROR);
            return;
        }
        if (socialNetwork.getUsers().containsKey(username)) {
            showAlert("Already Taken", "This username is already on an adventure. Please choose another.", Alert.AlertType.ERROR);
            return;
        }
        socialNetwork.addUser(username, name);
        showAlert("Welcome Aboard!", "Your account has been created. Your journey begins now!", Alert.AlertType.INFORMATION);
        showLoginPage();
    }

    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " + PRIMARY_COLOR + ";" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 12;" +
                        "-fx-font-size: 14px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0, 0, 1);"
        );
        field.setPrefWidth(300);
        return field;
    }

    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        field.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " + PRIMARY_COLOR + ";" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 12;" +
                        "-fx-font-size: 14px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0, 0, 1);"
        );
        field.setPrefWidth(300);
        return field;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 12 24;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0, 0, 1);"
        );
        button.setPrefWidth(300);

        button.setOnMouseEntered(e ->
                button.setStyle(button.getStyle() +
                        "-fx-background-color: derive(" + color + ", -10%);" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 8, 0, 0, 2);"
                )
        );
        button.setOnMouseExited(e ->
                button.setStyle(button.getStyle() +
                        "-fx-background-color: " + color + ";" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 4, 0, 0, 1);"
                )
        );

        return button;
    }

    private Label createTitle(String text) {
        Label title = new Label(text);
        title.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 36));
        title.setTextFill(Color.web(PRIMARY_COLOR));
        title.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.1)));
        return title;
    }

    private void showMainApplication(String username) {
        BorderPane mainLayout = new BorderPane();

        Image backgroundImage = new Image(getClass().getResourceAsStream("background.jpg"));
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        mainLayout.setBackground(new Background(background));

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9);");

        ImageView logoSmall = new ImageView(new Image(getClass().getResourceAsStream("logos.jpg")));
        logoSmall.setFitHeight(40);
        logoSmall.setFitWidth(40);

        Label headerTitle = new Label("Welcome, " + username + "!");
        headerTitle.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, 24));
        headerTitle.setTextFill(Color.web(PRIMARY_COLOR));

        header.getChildren().addAll(logoSmall, headerTitle);
        mainLayout.setTop(header);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-tab-min-height: 40px;" +
                        "-fx-tab-max-height: 40px;"
        );

        Tab userTab = createStyledTab("Travelers", createUserManagementPane(), "user.jpg");
        Tab friendTab = createStyledTab("Connections", createFriendManagementPane(), "friend.jpg");
        Tab displayTab = createStyledTab("Network", createDisplayPane(), "network.jpg");
        Tab recommendTab = createStyledTab("Discover", createRecommendationPane(), "recommendations.jpg");
        Tab addPostTab = createStyledTab("Share Story", createAddPostPane(), "post.jpg");
        Tab mutualFriendsTab = createMutualFriendsTab();
        Tab userPostsTab = createUserPostsTab();

        tabPane.getTabs().addAll(
                userTab, friendTab, displayTab, recommendTab,
                addPostTab, mutualFriendsTab, userPostsTab
        );

        mainLayout.setCenter(tabPane);

        Scene mainScene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Pathfinders - " + username + "'s Journey");
        primaryStage.show();
    }

    private Pane createUserManagementPane() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 20;");

        Label title = createTitle("Add a New Traveler");

        TextField usernameField = createStyledTextField("Traveler's Username");
        TextField nameField = createStyledTextField("Traveler's Full Name");

        Button addUserButton = createStyledButton("Add to Our Community", PRIMARY_COLOR);
        addUserButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String name = nameField.getText().trim();

            if (username.isEmpty() || name.isEmpty()) {
                showAlert("Incomplete Details", "Both username and name are needed for this journey!", Alert.AlertType.ERROR);
                return;
            }

            if (socialNetwork.getUsers().containsKey(username)) {
                showAlert("Username Exists", "Username already exists.", Alert.AlertType.ERROR);
            } else {
                socialNetwork.addUser(username, name);
                showAlert("Welcome Aboard!", "A new traveler has joined our community!", Alert.AlertType.INFORMATION);
                usernameField.clear();
                nameField.clear();
            }
        });

        layout.getChildren().addAll(title, usernameField, nameField, addUserButton);
        return layout;
    }

    private Pane createFriendManagementPane() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 20;");

        Label title = createTitle("Connect Travelers");

        TextField user1IdField = createStyledTextField("First Traveler's Username");
        TextField user2IdField = createStyledTextField("Second Traveler's Username");

        Button addFriendButton = createStyledButton("Create Connection", SECONDARY_COLOR);
        addFriendButton.setOnAction(e -> {
            String user1Id = user1IdField.getText().trim();
            String user2Id = user2IdField.getText().trim();

            if (user1Id.isEmpty() || user2Id.isEmpty()) {
                showAlert("Missing Information", "Both travelers' usernames are needed to create a connection!", Alert.AlertType.ERROR);
                return;
            }

            if (user1Id.equals(user2Id)) {
                showAlert("Invalid Connection", "A traveler can't connect with themselves!", Alert.AlertType.ERROR);
                return;
            }

            if (!socialNetwork.getUsers().containsKey(user1Id) || !socialNetwork.getUsers().containsKey(user2Id)) {
                showAlert("User Not Found", "One or both users not found.", Alert.AlertType.ERROR);
                return;
            }

            socialNetwork.addFriend(user1Id, user2Id);
            showAlert("New Connection!", "These travelers are now connected on their journey!", Alert.AlertType.INFORMATION);

            user1IdField.clear();
            user2IdField.clear();
        });

        Button removeFriendButton = createStyledButton("End Connection", ACCENT_COLOR);
        removeFriendButton.setOnAction(e -> {
            String user1Id = user1IdField.getText().trim();
            String user2Id = user2IdField.getText().trim();

            if (user1Id.isEmpty() || user2Id.isEmpty()) {
                showAlert("Missing Information", "Both travelers' usernames are needed to end a connection!", Alert.AlertType.ERROR);
                return;
            }

            if (!socialNetwork.getUsers().containsKey(user1Id) || !socialNetwork.getUsers().containsKey(user2Id)) {
                showAlert("User Not Found", "One or both users not found.", Alert.AlertType.ERROR);
                return;
            }

            socialNetwork.removeFriend(user1Id, user2Id);
            showAlert("Connection Status", "Connection removed if it existed.", Alert.AlertType.INFORMATION);

            user1IdField.clear();
            user2IdField.clear();
        });

        layout.getChildren().addAll(title, user1IdField, user2IdField, addFriendButton, removeFriendButton);
        return layout;
    }

    private Pane createDisplayPane() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 20;");

        Label title = createTitle("Explore Our Network");

        Button displayUsersButton = createStyledButton("View All Travelers", PRIMARY_COLOR);
        displayUsersButton.setOnAction(e -> {
            StringBuilder content = new StringBuilder();
            for (UserDirectory user : socialNetwork.getUsers().values()) {
                content.append(user.getUsername())
                        .append(" - ")
                        .append(user.getName())
                        .append("\n");
            }
            showAlert("Our Global Community", content.toString(), Alert.AlertType.INFORMATION);
        });

        Button displayNetworkButton = createStyledButton("View Connections", SECONDARY_COLOR);
        displayNetworkButton.setOnAction(e -> {
            StringBuilder content = new StringBuilder();
            for (UserDirectory user : socialNetwork.getUsers().values()) {
                content.append(user.getName()).append(" -> ");
                for (UserDirectory friend : user.getFriends()) {
                    content.append(friend.getName()).append(", ");
                }
                if (!user.getFriends().isEmpty()) {
                    content.setLength(content.length() - 2);
                }
                content.append("\n");
            }
            showAlert("Global Connections", content.toString(), Alert.AlertType.INFORMATION);
        });

        layout.getChildren().addAll(title, displayUsersButton, displayNetworkButton);
        return layout;
    }



    private Pane createRecommendationPane() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 20;");

        Label title = createTitle("Discover New Connections");

        TextField userIdField = createStyledTextField("Enter Traveler's Username");

        Button recommendButton = createStyledButton("Find New Connections", PRIMARY_COLOR);
        recommendButton.setOnAction(e -> {
            String userId = userIdField.getText().trim();

            if (userId.isEmpty()) {
                showAlert("Missing Information", "Please enter a traveler's username to find new connections!", Alert.AlertType.ERROR);
                return;
            }

            if (!socialNetwork.getUsers().containsKey(userId)) {
                showAlert("User Not Found", "We couldn't find a traveler with that username. Please check and try again.", Alert.AlertType.ERROR);
                return;
            }

            Set<UserDirectory> recommendations = socialNetwork.suggestFriends(userId);
            StringBuilder content = new StringBuilder();

            for (UserDirectory user : recommendations) {
                content.append(user.getName()).append(" (").append(user.getUsername()).append(")\n");
            }

            showAlert("Recommended Connections", content.toString(), Alert.AlertType.INFORMATION);
        });

        layout.getChildren().addAll(title, userIdField, recommendButton);
        return layout;
    }

    private Pane createAddPostPane() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 20;");

        Label title = createTitle("Share Your Journey");

        TextField usernameField = createStyledTextField("Your Username");

        TextArea postArea = new TextArea();
        postArea.setPromptText("Tell us about your adventure...");
        postArea.setStyle("-fx-background-color: white; -fx-border-color: " + PRIMARY_COLOR + "; " +
                "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10; -fx-font-size: 14px;");
        postArea.setPrefWidth(300);
        postArea.setPrefHeight(100);

        Button addPostButton = createStyledButton("Share Your Story", SECONDARY_COLOR);
        addPostButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String post = postArea.getText().trim();

            if (username.isEmpty() || post.isEmpty()) {
                showAlert("Incomplete Story", "Both your username and your story are needed to share!", Alert.AlertType.ERROR);
                return;
            }

            if (!socialNetwork.getUsers().containsKey(username)) {
                showAlert("User Not Found", "We couldn't find a traveler with that username. Please check and try again.", Alert.AlertType.ERROR);
                return;
            }

            socialNetwork.addUserPost(username, post);
            showAlert("Story Shared!", "Your journey has been shared with our community!", Alert.AlertType.INFORMATION);

            usernameField.clear();
            postArea.clear();
        });

        layout.getChildren().addAll(title, usernameField, postArea, addPostButton);
        return layout;
    }

    private Tab createMutualFriendsTab() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 20;");

        Label title = createTitle("Mutual Connections");

        TextField user1Field = createStyledTextField("First Traveler's Username");
        TextField user2Field = createStyledTextField("Second Traveler's Username");

        Button findMutualFriendsButton = createStyledButton("Discover Shared Connections", PRIMARY_COLOR);
        TextArea mutualFriendsArea = new TextArea();
        mutualFriendsArea.setEditable(false);
        mutualFriendsArea.setPrefWidth(300);
        mutualFriendsArea.setPrefHeight(150);
        mutualFriendsArea.setStyle("-fx-background-color: white; -fx-border-color: " + PRIMARY_COLOR + "; " +
                "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10; -fx-font-size: 14px;");

        findMutualFriendsButton.setOnAction(e -> {
            String user1 = user1Field.getText().trim();
            String user2 = user2Field.getText().trim();

            if (user1.isEmpty() || user2.isEmpty()) {
                showAlert("Missing Information", "Both travelers' usernames are needed to find common connections!", Alert.AlertType.ERROR);
                return;
            }

            if (!socialNetwork.getUsers().containsKey(user1) || !socialNetwork.getUsers().containsKey(user2)) {
                showAlert("User Not Found", "One or both users not found.", Alert.AlertType.ERROR);
                return;
            }

            Set<UserDirectory> mutualFriends = socialNetwork.getMutualFriends(user1, user2);
            StringBuilder content = new StringBuilder("Shared Connections:\n");

            for (UserDirectory friend : mutualFriends) {
                content.append(friend.getName()).append(" (").append(friend.getUsername()).append(")\n");
            }

            mutualFriendsArea.setText(content.toString());
        });

        layout.getChildren().addAll(title, user1Field, user2Field, findMutualFriendsButton, mutualFriendsArea);

        Tab tab = new Tab("Common Connections", layout);
        tab.setClosable(false);
        return tab;
    }

    private Tab createUserPostsTab() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 20;");

        Label title = createTitle("Explore Traveler Stories");

        TextField usernameField = createStyledTextField("Enter Traveler's Username");

        Button viewPostsButton = createStyledButton("View Stories", PRIMARY_COLOR);
        TextArea postsArea = new TextArea();
        postsArea.setEditable(false);
        postsArea.setPrefWidth(300);
        postsArea.setPrefHeight(150);
        postsArea.setStyle("-fx-background-color: white; -fx-border-color: " + PRIMARY_COLOR + "; " +
                "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10; -fx-font-size: 14px;");

        viewPostsButton.setOnAction(e -> {
            String username = usernameField.getText().trim();

            if (username.isEmpty()) {
                showAlert("Missing Information", "Please enter a traveler's username to view their stories!", Alert.AlertType.ERROR);
                return;
            }

            UserDirectory user = socialNetwork.getUsers().get(username);

            if (user == null) {
                showAlert("Traveler Not Found", "We couldn't find a traveler with that username. Please check and try again.", Alert.AlertType.ERROR);
                return;
            }

            StringBuilder content = new StringBuilder("Stories shared by " + user.getName() + ":\n");
            for (String post : user.getPosts()) {
                content.append("- ").append(post).append("\n\n");
            }
            postsArea.setText(content.toString());
        });

        layout.getChildren().addAll(title, usernameField, viewPostsButton, postsArea);

        Tab tab = new Tab("Traveler Stories", layout);
        tab.setClosable(false);
        return tab;
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
                "-fx-background-color: white;" +
                        "-fx-padding: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 2);"
        );

        alert.showAndWait();
    }

    private Tab createStyledTab(String text, Pane content, String iconPath) {
        Tab tab = new Tab(text, content);
        tab.setStyle("-fx-background-color: " + PRIMARY_COLOR + "; -fx-text-base-color: white;");

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitHeight(20);
        icon.setFitWidth(20);
        tab.setGraphic(icon);

        return tab;
    }
}

