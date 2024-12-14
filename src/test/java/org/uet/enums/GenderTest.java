package org.uet.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenderTest {

    @Test
    void testGenderValues() {
        Gender[] values = Gender.values();
        assertEquals(2, values.length);
        assertEquals(Gender.MALE, values[0]);
        assertEquals(Gender.FEMALE, values[1]);
    }

    @Test
    void testValueOf() {
        assertEquals(Gender.MALE, Gender.valueOf("MALE"));
        assertEquals(Gender.FEMALE, Gender.valueOf("FEMALE"));
    }

    @Test
    void testInvalidValueOf() {
        assertThrows(IllegalArgumentException.class, () -> {
            Gender.valueOf("INVALID");
        });
    }
}
