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

public class InputMenuTest {
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
    void createInputMenu() throws UserExitException {
        var menu = new InputMenu<Void>(
            "Create Test Title",
            new InputMenuItem<>((str) -> null)
        );

        var scanner = EOFScanner();
        assertThrows(
            UserExitException.class,
            () -> menu.prompt(scanner)
        );

        var expected = """
            Create Test Title
            > 
            """;

        assertTrue(expected.startsWith(outContent.toString().strip()));
//        assertEquals(expected, outContent.toString().strip());
    }

    @Test()
    void inputValue() throws UserExitException {
        var userInput = "Test input string";
        var expectedInput = userInput;

        var menu = new InputMenu<>(
            "Input Test Title",
            new InputMenuItem<>((str) -> {
                assertEquals(expectedInput, str);
                return null;
            })
        );

        var scanner = charScanner(userInput);
        menu.prompt(scanner);
    }

    @Test()
    void stripInputValue() throws UserExitException {
        var userInput = "   Test input string   ";
        var expectedInput = userInput.strip();

        var menu = new InputMenu<>(
            "Input Test Title",
            new InputMenuItem<>((str) -> {
                assertEquals(expectedInput, str);
                return null;
            })
        );

        var scanner = charScanner(userInput);
        menu.prompt(scanner);
    }

    @Test()
    void outputValue() throws UserExitException {
        var userInput = "Test input string";
        var expectedInput = userInput;
        var expectedOutput = "Test Output";

        InputMenu<String> menu = new InputMenu<>(
            "Output Test Title",
            new InputMenuItem<>((str) -> {
                assertEquals(expectedInput, str);
                return expectedOutput;
            })
        );

        var scanner = charScanner(userInput);
        var output = menu.prompt(scanner);

        assertEquals(expectedOutput, output);
    }
}
