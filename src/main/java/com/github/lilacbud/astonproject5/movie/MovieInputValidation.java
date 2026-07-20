package com.github.lilacbud.astonproject5.movie;

import com.github.lilacbud.astonproject5.util.InputValidation;
import java.util.Optional;

public final class MovieInputValidation {
    public static final int MIN_YEAR = 1888;
    public static final float MIN_HOUR_LENGTH = 0.01F;
    
    public static final String YEAR_LESS_THAN_MIN_MESSAGE = "There were no films in that year";
    public static final String HOUR_LENGTH_NEGATIVE_MESSAGE = "Hour length must not be negative";
    public static final String HOUR_LENGTH_LESS_THAN_MIN_MESSAGE = "Hour length must not be that small";
    public static final String HOUR_LENGTH_INVALID_MESSAGE = "Hour length must be a real number";
    
    private MovieInputValidation() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static Optional<String> validateName(String input) {
        return InputValidation.validateInput(input).map(s -> s.replaceAll("\\s+", " "));
    }
    
    static Optional<Integer> validateYearOfRelease(String input) {
        final Optional<Integer> validatedInput = InputValidation.validateIntegerInput(input);
        if (validatedInput.isEmpty()) {
            return Optional.empty();
        }
        try {
            final Integer convertedYear = validatedInput.get();
            if (convertedYear < MIN_YEAR) {
                throw new IllegalArgumentException();
            }
            return Optional.of(convertedYear);
        } catch (IllegalArgumentException e) {
            System.err.println(YEAR_LESS_THAN_MIN_MESSAGE);
        }
        return Optional.empty();
    }
    
    static Optional<Float> validateHourLength(String input){
        final Optional<String> validatedInput = InputValidation.validateInput(input);
        if (validatedInput.isEmpty()) {
            return Optional.empty();
        }
        try {
            final Float convertedHL = Float.valueOf(validatedInput.get());
            if (convertedHL < MIN_HOUR_LENGTH) {
                String message;
                if (convertedHL < 0F) {
                    message = HOUR_LENGTH_NEGATIVE_MESSAGE;
                }
                else {
                    message = HOUR_LENGTH_LESS_THAN_MIN_MESSAGE;
                }
                throw new IllegalArgumentException(message);
            } 
            return Optional.of(convertedHL);
        } catch (NumberFormatException e) {
            System.err.println(HOUR_LENGTH_INVALID_MESSAGE);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        return Optional.empty();
    }
}
