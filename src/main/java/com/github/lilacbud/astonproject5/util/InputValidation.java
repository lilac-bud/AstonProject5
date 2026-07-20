package com.github.lilacbud.astonproject5.util;

import java.util.Optional;

public final class InputValidation {
    public static final String INPUT_NULL_MESSAGE = "Input must not be null";
    public static final String INPUT_EMPTY_MESSAGE = "Input must not be empty";
    public static final String INTEGER_INPUT_INVALID_MESSAGE = "Input must be a whole number";
    public static final String INTEGER_INPUT_NEGATIVE_MESSAGE = "Input must not be negative";
    
    private InputValidation() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Optional<String> validateInput(String input) {
        if (input == null) {
            System.err.println(INPUT_NULL_MESSAGE);
            return Optional.empty();
        }
        if (input.isBlank()) {
            System.err.println(INPUT_EMPTY_MESSAGE);
            return Optional.empty();
        }
        return Optional.of(input.trim());
    }

    public static Optional<Integer> validateIntegerInput(String input) {
        final Optional<String> validatedInput = validateInput(input);
        if (validatedInput.isEmpty()) {
            return Optional.empty();
        }
        try {
            final Integer convertedInput = Integer.valueOf(validatedInput.get());
            if (convertedInput < 0) {
                throw new IllegalArgumentException();
            }
            return Optional.of(convertedInput);
        } catch (NumberFormatException e) {
            System.err.println(INTEGER_INPUT_INVALID_MESSAGE);
        } catch (IllegalArgumentException e) {
            System.err.println(INTEGER_INPUT_NEGATIVE_MESSAGE);
        }
        return Optional.empty();
    }
}
