package ui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage stage;

    public static void init(Stage primaryStage) {
        stage = primaryStage;
    }

    private static void setRoot(Parent root) {
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root, 1000, 650));
        } else {
            stage.getScene().setRoot(root);
        }
    }

    public static void showLogin()     { setRoot(new LoginView().getRoot()); }
    public static void showRegister()  { setRoot(new RegisterView().getRoot()); }
    public static void showDashboard() { setRoot(new DashboardView().getRoot()); }
}
