package com.playwrightAutomation.BaseUtils;

public class Assertions {
    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertEquals(Object actual, Object expected, String message) {
        if (!actual.equals(expected)) {
            throw new AssertionError(message + " Expected: " + expected + ", but got: " + actual);
        }
    }

    public static void assertEquals(String actual, String expected, String message) {
        if (!actual.equals(expected)) {
            throw new AssertionError(message + " Expected: " + expected + ", but got: " + actual);
        }
    }

    public static void assertNotNull(Object object, String message) {
        if (object == null) {
            throw new AssertionError(message);
        }
    }

    
}
