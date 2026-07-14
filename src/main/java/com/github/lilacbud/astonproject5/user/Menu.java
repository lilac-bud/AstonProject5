package com.github.lilacbud.astonproject5.user;

import com.github.lilacbud.astonproject5.util.InputValidation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;
import static java.util.Objects.requireNonNull;

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
                System.out.println(title);
                IntStream.range(0, options.size())
                        .mapToObj(i -> String.format("%d. %s", i + 1, options.get(i).getTitle()))
                        .forEach(System.out::println);
                System.out.print(prompt);
                validatedInput = InputValidation.validateIntegerInput(scanner.nextLine());
            } while (validatedInput.isEmpty());
            try {
                return options.get(validatedInput.get() - 1);
            } catch (IndexOutOfBoundsException e) {
                System.err.println("No such option");
            }
        }
    }
    
    @FunctionalInterface
    public static interface MenuCommand<T> {
        public void execute(T client);
    }
    public static class MenuOption<T> {
        private final String title;
        private final MenuCommand<T> command;
        
        public MenuOption(String title, MenuCommand<T> command) {
            this.title = requireNonNull(title, "Title cannot be null");
            this.command = requireNonNull(command, "Command cannot be null");
        }
        private String getTitle() {
            return title;
        }
        public void execute(T client) {
            command.execute(client);
        }
    }
    
    public static interface TitleBuilder<T> {
        public PromptBuilder<T> withTitle(String title);
    }
    public static interface PromptBuilder<T> {
        public OptionBuilder<T> withPrompt(String prompt);
    }
    public static interface OptionBuilder<T> {
        public StepBuilder<T> withOption(MenuOption<T> option);
    }
    public static class StepBuilder<T> implements TitleBuilder<T>, PromptBuilder<T>, OptionBuilder<T> {
        private String title;
        private String prompt;
        private final List<MenuOption<T>> options = new ArrayList<>();
        
        private StepBuilder() {}
        
        public static <T> TitleBuilder<T> newBuilder() {
            return new StepBuilder<>();
        }
        @Override
        public PromptBuilder<T> withTitle(String title) {
            this.title = requireNonNull(title, "Title cannot be null");
            return this;
        }
        @Override
        public OptionBuilder<T> withPrompt(String prompt) {
            this.prompt = requireNonNull(prompt, "Prompt cannot be null");
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
