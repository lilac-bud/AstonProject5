package com.github.lilacbud.astonproject5.util;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;
import java.util.Optional;
import java.util.Scanner;

public final class InputRequest {
    private InputRequest() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    public static String askString(Scanner scanner, String prompt) {
        requireNonNull(scanner, "Scanner must not be null");
        Optional<String> validatedString;
        do {
            if (prompt != null)
                System.out.print(prompt);
            validatedString = InputValidation.validateInput(scanner.nextLine());
        } while (validatedString.isEmpty());
        return validatedString.get();
    }
    public static String askString(Scanner scanner) {
        return askString(scanner, null);
    }
    
    public static int askInteger(Scanner scanner, String prompt) {
        requireNonNull(scanner, "Scanner must not be null");
        Optional<Integer> validatedSize;
        do {
            System.out.print(requireNonNullElse(prompt, ""));
            validatedSize = InputValidation.validateIntegerInput(scanner.nextLine());
        } while (validatedSize.isEmpty());
        return validatedSize.get();
    }
    public static int askInteger(Scanner scanner) {
        return askInteger(scanner, null);
    }
}
