package com.github.lilacbud.astonproject5;

import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.movie.MoviesFiller;
import com.github.lilacbud.astonproject5.movie.save.MoviesSaver;
import com.github.lilacbud.astonproject5.movie.sort.MoviesSorter;
import com.github.lilacbud.astonproject5.movie.sort.SortingStrategy;
import com.github.lilacbud.astonproject5.user.Menu;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IllegalFormatException;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.Scanner;

public class App {
    private boolean running = true;
    private final List<Movie> movies = new ArrayList<>();
    private MoviesFiller filler;
    private final MoviesSorter sorter;
    private MoviesSaver saver;
    
    private App(StepBuilder builder) {
        this.sorter = builder.sorter;
    }
    public App(MoviesSorter sorter) {
        this.sorter = sorter;
    }
    public App() {
        this(new MoviesSorter(null, null));
    }
    
    public void run(Menu.MenuCommand<App> command) {
        while (running)
            command.execute(this);
    }
    public void setFiller(MoviesFiller filler) {
        this.filler = requireNonNull(filler, "Filler cannot be null");
    }
    public void setSaver(MoviesSaver saver) {
        this.saver = requireNonNull(saver, "Saver cannot be null");
    }
    public void setSortingStrategy(SortingStrategy sortStrategy) {
        sorter.setSortingStrategy(requireNonNull(sortStrategy, "Sorting strategy cannot be null"));
    }
    public void setComparator(Comparator<Movie> comp) {
        sorter.setComparator(requireNonNull(comp, "Comparator cannot be null"));
    }
    public boolean moviesIsEmpty() {
        return movies.isEmpty();
    }
    public void printMovies(String successMessage, String printFormat) {
        try {
            movies.forEach(movie -> System.out.println(String.format(printFormat, 
                    movie.getName(), 
                    movie.getYearOfRelease(), 
                    movie.getHourLength())));
        } catch (NullPointerException | IllegalFormatException e) {
            movies.forEach(System.out::println);
        }
        if (successMessage != null)
            System.out.println(successMessage);
    }
    public void saveMovies() {
        saver.save(movies);
    }
    public void fillMovies() {
        filler.fillMovies(movies);
    }
    public void sortMovies() {
        sorter.performSorting(movies);
    }
    public void exit() {
        running = false;
    }
    
    public void tryCommandTillSuccess(String successMessage, Menu.MenuCommand<App> command) {
        requireNonNull(command, "Command cannot be null");
        while (true) {
            try {
                command.execute(this);
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                continue;
            }
            if (successMessage != null)
                System.out.println(successMessage);
            break;
        }
    }
    public void tryCommandTillSuccess(Menu.MenuCommand<App> command) {
        tryCommandTillSuccess(null, command);
    }
    
    public static record Menus(Menu<App> mainMenu, Menu<App> setFillerMenu, 
        Menu<App> setSortMenu, Menu<App> setCompMenu, Menu<App> setSaverMenu) {
        public Menus {
            validateMenu(mainMenu);
            validateMenu(setFillerMenu);
            validateMenu(setSortMenu);
            validateMenu(setCompMenu);
            validateMenu(setSaverMenu);
        }
        private static Menu<App> validateMenu(Menu<App> menu) {
            return requireNonNull(menu, "Menu cannot be null");
        }
    }
    
    public static interface ScannerBuilder {
        public MenusBuilder withScanner(Scanner scanner);
    }
    public static interface MenusBuilder {
        public StepBuilder withMenus(Menus menus);
    }
    public static class StepBuilder implements ScannerBuilder, MenusBuilder {
        private Scanner scanner;
        private Menus menus;
        private MoviesSorter sorter = new MoviesSorter(null, null);
        
        private StepBuilder() {}
        
        public static ScannerBuilder newBuilder() {
            return new StepBuilder();
        }
        @Override
        public MenusBuilder withScanner(Scanner scanner) {
            this.scanner = requireNonNull(scanner, "Scanner must not be null");
            return this;
        }
        @Override
        public StepBuilder withMenus(Menus menus) {
            this.menus = requireNonNull(menus, "Menus must not be null");
            return this;
        }
        public StepBuilder withMoviesSorter(MoviesSorter sorter) {
            this.sorter = requireNonNull(sorter, "Sorter must not be null");
            return this;
        }
        public App build() {
            return new App(this);
        }
    }
}
