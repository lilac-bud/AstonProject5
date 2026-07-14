package com.github.lilacbud.astonproject5.user;

import com.github.lilacbud.astonproject5.util.InputValidation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Menu {
    private final String title;
    private final String prompt;
    private final List<MenuOption> options;
    
    private Menu(StepBuilder builder) {
        this.title = builder.title;
        this.prompt = builder.prompt;
        this.options = builder.options;
    }
    
    public MenuOption chooseOption(Scanner scanner) {
        if (options.size() == 1)
            return options.get(0);
        if (scanner == null)
            throw new IllegalArgumentException("Scanner cannot be null");
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
    public static interface MenuCommand {
        void execute();
    }
    public static class MenuOption {
        private final String title;
        private final MenuCommand command;
        
        public MenuOption(String title, MenuCommand command) {
            if (title == null || command == null)
                throw new IllegalArgumentException("Both title and command cannot be null");
            this.title = title;
            this.command = command;
        }
        private String getTitle() {
            return title;
        }
        public void execute() {
            command.execute();
        }
    }
    
    public static interface TitleBuilder {
        public PromptBuilder withTitle(String title);
    }
    public static interface PromptBuilder {
        public OptionBuilder withPrompt(String prompt);
    }
    public static interface OptionBuilder {
        public StepBuilder withOption(MenuOption option);
    }
    public static class StepBuilder implements TitleBuilder, PromptBuilder, OptionBuilder {
        private String title;
        private String prompt;
        private final List<MenuOption> options = new ArrayList<>();
        
        private StepBuilder() {}
        
        public static TitleBuilder newBuilder() {
            return new StepBuilder();
        }
        @Override
        public PromptBuilder withTitle(String title) {
            if (title == null)
                throw new IllegalArgumentException("Title cannot be null");
            this.title = title;
            return this;
        }
        @Override
        public OptionBuilder withPrompt(String prompt) {
            if (prompt == null)
                throw new IllegalArgumentException("Prompt cannot be null");
            this.prompt = prompt;
            return this;
        }
        @Override
        public StepBuilder withOption(MenuOption option) {
            if (option == null)
                throw new IllegalArgumentException("Option cannot be null");
            options.add(option);
            return this;
        }
        public Menu build() {
            return new Menu(this);
        }
    }
}
