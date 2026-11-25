package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:tiket_konser.db";

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    public static void init() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver SQLite (sqlite-jdbc .jar) belum ditambahkan ke project.", e);
        }

        String[] schema = {
            "CREATE TABLE IF NOT EXISTS users ("
                + "id_user INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL UNIQUE,"
                + "nama TEXT NOT NULL,"
                + "password TEXT NOT NULL,"
                + "role TEXT NOT NULL CHECK (role IN ('ADMIN','PETUGAS')))",

            "CREATE TABLE IF NOT EXISTS konser ("
                + "id_konser INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nama_konser TEXT NOT NULL,"
                + "artis TEXT,"
                + "venue TEXT,"
                + "tgl_konser TEXT NOT NULL)",

            "CREATE TABLE IF NOT EXISTS tiket ("
                + "id_tiket INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "id_konser INTEGER NOT NULL REFERENCES konser(id_konser) ON DELETE CASCADE,"
                + "jenis TEXT NOT NULL,"
                + "harga INTEGER NOT NULL,"
                + "stok INTEGER NOT NULL)",

            "CREATE TABLE IF NOT EXISTS pelanggan ("
                + "id_pelanggan INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nama TEXT NOT NULL,"
                + "no_hp TEXT NOT NULL,"
                + "email TEXT,"
                + "membership INTEGER NOT NULL DEFAULT 0)",

            "CREATE TABLE IF NOT EXISTS pemesanan ("
                + "id_pemesanan INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "id_user INTEGER NOT NULL REFERENCES users(id_user),"
                + "id_pelanggan INTEGER NOT NULL REFERENCES pelanggan(id_pelanggan),"
                + "tgl_pesan TEXT NOT NULL,"
                + "total_bayar INTEGER NOT NULL)",

            "CREATE TABLE IF NOT EXISTS detail_pemesanan ("
                + "id_detail INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "id_pemesanan INTEGER NOT NULL REFERENCES pemesanan(id_pemesanan) ON DELETE CASCADE,"
                + "id_tiket INTEGER NOT NULL REFERENCES tiket(id_tiket),"
                + "jumlah INTEGER NOT NULL,"
                + "harga_satuan INTEGER NOT NULL,"
                + "total_harga INTEGER NOT NULL)"
        };

        try (Connection c = getConnection(); Statement st = c.createStatement()) {
            for (String sql : schema) {
                st.execute(sql);
            }
            seedAdmin(c);
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyiapkan database: " + e.getMessage(), e);
        }
    }

    private static void seedAdmin(Connection c) throws SQLException {
        try (Statement st = c.createStatement();
             java.sql.ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users WHERE role = 'ADMIN'")) {
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO users (username, nama, password, role) VALUES (?,?,?,?)")) {
                    ps.setString(1, "admin");
                    ps.setString(2, "Administrator");
                    ps.setString(3, "admin123");
                    ps.setString(4, "ADMIN");
                    ps.executeUpdate();
                }
            }
        }
    }
}
