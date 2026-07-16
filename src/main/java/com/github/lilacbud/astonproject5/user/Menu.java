package com.github.lilacbud.astonproject5.user;

import com.github.lilacbud.astonproject5.util.InputValidation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;
import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

public class Menu<T> {
    private final String title;
    private final String prompt;
    private final List<MenuOption<T>> options;
    
    private Menu(StepBuilder<T> builder) {
        this.title = builder.title;
        this.prompt = builder.prompt;
        this.options = builder.options;
    }
    
    public MenuOption<T> chooseOption(Scanner scanner) {
        if (options.size() == 1)
            return options.get(0);
        requireNonNull(scanner, "Scanner cannot be null");
        Optional<Integer> validatedInput;
        while (true) {
            do {
                if (title != null)
                    System.out.println(title);
                IntStream.range(0, options.size())
                        .filter(i -> Objects.nonNull(options.get(i).getTitle()))
                        .mapToObj(i -> String.format("%d. %s", i + 1, options.get(i).getTitle()))
                        .forEach(System.out::println);
                System.out.print(requireNonNullElse(prompt, ""));
                validatedInput = InputValidation.validateIntegerInput(scanner.nextLine());
            } while (validatedInput.isEmpty());
            try {
                return options.get(validatedInput.get() - 1);
            } catch (IndexOutOfBoundsException e) {
                System.err.println("No such option");
            }
        }
    }
    
    public static class MenuOption<T> {
        private final String title;
        private final MenuCommand<T> command;
        
        public MenuOption(String title, MenuCommand<T> command) {
            this.title = title;
            this.command = requireNonNull(command, "Command must not be null");
        }
        public MenuOption(MenuCommand<T> command) {
            this(null, command);
        }
        private String getTitle() {
            return title;
        }
        public void execute(T client) {
            command.execute(client);
        }
    }
    
    public static interface OptionBuilder<T> {
        public OptionBuilder<T> withTitle(String title);
        public OptionBuilder<T> withPrompt(String prompt);
        public StepBuilder<T> withOption(MenuOption<T> option);
    }
    public static class StepBuilder<T> implements OptionBuilder<T> {
        private String title;
        private String prompt;
        private final List<MenuOption<T>> options = new ArrayList<>();
        
        private StepBuilder() {}
        
        public static <T> OptionBuilder<T> newBuilder() {
            return new StepBuilder<>();
        }
        @Override
        public OptionBuilder<T> withTitle(String title) {
            this.title = title;
            return this;
        }
        @Override
        public OptionBuilder<T> withPrompt(String prompt) {
            this.prompt = prompt;
            return this;
        }
        @Override
        public StepBuilder<T> withOption(MenuOption<T> option) {
            options.add(requireNonNull(option, "Option cannot be null"));
            return this;
        }
        public Menu<T> build() {
            return new Menu<>(this);
        }
    }
}
