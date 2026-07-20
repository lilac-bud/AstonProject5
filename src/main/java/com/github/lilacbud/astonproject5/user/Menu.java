package com.github.lilacbud.astonproject5.user;

import com.github.lilacbud.astonproject5.util.InputValidation;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class Menu<T> {
    public static final String SCANNER_NULL_MESSAGE = "Scanner must not be null";
    public static final String NO_OPTION_MESSAGE = "No such option";
    
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
        requireNonNull(scanner, SCANNER_NULL_MESSAGE);
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
                System.err.println(NO_OPTION_MESSAGE);
            }
        }
    }
    
    public void chooseOptionAndExecute(Scanner scanner, T client) {
        chooseOption(scanner).execute(client);
    }
    
    public static class MenuOption<T> {
        public static final String COMMAND_NULL_MESSAGE = "Command must not be null";
        
        private final Optional<String> title;
        private final Consumer<T> command;
        
        public MenuOption(String title, Consumer<T> command) {
            this.title = Optional.ofNullable(title);
            this.command = requireNonNull(command, COMMAND_NULL_MESSAGE);
        }
        
        public MenuOption(Consumer<T> command) {
            this(null, command);
        }
        
        public void execute(T client) {
            command.accept(client);
        }
    }
    
    public static interface OptionBuilder<T> {
        public OptionBuilder<T> withTitle(String title);
        public OptionBuilder<T> withPrompt(String prompt);
        public StepBuilder<T> withOption(MenuOption<T> option);
    }
    
    public static class StepBuilder<T> implements OptionBuilder<T> {
        public static final String OPTION_NULL_MESSAGE = "Option must not be null";
        
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
