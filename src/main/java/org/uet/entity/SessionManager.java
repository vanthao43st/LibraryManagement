package org.uet.entity;

public class SessionManager {

    private static SessionManager instance;

    private User currentUser;

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Đặt thông tin người dùng hiện tại
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Xóa thông tin người dùng hiện tại (ví dụ: khi đăng xuất)
    public void clearSession() {
        this.currentUser = null;
    }
}
