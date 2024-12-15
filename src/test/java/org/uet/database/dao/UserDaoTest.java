package org.uet.database.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.entity.User;
import org.uet.enums.Gender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {

    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
    }

    @Test
    void testGetAllUsersAsync() {
        CompletableFuture<ArrayList<User>> usersFuture = userDao.getAllUsersAsync();
        ArrayList<User> users = usersFuture.join();

        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    void testAddUserAsync() {
        User newUser = new User(
                "99999999",
                "Test User",
                Gender.MALE,
                "Class A",
                "Computer Science",
                "1234567890",
                "test@example.com",
                "testuser",
                "password"
        );

        CompletableFuture<Void> addUserFuture = userDao.addUserAsync(newUser);
        addUserFuture.join();

        CompletableFuture<ArrayList<User>> usersFuture = userDao.getAllUsersAsync();
        ArrayList<User> users = usersFuture.join();

        boolean userExists = users.stream().anyMatch(user -> user.getId().equals("99999999"));
        assertTrue(userExists);
    }

    @Test
    void testUpdateUserAsync() {
        User updatedUser = new User(
                "99999999",
                "Updated User",
                Gender.FEMALE,
                "Class B",
                "Software Engineering",
                "0987654321",
                "updated@example.com",
                "updateduser",
                "newpassword"
        );

        CompletableFuture<Void> updateUserFuture = userDao.updateUserAsync(updatedUser);
        updateUserFuture.join();

        CompletableFuture<ArrayList<User>> usersFuture = userDao.getAllUsersAsync();
        ArrayList<User> users = usersFuture.join();

        User user = users.stream().filter(u -> u.getId().equals("99999999")).findFirst().orElse(null);
        assertNotNull(user);
        assertEquals("Updated User", user.getFullname());
        assertEquals(Gender.FEMALE, user.getGender());
        assertEquals("Class B", user.getClassname());
        assertEquals("Software Engineering", user.getMajor());
        assertEquals("0987654321", user.getPhonenumber());
        assertEquals("updated@example.com", user.getEmail());
        assertEquals("updateduser", user.getUsername());
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    void testDeleteUserAsync() {
        CompletableFuture<Void> deleteUserFuture = userDao.deleteUserAsync("99999999");
        deleteUserFuture.join();

        CompletableFuture<ArrayList<User>> usersFuture = userDao.getAllUsersAsync();
        ArrayList<User> users = usersFuture.join();

        boolean userExists = users.stream().anyMatch(user -> user.getId().equals("99999999"));
        assertFalse(userExists);
    }

    @Test
    void testHasUnreturnedBooksAsync() {
        CompletableFuture<Boolean> hasUnreturnedBooksFuture = userDao.hasUnreturnedBooksAsync("123");
        boolean hasUnreturnedBooks = hasUnreturnedBooksFuture.join();

        assertNotNull(hasUnreturnedBooks);
    }

    @Test
    void testDoesUserIdExistAsync() {
        CompletableFuture<Boolean> doesUserIdExistFuture = userDao.doesUserIdExistAsync("12345678");
        boolean doesUserIdExist = doesUserIdExistFuture.join();

        assertNotNull(doesUserIdExist);
    }

    @Test
    void testDoesUsernameExistAsync() {
        CompletableFuture<Boolean> doesUsernameExistFuture = userDao.doesUsernameExistAsync("testuser");
        boolean doesUsernameExist = doesUsernameExistFuture.join();

        assertNotNull(doesUsernameExist);
    }
}
