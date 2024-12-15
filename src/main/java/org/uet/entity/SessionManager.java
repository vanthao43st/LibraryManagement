package org.uet.entity;

public class SessionManager {

    protected static SessionManager instance;

    protected User currentUser;

    protected SessionManager() {}

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
