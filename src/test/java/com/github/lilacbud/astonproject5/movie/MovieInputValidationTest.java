package com.github.lilacbud.astonproject5.movie;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MovieInputValidationTest {
    
    private final PrintStream originalErr = System.err;
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    
    public MovieInputValidationTest() {
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
        System.out.println("validateName given null");
        String input = null;
        Optional<String> result = MovieInputValidation.validateName(input);
        assertTrue(result.isEmpty());
        assertEquals("Input cannot be empty", errContent.toString().trim());
    }
    @Test
    public void testValidateNameGivenBlankString() {
        System.out.println("validateName given blank string");
        String input = "\n";
        Optional<String> result = MovieInputValidation.validateName(input);
        assertTrue(result.isEmpty());
        assertEquals("Input cannot be empty", errContent.toString().trim());
    }
    @Test
    public void testValidateNameGivenValidString() {
        System.out.println("validateName given valid string");
        String input = "Correct     input\n";
        Optional<String> expectedResult = Optional.of("Correct input");
        Optional<String> result = MovieInputValidation.validateName(input);
        assertTrue(result.isPresent());
        assertEquals(expectedResult, result);
        assertTrue(errContent.toString().isEmpty());
    }

    @Test
    public void testValidateYearOfReleaseGivenNull() {
        System.out.println("validateYearOfRelease given null");
        String input = null;
        Optional<Integer> result = MovieInputValidation.validateYearOfRelease(input);
        assertTrue(result.isEmpty());
        assertEquals("Input cannot be empty", errContent.toString().trim());
    }
    @Test
    public void testValidateYearOfReleaseGivenBlankString() {
        System.out.println("validateYearOfRelease given blank string");
        String input = "\n";
        Optional<Integer> result = MovieInputValidation.validateYearOfRelease(input);
        assertTrue(result.isEmpty());
        assertEquals("Input cannot be empty", errContent.toString().trim());
    }
    @Test
    public void testValidateYearOfReleaseGivenUnconvertableString() {
        System.out.println("validateYearOfRelease given unconvertable string");
        String input = "qwerty";
        Optional<Integer> result = MovieInputValidation.validateYearOfRelease(input);
        assertTrue(result.isEmpty());
        assertEquals("That's not a year", errContent.toString().trim());
    }
    @Test
    public void testValidateYearOfReleaseGivenYearLessThanMinYear() {
        System.out.println("validateYearOfRelease given year less than min year");
        String input = "1500";
        Optional<Integer> result = MovieInputValidation.validateYearOfRelease(input);
        assertTrue(result.isEmpty());
        assertEquals("There were no films in that year", errContent.toString().trim());
    }
    @Test
    public void testValidateYearOfReleaseGivenValidYear() {
        System.out.println("validateYearOfRelease given valid year");
        String input = "2026";
        Optional<Integer> result = MovieInputValidation.validateYearOfRelease(input);
        assertTrue(result.isPresent());
        assertEquals(2026, result.get());
        assertTrue(errContent.toString().isEmpty());
    }

    @Test
    public void testValidateHourLengthGivenNull() {
        System.out.println("validateHourLength given null");
        String input = null;
        Optional<Float> result = MovieInputValidation.validateHourLength(input);
        assertTrue(result.isEmpty());
        assertEquals("Input cannot be empty", errContent.toString().trim());
    }
    @Test
    public void testValidateHourLengthGivenBlankString() {
        System.out.println("validateHourLength given blank string");
        String input = "\n";
        Optional<Float> result = MovieInputValidation.validateHourLength(input);
        assertTrue(result.isEmpty());
        assertEquals("Input cannot be empty", errContent.toString().trim());
    }
    @Test
    public void testValidateHourLengthGivenUnconvertableString() {
        System.out.println("validateHourLength given unconvertable string");
        String input = "qwerty";
        Optional<Float> result = MovieInputValidation.validateHourLength(input);
        assertTrue(result.isEmpty());
        assertEquals("That's not an hour length", errContent.toString().trim());
    }
    @Test
    public void testValidateHourLengthGivenNegativeHourLength() {
        System.out.println("validateHourLength given negative hour length");
        String input = "-2";
        Optional<Float> result = MovieInputValidation.validateHourLength(input);
        assertTrue(result.isEmpty());
        assertEquals("Hour length cannot be negative", errContent.toString().trim());
    }
    @Test
    public void testValidateHourLengthGivenHourLengthLessThanMinHourLength() {
        System.out.println("validateHourLength given hour length less than min hour length");
        String input = "0.000001";
        Optional<Float> result = MovieInputValidation.validateHourLength(input);
        assertTrue(result.isEmpty());
        assertEquals("Hour length cannot be that small", errContent.toString().trim());
    }
    @Test
    public void testValidateHourLengthGivenValidHourLength() {
        System.out.println("validateHourLength given valid hour length");
        String input = "2.5";
        Optional<Float> result = MovieInputValidation.validateHourLength(input);
        assertTrue(result.isPresent());
        assertEquals(2.5F, result.get());
        assertTrue(errContent.toString().isEmpty());
    }
}
