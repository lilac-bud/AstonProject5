package com.github.lilacbud.astonproject5.movie;

import com.github.lilacbud.astonproject5.util.InputValidation;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.function.Supplier;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;

public class MovieInputValidationTest {
    private final PrintStream originalErr = System.err;
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    
    private <T> Optional<T> validateInputWithMock(Supplier<Optional<T>> validator) {
        try (MockedStatic<InputValidation> validation = mockStatic(InputValidation.class)) {
            validation.when(() -> InputValidation.validateInput(anyString()))
                    .thenAnswer(i -> Optional.ofNullable(i.getArgument(0).toString().trim()));
            validation.when(() -> InputValidation.validateInput("\n")).thenReturn(Optional.empty());
            validation.when(() -> InputValidation.validateIntegerInput(anyString()))
                    .thenAnswer(i -> Optional.ofNullable(Integer.valueOf(i.getArgument(0))));
            validation.when(() -> InputValidation.validateIntegerInput("\n")).thenReturn(Optional.empty());
            validation.when(() -> InputValidation.validateIntegerInput("qwerty")).thenReturn(Optional.empty());
            
            return validator.get();
        }
    }

    @BeforeEach
    public void setUp() {
        System.setErr(new PrintStream(errContent));
    }
    
    @AfterEach
    public void tearDown() {
        System.setErr(originalErr);
    }

    @Test
    public void testValidateNameGivenNull() {
        String input = null;
        Optional<String> result = validateInputWithMock(() -> MovieInputValidation.validateName(input));
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testValidateNameGivenBlankString() {
        String input = "\n";
        Optional<String> result = validateInputWithMock(() -> MovieInputValidation.validateName(input));
        assertTrue(result.isEmpty());
    }
    @Test
    public void testValidateNameGivenValidString() {
        String input = "Correct     input\n";
        Optional<String> expectedResult = Optional.of(input.trim().replaceAll("\\s+", " "));
        Optional<String> result = validateInputWithMock(() -> MovieInputValidation.validateName(input));
        assertEquals(expectedResult, result);
    }

    @Test
    public void testValidateYearOfReleaseGivenNull() {
        String input = null;
        Optional<Integer> result = validateInputWithMock(() -> MovieInputValidation.validateYearOfRelease(input));
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testValidateYearOfReleaseGivenBlankString() {
        String input = "\n";
        Optional<Integer> result = validateInputWithMock(() -> MovieInputValidation.validateYearOfRelease(input));
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testValidateYearOfReleaseGivenUnconvertableString() {
        String input = "qwerty";
        Optional<Integer> result = validateInputWithMock(() -> MovieInputValidation.validateYearOfRelease(input));
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testValidateYearOfReleaseGivenYearLessThanMinYear() {
        String input = "1500";
        Optional<Integer> result = validateInputWithMock(() -> MovieInputValidation.validateYearOfRelease(input));
        assertTrue(result.isEmpty());
        assertEquals(MovieInputValidation.YEAR_LESS_THAN_MIN_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void testValidateYearOfReleaseGivenValidYear() {
        String input = "2026";
        Optional<Integer> expectedResult = Optional.of(Integer.valueOf(input));
        Optional<Integer> result = validateInputWithMock(() -> MovieInputValidation.validateYearOfRelease(input));
        assertEquals(expectedResult, result);
        assertTrue(errContent.toString().isEmpty());
    }

    @Test
    public void testValidateHourLengthGivenNull() {
        System.out.println("validateHourLength given null");
        String input = null;
        Optional<Float> result = MovieInputValidation.validateHourLength(input);
        assertTrue(result.isEmpty());
        assertEquals(InputValidation.INPUT_NULL_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void testValidateHourLengthGivenBlankString() {
        System.out.println("validateHourLength given blank string");
        String input = "\n";
        Optional<Float> result = MovieInputValidation.validateHourLength(input);
        assertTrue(result.isEmpty());
        assertEquals(InputValidation.INPUT_EMPTY_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void testValidateHourLengthGivenUnconvertableString() {
        System.out.println("validateHourLength given unconvertable string");
        String input = "qwerty";
        Optional<Float> result = MovieInputValidation.validateHourLength(input);
        assertTrue(result.isEmpty());
        assertEquals(MovieInputValidation.HOUR_LENGTH_INVALID_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void testValidateHourLengthGivenNegativeHourLength() {
        System.out.println("validateHourLength given negative hour length");
        String input = "-2";
        Optional<Float> result = MovieInputValidation.validateHourLength(input);
        assertTrue(result.isEmpty());
        assertEquals(MovieInputValidation.HOUR_LENGTH_NEGATIVE_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void testValidateHourLengthGivenHourLengthLessThanMinHourLength() {
        System.out.println("validateHourLength given hour length less than min hour length");
        String input = "0.000001";
        Optional<Float> result = MovieInputValidation.validateHourLength(input);
        assertTrue(result.isEmpty());
        assertEquals(MovieInputValidation.HOUR_LENGTH_LESS_THAN_MIN_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void testValidateHourLengthGivenValidHourLength() {
        System.out.println("validateHourLength given valid hour length");
        String input = "2.5";
        Optional<Float> expectedResult = Optional.of(Float.valueOf(input));
        Optional<Float> result = MovieInputValidation.validateHourLength(input);
        assertEquals(expectedResult, result);
        assertTrue(errContent.toString().isEmpty());
    }
}
