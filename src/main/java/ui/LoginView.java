package ui;

import dao.UserDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.User;
import util.Session;

import java.sql.SQLException;

public class LoginView {
    private final StackPane root = new StackPane();
    private final UserDAO userDAO = new UserDAO();

    public LoginView() {
        Label title = new Label("User Login");
        title.setStyle(Styles.TITLE);
        Label subtitle = new Label("Masukkan username dan password Anda.");
        subtitle.setStyle(Styles.MUTED);
        VBox header = new VBox(4, title, subtitle);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle(Styles.CARD_HEADER);

        Label lblUser = new Label("Username:");
        lblUser.setStyle(Styles.LABEL);
        TextField tfUser = new TextField();
        tfUser.setPromptText("Masukkan username...");

        Label lblPass = new Label("Password:");
        lblPass.setStyle(Styles.LABEL);
        PasswordField pfPass = new PasswordField();
        pfPass.setPromptText("Masukkan password...");

        Label message = new Label();
        message.setWrapText(true);
        message.setStyle(Styles.ERROR);

        Button btnRegister = new Button("Register");
        btnRegister.setStyle(Styles.BUTTON);
        Button btnLogin = new Button("Login");
        btnLogin.setStyle(Styles.BUTTON_PRIMARY);
        btnLogin.setDefaultButton(true);

        HBox buttons = new HBox(10, btnRegister, btnLogin);
        buttons.setAlignment(Pos.CENTER);

        VBox form = new VBox(8, lblUser, tfUser, lblPass, pfPass, message, buttons);
        form.setPadding(new Insets(20));

        VBox card = new VBox(header, form);
        card.setMaxWidth(380);
        card.setMaxHeight(VBox.USE_PREF_SIZE);
        card.setStyle(Styles.CARD);

        root.getChildren().add(card);
        root.setStyle(Styles.PAGE_BG);

        btnRegister.setOnAction(e -> SceneManager.showRegister());
        btnLogin.setOnAction(e -> {
            String username = tfUser.getText().trim();
            String password = pfPass.getText();

            if (username.isEmpty() || password.isEmpty()) {
                message.setText("Username dan password wajib diisi.");
                return;
            }
            try {
                User user = userDAO.login(username, password);
                if (user == null) {
                    message.setText("Username atau password salah.");
                    return;
                }
                Session.setCurrentUser(user);
                SceneManager.showDashboard();
            } catch (SQLException ex) {
                message.setText("Gagal terhubung ke database: " + ex.getMessage());
            }
        });
    }

    public Parent getRoot() {
        return root;
    }
}
