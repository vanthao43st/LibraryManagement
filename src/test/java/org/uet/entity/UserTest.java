package org.uet.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.enums.Gender;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(
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
    }

    @Test
    void testGetId() {
        assertEquals("99999999", user.getId());
    }

    @Test
    void testSetId() {
        user.setId("88888888");
        assertEquals("88888888", user.getId());
    }

    @Test
    void testGetFullname() {
        assertEquals("Test User", user.getFullname());
    }

    @Test
    void testSetFullname() {
        user.setFullname("Updated User");
        assertEquals("Updated User", user.getFullname());
    }

    @Test
    void testGetGender() {
        assertEquals(Gender.MALE, user.getGender());
    }

    @Test
    void testSetGender() {
        user.setGender(Gender.FEMALE);
        assertEquals(Gender.FEMALE, user.getGender());
    }

    @Test
    void testGetClassname() {
        assertEquals("Class A", user.getClassname());
    }

    @Test
    void testSetClassname() {
        user.setClassname("Class B");
        assertEquals("Class B", user.getClassname());
    }

    @Test
    void testGetMajor() {
        assertEquals("Computer Science", user.getMajor());
    }

    @Test
    void testSetMajor() {
        user.setMajor("Software Engineering");
        assertEquals("Software Engineering", user.getMajor());
    }

    @Test
    void testGetPhonenumber() {
        assertEquals("1234567890", user.getPhonenumber());
    }

    @Test
    void testSetPhonenumber() {
        user.setPhonenumber("0987654321");
        assertEquals("0987654321", user.getPhonenumber());
    }

    @Test
    void testGetEmail() {
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testSetEmail() {
        user.setEmail("updated@example.com");
        assertEquals("updated@example.com", user.getEmail());
    }

    @Test
    void testGetUsername() {
        assertEquals("testuser", user.getUsername());
    }

    @Test
    void testSetUsername() {
        user.setUsername("updateduser");
        assertEquals("updateduser", user.getUsername());
    }

    @Test
    void testGetPassword() {
        assertEquals("password", user.getPassword());
    }

    @Test
    void testSetPassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    void testToString() {
        String expected = "User{classname='Class A', id='99999999', fullname='Test User', gender=MALE, major='Computer Science', phonenumber='1234567890', email='test@example.com', username='testuser', password='password'}";
        assertEquals(expected, user.toString());
    }
}
