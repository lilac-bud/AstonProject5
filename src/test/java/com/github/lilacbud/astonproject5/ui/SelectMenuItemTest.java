package com.github.lilacbud.astonproject5.ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SelectMenuItemTest {
    @Test()
    void createVoidSelectMenuItem() throws ExitException {
        var expectedResult = 42;

        var menuItem = new SelectMenuItem<Void, Integer>(
            'K',
            "Test title",
            (value) -> {
                assertEquals(null, value);
                return expectedResult;
            }
        );

        assertEquals("[K] - Test title", menuItem.getTitle());

        var actualResult = menuItem.executeAction(null);
        assertEquals(expectedResult, actualResult);
    }

    @Test()
    void createValueSelectMenuItem() throws ExitException {
        var expectedResult = 42;

        var menuItem = new SelectMenuItem<Integer, Integer>(
            'K',
            "Test title",
            10,
            (value) -> {
                assertEquals(10, value);
                return expectedResult;
            }
        );

        assertEquals("[K] - Test title", menuItem.getTitle());

        var actualResult = menuItem.executeAction(10);
        assertEquals(expectedResult, actualResult);
    }

    @Test()
    void createThrowSelectMenuItem() throws ExitException {
        var menuItem = new SelectMenuItem<Integer, Integer>(
            'K',
            "Test title",
            10,
            (value) -> {
                throw new ExitException();
            }
        );

        assertThrows(
            ExitException.class,
            () -> menuItem.executeAction(10)
        );
    }
}
