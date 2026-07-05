package com.github.lilacbud.astonproject5.user.ui;

import com.github.lilacbud.astonproject5.user.UserExitException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class SelectMenuTest {
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
    void createSelectMenu() throws UserExitException {
        var menu = new SelectMenu<Void, Void>(
            "Select Test Title",
            new SelectMenuItem<>('1', "Option 1", (v) -> v),
            new SelectMenuItem<>('2', "Option 2", (v) -> v),
            new SelectMenuItem<>('3', "Option 3", (v) -> v),
            new SelectMenuItem<>('4', "Option 4", (v) -> v)
        );

        var scanner = EOFScanner();

        assertThrows(
            UserExitException.class,
            () -> menu.prompt(scanner)
        );

        var expected = """
            Select Test Title
            [1] - Option 1
            [2] - Option 2
            [3] - Option 3
            [4] - Option 4
            >
            """;

        assertTrue(expected.startsWith(outContent.toString().strip()));
//        assertEquals(expected, outContent.toString().strip());
    }

    @Test()
    void inputOutputValue() throws UserExitException {
        UIMenuAction<Integer, Integer> action = (value) -> {
            assertEquals(value, 20);
            return value * 10;
        };

        var menu = new SelectMenu<Integer, Integer>(
            "Select Test Title",
            new SelectMenuItem<>('1', "Option 1", 10, action),
            new SelectMenuItem<>('2', "Option 2", 20, action),
            new SelectMenuItem<>('3', "Option 3", 30, action),
            new SelectMenuItem<>('4', "Option 4", 40, action)
        );

        var scanner = charScanner("2");

        assertEquals(200, menu.prompt(scanner));
    }
}
