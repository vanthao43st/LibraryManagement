package org.uet.test;

import org.uet.database.dao.UserDao;
import org.uet.entity.User;
import org.uet.enums.Gender;

import java.sql.SQLException;
import java.util.ArrayList;

public class TestUser {
    private static final UserDao userDao = new UserDao();

    public static void getAllUser() {
        ArrayList<User> users = userDao.getAllUsers();
        for (User user : users) {
            System.out.println(user.toString());
        }
    }

    public static void getUserById() {
        String userId = "16020587";
        User user = userDao.getUserById(userId);
        System.out.println(user.toString());
    }

    public static void addUser() throws SQLException {
        User user = new User("21020948", "Nguyen Van A", Gender.MALE, "K66K", "Điện tử viễn thông",
                "0394052033", "abc@gmail.com");

        userDao.addUser(user);
    }

    public static void updateUser() throws SQLException {
        User user = new User("21020948", "Cao Van B", Gender.MALE, "K66K", "Điện tử viễn thông",
                "0394052033", "abc@gmail.com");

        userDao.updateUser(user);
    }

    public static void searchUsers() {
//        ArrayList<User> users = userDao.searchUsers(null, "Trần Ngọc Khánh", null);
//        for (User user : users) {
//            System.out.println(user.toString());
//        }
    }

    public static void main(String[] args) throws SQLException {
//        getAllUser();
//        getUserById();
//        addUser();
//        updateUser();
        searchUsers();
    }
}
