package org.uet.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uet.enums.Gender;

import static org.junit.jupiter.api.Assertions.*;

class SessionManagerTest {

    private SessionManager sessionManager;
    private User user;

    @BeforeEach
    void setUp() {
        sessionManager = SessionManager.getInstance();
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
    void testGetInstance() {
        assertNotNull(SessionManager.getInstance());
        assertSame(sessionManager, SessionManager.getInstance());
    }

    @Test
    void testSetCurrentUser() {
        sessionManager.setCurrentUser(user);
        assertEquals(user, sessionManager.getCurrentUser());
    }

    @Test
    void testGetCurrentUser() {
        sessionManager.setCurrentUser(user);
        User currentUser = sessionManager.getCurrentUser();
        assertNotNull(currentUser);
        assertEquals(user, currentUser);
    }

    @Test
    void testClearSession() {
        sessionManager.setCurrentUser(user);
        sessionManager.clearSession();
        assertNull(sessionManager.getCurrentUser());
    }
}
