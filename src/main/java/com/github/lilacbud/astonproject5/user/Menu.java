package com.github.lilacbud.astonproject5.user;

import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.movie.MoviesFiller;
import com.github.lilacbud.astonproject5.movie.save.MoviesSaver;
import com.github.lilacbud.astonproject5.movie.sort.*;
import com.github.lilacbud.astonproject5.util.InputValidation;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Menu {
    private static Menu instance;
    private boolean running = true;
    private final List<Movie> movies = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private MoviesFiller filler;
    private MoviesSorter sorter;
    private MoviesSaver saver;
    
    private SubMenu mainMenu;
    private SubMenu fillMenu;
    private SubMenu sortMenu;
    private SubMenu compMenu;
    private SubMenu saveMenu;
    
    private Menu() {}
    public static Menu getInstance(){
        if (instance == null)
            instance = new Menu();
        return instance;
    }
    public void run(){
        while (running)
            mainMenu.chooseOption(scanner).execute();
    }
    
    public Scanner getScanner() {
        return scanner;
    }
    public void setFiller(MoviesFiller filler) {
        this.filler = filler;
    }
    public void setSaver(MoviesSaver saver) {
        this.saver = saver;
    }
    public void setSortingStrategy(SortingStrategy sortStrategy) {
        if (sorter == null) {
            Comparator<Movie> comp = Comparator.comparing(Movie::hashCode);
            sorter = new MoviesSorter(sortStrategy, comp);
        }
        else
            sorter.setSortingStrategy(sortStrategy);
    }
    public void setComparator(Comparator<Movie> comp) {
        if (sorter == null) {
            SortingStrategy sortStrategy = (moviesList, comparator) -> ((List<Movie>)moviesList).sort(comparator);
            sorter = new MoviesSorter(sortStrategy, comp);
        }
        else
            sorter.setComparator(comp);
    }
    public String getFilepath(String prompt) {
        Optional<String> validatedFilepath;
        do {
            System.out.print(prompt);
            validatedFilepath = InputValidation.validateInput(scanner.nextLine());
        } while (validatedFilepath.isEmpty());
        return validatedFilepath.get();
    }
    public int getSize(String prompt) {
        Optional<Integer> validatedSize;
        do {
            System.out.print(prompt);
            validatedSize = InputValidation.validateIntegerInput(scanner.nextLine());
        } while (validatedSize.isEmpty());
        return validatedSize.get();
    }
    public boolean moviesIsEmpty() {
        return movies.isEmpty();
    }
    public void printMovies(String successMessage) {
        movies.forEach(System.out::println);
        System.out.println(successMessage);
    }
    public void saveMovies(String successMessage) {
        try {
            saveMenu.chooseOption(scanner).execute();
            saver.save(movies);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
        System.out.println(successMessage);
    }
    public void fillMovies(String successMessage) {
        while (true) {
            try {
                fillMenu.chooseOption(scanner).execute();
                filler.fillMovies(movies);
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                continue;
            }
            System.out.println(successMessage);
            break;
        }
    }
    public void sortMovies(String successMessage) {
        sortMenu.chooseOption(scanner).execute();
        compMenu.chooseOption(scanner).execute();
        sorter.performSorting(movies);
        System.out.println(successMessage);
    }
    public void exit() {
        running = false;
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
        private void execute() {
            command.execute();
        }
    }
    public static class SubMenu {
        private final String title;
        private final String prompt;
        private final List<MenuOption> options;
        
        public SubMenu(String title, String prompt, List<MenuOption> options) {
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
        private MenuOption chooseOption(Scanner scanner) {
            if (options.size() == 1)
                return options.get(0);
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
    }
    public static class Builder {
        public Builder withMainMenu(SubMenu menu) {
            Menu.getInstance().mainMenu = menu;
            return this;
        }
        public Builder withFillMenu(SubMenu menu) {
            Menu.getInstance().fillMenu = menu;
            return this;
        }
        public Builder withSortMenu(SubMenu menu) {
            Menu.getInstance().sortMenu = menu;
            return this;
        }
        public Builder withCompMenu(SubMenu menu) {
            Menu.getInstance().compMenu = menu;
            return this;
        }
        public Builder withSaveMenu(SubMenu menu) {
            Menu.getInstance().saveMenu = menu;
            return this;
        }
        public Menu build() {
            return Menu.getInstance();
        }
    }
}
