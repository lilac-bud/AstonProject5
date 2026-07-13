package com.github.lilacbud.astonproject5.user;

import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.movie.MoviesFiller;
import com.github.lilacbud.astonproject5.movie.save.*;
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
    private final SortingStrategy sortStrategy = new MergeSort();
    
    private SubMenu mainMenu;
    private SubMenu fillMenu;
    private SubMenu compMenu;
    private final SubMenu changeCompMenu = new SubMenu("Поменять?",
            List.of(
                    new MenuOption("Да", () -> Menu.getInstance().chooseComparator()),
                    new MenuOption("Нет", () -> {})
            )
    );
    
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
    
    public void setFiller(MoviesFiller filler) {
        this.filler = filler;
    }
    public void setComparator(Comparator<Movie> comp) {
        if (sorter == null)
            sorter = new MoviesSorter(sortStrategy, comp);
        else
            sorter.setComparator(comp);
    }
    public String getFilepath() {
        Optional<String> validatedFilepath;
        do {
            System.out.print("Укажите путь к файлу: ");
            validatedFilepath = InputValidation.validateInput(scanner.nextLine());
        } while (validatedFilepath.isEmpty());
        return validatedFilepath.get();
    }
    public int getSize() {
        Optional<Integer> validatedSize;
        do {
            System.out.print("Укажите количество фильмов: ");
            validatedSize = InputValidation.validateIntegerInput(scanner.nextLine());
        } while (validatedSize.isEmpty());
        return validatedSize.get();
    }
    public void printMovies() {
        movies.forEach(System.out::println);
    }
    public void saveMovies() {
        try {
            new DefaultSaver(getFilepath(), scanner).save(movies);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
    public void fillMovies() {
        while (true) {
            try {
                fillMenu.chooseOption(scanner).execute();
                filler.fillMovies(movies);
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                continue;
            }
            System.out.println("Список успешно заполнен");
            break;
        }
    }
    public void checkIfMoviesEmpty() {
        if (movies.isEmpty())
            fillMovies();
    }
    public void sortMovies() {
        if (sorter == null)
            chooseComparator();
        else
            changeCompMenu.chooseOption(scanner).execute();
        sorter.performSorting(movies);
        System.out.println("Список успешно отсортирован");
    }
    public void chooseComparator() {
        MenuOption compOption = compMenu.chooseOption(scanner);
        String title = String.format("Сейчас фильмы сортируются %s. Поменять?", compOption.getTitle().toLowerCase());
        changeCompMenu.setTitle(title);
        compOption.execute();
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
        private String title;
        private final List<MenuOption> options;
        
        public SubMenu(String title, List<MenuOption> options) {
            if (title == null)
                throw new IllegalArgumentException("Title cannot be null");
            if (options == null)
                throw new IllegalArgumentException("Options cannot be null");
            if (options.isEmpty())
                throw new IllegalArgumentException("Options cannot be empty");
            this.title = title;
            this.options = options;
        }
        private void setTitle(String title) {
            this.title = title;
        }
        private MenuOption chooseOption(Scanner scanner) {
            Optional<Integer> validatedInput;
            while (true) {
                do {
                    System.out.println(title);
                    IntStream.range(0, options.size())
                            .mapToObj(i -> String.format("%d. %s", i + 1, options.get(i).getTitle()))
                            .forEach(System.out::println);
                    System.out.print("Выберите одну из опций: ");
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
        public Builder withCompMenu(SubMenu menu) {
            Menu.getInstance().compMenu = menu;
            return this;
        }
        public Menu build() {
            return Menu.getInstance();
        }
    }
}
