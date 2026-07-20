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
    public void givenNull_whenValidatingName_thenReturnEmptyOptional() {
        String input = null;
        Optional<String> result = validateInputWithMock(() -> MovieInputValidation.validateName(input));
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void givenBlankString_whenValidatingName_thenReturnEmptyOptional() {
        String input = "\n";
        Optional<String> result = validateInputWithMock(() -> MovieInputValidation.validateName(input));
        assertTrue(result.isEmpty());
    }
    @Test
    public void givenNotEmptyString_whenValidatingName_thenReturnOptionalOfTrimmedStringWithoutExtraSpaces() {
        String input = "Correct     input\n";
        Optional<String> expectedResult = Optional.of(input.trim().replaceAll("\\s+", " "));
        Optional<String> result = validateInputWithMock(() -> MovieInputValidation.validateName(input));
        assertEquals(expectedResult, result);
    }

    @Test
    public void givenNull_whenValidatingYearOfRelease_thenReturnEmptyOptional() {
        String input = null;
        Optional<Integer> result = validateInputWithMock(() -> MovieInputValidation.validateYearOfRelease(input));
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void givenBlankString_whenValidatingYearOfRelease_thenReturnEmptyOptional() {
        String input = "\n";
        Optional<Integer> result = validateInputWithMock(() -> MovieInputValidation.validateYearOfRelease(input));
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void givenUnconvertableString_whenValidatingYearOfRelease_thenReturnEmptyOptional() {
        String input = "qwerty";
        Optional<Integer> result = validateInputWithMock(() -> MovieInputValidation.validateYearOfRelease(input));
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void givenIntegerLessThanMinYearAsString_whenValidatingYearOfRelease_thenReturnEmptyOptional() {
        String input = "1500";
        Optional<Integer> result = validateInputWithMock(() -> MovieInputValidation.validateYearOfRelease(input));
        assertTrue(result.isEmpty());
        assertEquals(MovieInputValidation.YEAR_LESS_THAN_MIN_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void givenIntegerAsString_whenValidatingYearOfRelease_thenReturnOptionalOfInteger() {
        String input = "2026";
        Optional<Integer> expectedResult = Optional.of(Integer.valueOf(input));
        Optional<Integer> result = validateInputWithMock(() -> MovieInputValidation.validateYearOfRelease(input));
        assertEquals(expectedResult, result);
        assertTrue(errContent.toString().isEmpty());
    }

    @Test
    public void givenNull_whenValidatingHourLength_thenReturnEmptyOptional() {
        String input = null;
        Optional<Float> result = validateInputWithMock(() -> MovieInputValidation.validateHourLength(input));
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void givenBlankString_whenValidatingHourLength_thenReturnEmptyOptional() {
        String input = "\n";
        Optional<Float> result = validateInputWithMock(() -> MovieInputValidation.validateHourLength(input));
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void givenUnconvertableString_whenValidatingHourLength_thenReturnEmptyOptional() {
        String input = "qwerty";
        Optional<Float> result = MovieInputValidation.validateHourLength(input);
        assertTrue(result.isEmpty());
        assertEquals(MovieInputValidation.HOUR_LENGTH_INVALID_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void givenNegativeFloatAsString_whenValidatingHourLength_thenReturnEmptyOptional() {
        String input = "-2";
        Optional<Float> result = MovieInputValidation.validateHourLength(input);
        assertTrue(result.isEmpty());
        assertEquals(MovieInputValidation.HOUR_LENGTH_NEGATIVE_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void givenFloatLessThanMinHourLengthAsString_whenValidatingHourLength_thenReturnEmptyOptional() {
        String input = "0.000001";
        Optional<Float> result = MovieInputValidation.validateHourLength(input);
        assertTrue(result.isEmpty());
        assertEquals(MovieInputValidation.HOUR_LENGTH_LESS_THAN_MIN_MESSAGE, errContent.toString().trim());
    }
    
    @Test
    public void givenFloatAsString_whenValidatingHourLength_thenReturnEmptyOptional() {
        String input = "2.5";
        Optional<Float> expectedResult = Optional.of(Float.valueOf(input));
        Optional<Float> result = MovieInputValidation.validateHourLength(input);
        assertEquals(expectedResult, result);
        assertTrue(errContent.toString().isEmpty());
    }
}
