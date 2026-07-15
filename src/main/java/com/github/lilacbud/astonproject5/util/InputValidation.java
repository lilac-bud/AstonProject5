package com.github.lilacbud.astonproject5.util;

import java.util.Optional;

public final class InputValidation {
    private InputValidation() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Optional<String> validateInput(String input) {
        if (input == null || input.isBlank()) {
            System.err.println("Input cannot be empty");
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
            System.err.println("Input must be a whole number");
        } catch (IllegalArgumentException e) {
            System.err.println("Input cannot be negative");
        }
        return Optional.empty();
    }
}
