package com.github.lilacbud.astonproject5.user;

import com.github.lilacbud.astonproject5.util.InputValidation;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Menu {
    private final String title;
    private final String prompt;
    private final List<MenuOption> options;
    
    public Menu(String title, String prompt, List<MenuOption> options) {
        if (title == null)
            throw new IllegalArgumentException("Title cannot be null");
        if (prompt == null)
            throw new IllegalArgumentException("Choosing prompt cannot be null");
        if (options == null)
            throw new IllegalArgumentException("Options cannot be null");
        if (options.isEmpty())
            throw new IllegalArgumentException("Options cannot be empty");
        this.title = title;
        this.prompt = prompt;
        this.options = options;
    }
    
    public MenuOption chooseOption(Scanner scanner) {
        if (options.size() == 1) {
            return options.get(0);
        }
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
    
    public static interface MenuCommand {
        void execute();
    }
    public static class MenuOption {
        private final String title;
        private final MenuCommand command;
        
        public MenuOption(String title, MenuCommand command) {
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
}
