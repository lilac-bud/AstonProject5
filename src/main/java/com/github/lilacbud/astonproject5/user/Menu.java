package com.github.lilacbud.astonproject5.user;

import com.github.lilacbud.astonproject5.util.InputValidation;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Menu<T> {
    private final Optional<String> title;
    private final Optional<String> prompt;
    private final List<MenuOption<T>> options;
    
    private Menu(StepBuilder<T> builder) {
        this.title = Optional.ofNullable(builder.title);
        this.prompt = Optional.ofNullable(builder.prompt);
        this.options = builder.options;
    }
    
    public MenuOption<T> chooseOption(Scanner scanner) {
        if (options.size() == 1) {
            return options.get(0);
        }
        requireNonNull(scanner, "Scanner must not be null");
        Optional<Integer> validatedInput;
        while (true) {
            do {
                title.ifPresent(System.out::println);
                IntStream.range(0, options.size())
                        .filter(i -> options.get(i).title.isPresent())
                        .mapToObj(i -> String.format("%d. %s", i + 1, options.get(i).title.get()))
                        .forEach(System.out::println);
                prompt.ifPresent(System.out::print);
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
        private final Optional<String> title;
        private final MenuCommand<T> command;
        
        public MenuOption(String title, MenuCommand<T> command) {
            this.title = Optional.ofNullable(title);
            this.command = requireNonNull(command, "Command must not be null");
        }
        public MenuOption(MenuCommand<T> command) {
            this(null, command);
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
            options.add(requireNonNull(option, "Option must not be null"));
            return this;
        }
        public Menu<T> build() {
            return new Menu<>(this);
        }
    }
}
