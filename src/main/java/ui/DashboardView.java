package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.User;
import util.Session;

public class DashboardView {
    private final BorderPane root = new BorderPane();

    public DashboardView() {
        User user = Session.getCurrentUser();

        // ---------- Header ----------
        Label appName = new Label("Sistem Pemesanan Tiket Konser");
        appName.setStyle(Styles.TITLE);
        Label roleInfo = new Label("Login sebagai: " + user.getNama() + "  |  Role: " + user.getRole());
        roleInfo.setStyle(Styles.MUTED);
        VBox titleBox = new VBox(4, appName, roleInfo);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnLogout = new Button("Logout");
        btnLogout.setStyle(Styles.BUTTON);
        btnLogout.setOnAction(e -> {
            Session.clear();
            SceneManager.showLogin();
        });

        HBox header = new HBox(10, titleBox, spacer, btnLogout);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(16, 24, 16, 24));
        header.setStyle(Styles.HEADER);

        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        if (user.isAdmin()) {
            tabs.getTabs().add(placeholderTab("Konser", "Data Konser",
                    "Tambah, ubah, dan hapus konser beserta stok tiket Reguler, VIP, dan VVIP."));
        } else {
            tabs.getTabs().add(placeholderTab("Pesan Tiket", "Pesan tiket konser",
                    "Lengkapi data pemesan dan pilih tiket konser."));
        }
        tabs.getTabs().add(placeholderTab("Laporan", "Laporan pemesanan",
                "Pilih tanggal dan ekspor laporan ke PDF."));

        root.setTop(header);
        root.setCenter(tabs);
        root.setStyle(Styles.PAGE_BG);
    }

    private Tab placeholderTab(String tabName, String heading, String description) {
        Label h = new Label(heading);
        h.setStyle(Styles.H2);
        Label d = new Label(description);
        d.setStyle(Styles.MUTED);

        VBox content = new VBox(6, h, d);
        content.setPadding(new Insets(24));
        content.setAlignment(Pos.TOP_LEFT);

        StackPane wrapper = new StackPane(content);
        StackPane.setAlignment(content, Pos.TOP_LEFT);
        wrapper.setStyle("-fx-background-color: #f4f4f4;");

        Tab tab = new Tab(tabName, wrapper);
        tab.setClosable(false);
        return tab;
    }

    public Parent getRoot() {
        return root;
    }
}
