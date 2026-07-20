package com.github.lilacbud.astonproject5.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InputValidationTest {
    private final PrintStream originalErr = System.err;
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    
    @BeforeEach
    public void setUp() {
        System.setErr(new PrintStream(errContent));
    }
    
    @AfterEach
    public void tearDown() {
        System.setErr(originalErr);
    }

    @Test
    public void givenNull_whenValindatingInput_thenReturnEmptyOptional() {
        String input = null;
        Optional<String> result = InputValidation.validateInput(input);
        assertTrue(result.isEmpty());
        assertEquals(InputValidation.INPUT_NULL_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void givenBlankString_whenValidatingInput_thenReturnEmptyOptional() {
        String input = "\n";
        Optional<String> result = InputValidation.validateInput(input);
        assertTrue(result.isEmpty());
        assertEquals(InputValidation.INPUT_EMPTY_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void givenNotEmptyString_whenValidatingInput_thenReturnOptionalOfTrimmedString() {
        String input = "Correct input\n";
        Optional<String> expectedResult = Optional.of(input.trim());
        Optional<String> result = InputValidation.validateInput(input);
        assertTrue(result.isPresent());
        assertEquals(expectedResult, result);
        assertTrue(errContent.toString().isEmpty());
    }
    
    @Test
    public void givenNull_whenValidatingIntegerInput_thenReturnEmptyOptional() {
        String input = null;
        Optional<Integer> result = InputValidation.validateIntegerInput(input);
        assertTrue(result.isEmpty());
        assertEquals(InputValidation.INPUT_NULL_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void givenBlankString_whenValidatingIntegerInput_thenReturnEmptyOptional() {
        String input = "\n";
        Optional<Integer> result = InputValidation.validateIntegerInput(input);
        assertTrue(result.isEmpty());
        assertEquals(InputValidation.INPUT_EMPTY_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void givenUnconvertableString_whenValidatingIntegerInput_thenReturnEmptyOptional() {
        String input = "qwerty\n";
        Optional<Integer> result = InputValidation.validateIntegerInput(input);
        assertTrue(result.isEmpty());
        assertEquals(InputValidation.INTEGER_INPUT_INVALID_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void givenNegativeIntegerAsString_whenValidatingIntegerInput_thenReturnEmptyOptional() {
        String input = "-2\n";
        Optional<Integer> result = InputValidation.validateIntegerInput(input);
        assertTrue(result.isEmpty());
        assertEquals(InputValidation.INTEGER_INPUT_NEGATIVE_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void givenPositiveIntegerAsString_whenValidatingIntegerInput_thenReturnOptionalOfInteger() {
        int expectedNumber = 2;
        String input = expectedNumber + "\n";
        Optional<Integer> result = InputValidation.validateIntegerInput(input);
        assertEquals(expectedNumber, result.orElseThrow());
        assertTrue(errContent.toString().isEmpty());
    }
}
