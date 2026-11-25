package util;

import model.User;

/** Menyimpan user yang sedang login ("simpan data user yang login" pada flowchart). */
public class Session {
    private static User currentUser;

    public static void setCurrentUser(User user) { currentUser = user; }
    public static User getCurrentUser() { return currentUser; }
    public static void clear() { currentUser = null; }
}
