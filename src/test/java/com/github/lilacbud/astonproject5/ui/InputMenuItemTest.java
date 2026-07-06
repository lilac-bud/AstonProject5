package com.github.lilacbud.astonproject5.ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InputMenuItemTest {
    @Test()
    void createInputMenuItem() throws ExitException {
        var userInput = "Test input string";
        var expectedResult = "Test result";

        var menuItem = new InputMenuItem<String>(
            (value) -> {
                assertEquals(userInput, value);
                return expectedResult;
            }
        );

        var actualResult = menuItem.executeAction(userInput);
        assertEquals(expectedResult, actualResult);
    }

    @Test()
    void createRawInputMenuItem() throws ExitException {
        var userInput = "   Test input string   ";

        var menuItem = new InputMenuItem<String>(
            (value) -> {
                assertEquals(userInput, value);
                return value.strip();
            }
        );

        var actualResult = menuItem.executeAction(userInput);
        assertEquals(userInput.strip(), actualResult);
    }

    @Test()
    void createThrowInputMenuItem() throws ExitException {
        String userInput = null;

        var menuItem = new InputMenuItem<Void>(
            (value) -> {
                throw new ExitException();
            }
        );

        assertThrows(
            ExitException.class,
            () -> menuItem.executeAction(userInput)
        );
    }
}
