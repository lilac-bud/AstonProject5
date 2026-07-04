package com.github.lilacbud.astonproject5.user.ui;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

public final class PromptHelpers {
    private PromptHelpers() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static <T> T promptUserInput(
        Scanner scanner,
        String prompt,
        Function<String, T> parser,
        Predicate<T> validator
    ) {
        System.out.print(prompt);

        while (scanner.hasNext()) {
            String input = scanner.nextLine().strip();

            try {
                var value = parser.apply(input);

                if (Objects.nonNull(validator) && !validator.test(value)) {
                    throw new IOException();
                }

                return value;
            } catch (Throwable ex) {
                System.out.println("Ошибка ввода, попробуйте еще раз.");
                System.out.print(prompt);
            }
        }

        return null;
    }

    public static UIMenuItemOption promptUserSelect(
        Scanner scanner,
        String prompt,
        List<UIMenuItemOption> options
    ) {
        var promptWithOptions = new StringBuilder(prompt);
        promptWithOptions.append("\n");

        for (int i = 0; i < options.size(); i++) {
            var option = options.get(i);
            var title = option.getTitle();

            if (Objects.nonNull(title)) {
                promptWithOptions.append(title);
                promptWithOptions.append("\n");
            }
        }

        promptWithOptions.append("> ");

        var selectedKey = promptUserInput(
            scanner,
            promptWithOptions.toString(),
            (val) -> val.strip().charAt(0),
            (val) -> {
                if (Objects.isNull(val)) {
                    return false;
                }

                return options.stream()
                    .anyMatch(opt -> opt.matchKey(val));
            }
        );

        return options.stream()
            .filter(opt -> opt.matchKey(selectedKey))
            .findFirst()
            .orElse(null);
    }
}
