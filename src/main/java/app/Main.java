package app;

import db.Database;
import javafx.application.Application;
import javafx.stage.Stage;
import ui.SceneManager;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Database.init();
        SceneManager.init(stage);
        stage.setTitle("Pemesanan Tiket Konser");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        SceneManager.showLogin();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
