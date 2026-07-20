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
    public void testValidateInputGivenNull() {
        System.out.println("validateInput given null");
        String input = null;
        Optional<String> result = InputValidation.validateInput(input);
        assertTrue(result.isEmpty());
        assertEquals(InputValidation.INPUT_NULL_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void testValidateInputGivenBlankString() {
        System.out.println("validateInput given blank string");
        String input = "\n";
        Optional<String> result = InputValidation.validateInput(input);
        assertTrue(result.isEmpty());
        assertEquals(InputValidation.INPUT_EMPTY_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void testValidateInputGivenValidString() {
        System.out.println("validateInput given valid string");
        String input = "Correct input\n";
        Optional<String> expectedResult = Optional.of("Correct input");
        Optional<String> result = InputValidation.validateInput(input);
        assertTrue(result.isPresent());
        assertEquals(expectedResult, result);
        assertTrue(errContent.toString().isEmpty());
    }
    
    @Test
    public void testValidateIntegerInputGivenNull() {
        System.out.println("validateIntegerInput given null");
        String input = null;
        Optional<Integer> result = InputValidation.validateIntegerInput(input);
        assertTrue(result.isEmpty());
        assertEquals(InputValidation.INPUT_NULL_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void testValidateIntegerInputGivenBlankString() {
        System.out.println("validateIntegerInput given blank string");
        String input = "\n";
        Optional<Integer> result = InputValidation.validateIntegerInput(input);
        assertTrue(result.isEmpty());
        assertEquals(InputValidation.INPUT_EMPTY_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void testValidateIntegerInputGivenUnconvertableString() {
        System.out.println("validateIntegerInput given unconvertable string");
        String input = "qwerty\n";
        Optional<Integer> result = InputValidation.validateIntegerInput(input);
        assertTrue(result.isEmpty());
        assertEquals(InputValidation.INTEGER_INPUT_INVALID_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void testValidateIntegerInputGivenNegativeInteger() {
        System.out.println("validateIntegerInput given negative integer");
        String input = "-2\n";
        Optional<Integer> result = InputValidation.validateIntegerInput(input);
        assertTrue(result.isEmpty());
        assertEquals(InputValidation.INTEGER_INPUT_NEGATIVE_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void testValidateIntegerInputGivenValidInteger() {
        System.out.println("validateIntegerInput given valid integer");
        String input = "2\n";
        Optional<Integer> result = InputValidation.validateIntegerInput(input);
        assertTrue(result.isPresent());
        assertEquals(2, result.get());
        assertTrue(errContent.toString().isEmpty());
    }
}
