package com.github.lilacbud.astonproject5.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PromptHelpersTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalSystemOut = System.out;
    private final InputStream originalSystemIn = System.in;

    static Scanner EOFScanner() {
        return new Scanner(new ByteArrayInputStream(new byte[]{}));
    }

    static Scanner charScanner(String str) {
        return new Scanner(new ByteArrayInputStream(str.getBytes()));
    }

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalSystemOut);
        System.setIn(originalSystemIn);
    }

    @Test()
    void promptUserInput() throws ExitException {
        var userInput = "Test input string";

        var scanner = charScanner(userInput);

        var actualResult = PromptHelpers.promptUserInput(
            scanner,
            "Test user prompt:",
            Function.identity(),
            null
        );

        assertEquals(userInput, actualResult);
        assertEquals("Test user prompt:", outContent.toString());
    }

    @Test()
    void promptUserInputParse() throws ExitException {
        var userInput = "42";

        var scanner = charScanner(userInput);

        var actualResult = PromptHelpers.promptUserInput(
            scanner,
            "Test user prompt:",
            (val) -> Integer.parseInt(val),
            null
        );

        assertEquals(42, actualResult);
    }

    @Test()
    void promptUserInputValidate() throws ExitException {
        var userInput = "42";

        var scanner = charScanner(userInput);

        var actualResult = PromptHelpers.promptUserInput(
            scanner,
            "Test user prompt:\n",
            (val) -> Integer.parseInt(val),
            (val) -> val < 5
        );

        assertEquals(null, actualResult);

        var expected = """
            Test user prompt:
            Ошибка ввода, попробуйте еще раз.
            Test user prompt:
            """.strip();

        assertEquals(expected, outContent.toString().strip());
    }

    @Test()
    void promptUserSelectVoid() throws ExitException {
        var scanner = charScanner("2");

        var actualResult = PromptHelpers.promptUserSelect(
            scanner,
            "Test user prompt:",
            List.of(
                new SelectMenuItem<>('1', "Option 1", (val) -> null),
                new SelectMenuItem<>('2', "Option 2", (val) -> {
                    assertEquals(null, val);
                    return null;
                }),
                new SelectMenuItem<>('3', "Option 3", (val) -> null)
            )
        );

        assertEquals(null, actualResult.executeAction(null));
        assertEquals(null, actualResult.getValue());
        assertEquals("[2] - Option 2", actualResult.getTitle());

        var expected = """
            Test user prompt:
            [1] - Option 1
            [2] - Option 2
            [3] - Option 3
            >
            """.strip();

        assertEquals(expected, outContent.toString().strip());
    }

    @Test()
    void promptUserSelectValue() throws ExitException {
        var scanner = charScanner("2");

        var actualResult = PromptHelpers.promptUserSelect(
            scanner,
            "Test user prompt:",
            List.of(
                new SelectMenuItem<>('1', "Option 1", 1, (val) -> null),
                new SelectMenuItem<>('2', "Option 2", 2, (val) -> {
                    assertEquals(2, val);
                    return val * 2;
                }),
                new SelectMenuItem<>('3', "Option 3", 3, (val) -> null)
            )
        );

        assertEquals(4, actualResult.executeAction(2));
        assertEquals(2, actualResult.getValue());
        assertEquals("[2] - Option 2", actualResult.getTitle());
    }
}
