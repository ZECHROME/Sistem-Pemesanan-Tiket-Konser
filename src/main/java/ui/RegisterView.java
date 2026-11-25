package ui;

import dao.UserDAO;
import javafx.animation.PauseTransition;
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
import javafx.util.Duration;

import java.sql.SQLException;

public class RegisterView {
    private final StackPane root = new StackPane();
    private final UserDAO userDAO = new UserDAO();

    public RegisterView() {
        Label title = new Label("User Register");
        title.setStyle(Styles.TITLE);
        Label subtitle = new Label("Buat akun petugas baru.");
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

        Button btnBack = new Button("Back");
        btnBack.setStyle(Styles.BUTTON);
        Button btnRegister = new Button("Register");
        btnRegister.setStyle(Styles.BUTTON_PRIMARY);
        btnRegister.setDefaultButton(true);

        HBox buttons = new HBox(10, btnBack, btnRegister);
        buttons.setAlignment(Pos.CENTER);

        VBox form = new VBox(8, lblUser, tfUser, lblPass, pfPass, message, buttons);
        form.setPadding(new Insets(20));

        VBox card = new VBox(header, form);
        card.setMaxWidth(380);
        card.setMaxHeight(VBox.USE_PREF_SIZE);
        card.setStyle(Styles.CARD);

        root.getChildren().add(card);
        root.setStyle(Styles.PAGE_BG);

        btnBack.setOnAction(e -> SceneManager.showLogin());
        btnRegister.setOnAction(e -> {
            String username = tfUser.getText().trim();
            String password = pfPass.getText();

            if (username.isEmpty() || password.isEmpty()) {
                message.setStyle(Styles.ERROR);
                message.setText("Semua data wajib diisi.");
                return;
            }
            try {
                if (userDAO.usernameExists(username)) {
                    message.setStyle(Styles.ERROR);
                    message.setText("Username sudah terdaftar.");
                    return;
                }
                userDAO.registerPetugas(username, password);
                message.setStyle(Styles.SUCCESS);
                message.setText("Registrasi berhasil. Kembali ke halaman login...");
                btnRegister.setDisable(true);
                PauseTransition pause = new PauseTransition(Duration.seconds(1.2));
                pause.setOnFinished(ev -> SceneManager.showLogin());
                pause.play();
            } catch (SQLException ex) {
                message.setStyle(Styles.ERROR);
                message.setText("Gagal menyimpan data: " + ex.getMessage());
            }
        });
    }

    public Parent getRoot() {
        return root;
    }
}
