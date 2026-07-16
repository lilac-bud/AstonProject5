package com.github.lilacbud.astonproject5.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;

public class InputRequestTest {
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    
    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testAskStringGivenNullScanner() {
        System.out.println("askString given null scanner");
        var thrown = assertThrows(NullPointerException.class, () -> InputRequest.askString(null));
        assertEquals("Scanner must not be null", thrown.getMessage());
    }
    @Test
    public void testAskStringGivenValidScanner() {
        System.out.println("askString given valid scanner");
        System.setOut(new PrintStream(outContent));
        final String prompt = "Filepath:";
        String result;
        try (MockedStatic<InputValidation> validation = mockStatic(InputValidation.class)) {
            validation.when(() -> InputValidation.validateInput(anyString()))
                    .thenAnswer(i -> Optional.of(i.getArgument(0)));
            validation.when(() -> InputValidation.validateInput("")).thenReturn(Optional.empty());
            validation.when(() -> InputValidation.validateInput(" ")).thenReturn(Optional.empty());

            result = InputRequest.askString(new Scanner("\n \nfilepath\n"), prompt);
        }
        assertEquals("filepath", result);
        assertEquals(prompt.repeat(3), outContent.toString().trim());
    }

    @Test
    public void testAskIntegerGivenNullScanner() {
        System.out.println("askInteger given null scanner");
        var thrown = assertThrows(NullPointerException.class, () -> InputRequest.askInteger(null));
        assertEquals("Scanner must not be null", thrown.getMessage());
    }
    
    @Test
    public void testAskIntegerGivenValidScanner() {
        System.out.println("askInteger given valid scanner");
        System.setOut(new PrintStream(outContent));
        final String prompt = "Size:";
        int result;
        try (MockedStatic<InputValidation> validation = mockStatic(InputValidation.class)) {
            validation.when(() -> InputValidation.validateIntegerInput(anyString()))
                    .thenAnswer(i -> Optional.of(Integer.valueOf(i.getArgument(0))));
            validation.when(() -> InputValidation.validateIntegerInput("")).thenReturn(Optional.empty());
            validation.when(() -> InputValidation.validateIntegerInput(" ")).thenReturn(Optional.empty());
            validation.when(() -> InputValidation.validateIntegerInput("qwerty")).thenReturn(Optional.empty());
            validation.when(() -> InputValidation.validateIntegerInput("-2")).thenReturn(Optional.empty());

            result = InputRequest.askInteger(new Scanner("\n \nqwerty\n-2\n2\n"), prompt);
        }
        assertEquals(2, result);
        assertEquals(prompt.repeat(5), outContent.toString().trim());
    }
}
