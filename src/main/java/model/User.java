package model;

public class User {
    public static final String ADMIN = "ADMIN";
    public static final String PETUGAS = "PETUGAS";

    private final int id;
    private final String username;
    private final String nama;
    private final String role;

    public User(int id, String username, String nama, String role) {
        this.id = id;
        this.username = username;
        this.nama = nama;
        this.role = role;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getNama() { return nama; }
    public String getRole() { return role; }
    public boolean isAdmin() { return ADMIN.equals(role); }
}
