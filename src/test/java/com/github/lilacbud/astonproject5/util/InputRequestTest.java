package com.github.lilacbud.astonproject5.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;

public class InputRequestTest {
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    
    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }
    
    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void givenNullAsScanner_whenAskingForString_thenThrow() {
        final var thrown = assertThrows(NullPointerException.class, () -> InputRequest.askString(null));
        assertEquals(InputRequest.SCANNER_NULL_MESSAGE, thrown.getMessage());
    }
    
    @Test
    public void givenScanner_whenAskingForString_thenReturnString() {
        final String prompt = "Filepath:";
        final String expectedResult = "filepath";
        final String input = String.format("\n \n%s\n", expectedResult);
        String result;
        try (MockedStatic<InputValidation> validation = mockStatic(InputValidation.class)) {
            validation.when(() -> InputValidation.validateInput(anyString()))
                    .thenAnswer(i -> Optional.of(i.getArgument(0)));
            validation.when(() -> InputValidation.validateInput("")).thenReturn(Optional.empty());
            validation.when(() -> InputValidation.validateInput(" ")).thenReturn(Optional.empty());

            result = InputRequest.askString(new Scanner(input), prompt);
        }
        assertEquals(expectedResult, result);
        assertEquals(prompt.repeat(3), outContent.toString().trim());
    }

    @Test
    public void givenNullAsScanner_whenAskingForInteger_thenThrow() {
        final var thrown = assertThrows(NullPointerException.class, () -> InputRequest.askInteger(null));
        assertEquals(InputRequest.SCANNER_NULL_MESSAGE, thrown.getMessage());
    }
    
    @Test
    public void givenScanner_whenAskingForInteger_thenReturnInteger() {
        final String prompt = "Size:";
        final int expectedResult = 2;
        final String input = String.format("\n \nqwerty\n-2\n%d\n", expectedResult);
        int result;
        try (MockedStatic<InputValidation> validation = mockStatic(InputValidation.class)) {
            validation.when(() -> InputValidation.validateIntegerInput(anyString()))
                    .thenAnswer(i -> Optional.of(Integer.valueOf(i.getArgument(0))));
            validation.when(() -> InputValidation.validateIntegerInput("")).thenReturn(Optional.empty());
            validation.when(() -> InputValidation.validateIntegerInput(" ")).thenReturn(Optional.empty());
            validation.when(() -> InputValidation.validateIntegerInput("qwerty")).thenReturn(Optional.empty());
            validation.when(() -> InputValidation.validateIntegerInput("-2")).thenReturn(Optional.empty());

            result = InputRequest.askInteger(new Scanner(input), prompt);
        }
        assertEquals(expectedResult, result);
        assertEquals(prompt.repeat(5), outContent.toString().trim());
    }
}
