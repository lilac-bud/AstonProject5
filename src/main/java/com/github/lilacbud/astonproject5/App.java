package com.github.lilacbud.astonproject5;

import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.movie.MoviesFiller;
import com.github.lilacbud.astonproject5.movie.save.MoviesSaver;
import com.github.lilacbud.astonproject5.movie.sort.MoviesSorter;
import com.github.lilacbud.astonproject5.movie.sort.SortingStrategy;
import com.github.lilacbud.astonproject5.user.Menu;
import com.github.lilacbud.astonproject5.util.InputValidation;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {
    private static App INSTANCE;
    private boolean running = true;
    private final List<Movie> movies = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private MoviesFiller filler;
    private MoviesSorter sorter;
    private MoviesSaver saver;
    
    private final Menu mainMenu, fillMenu, sortMenu, compMenu, saveMenu;
    
    private App(StepBuilder builder) {
        this.mainMenu = builder.mainMenu;
        this.fillMenu = builder.fillMenu;
        this.sortMenu = builder.sortMenu;
        this.compMenu = builder.compMenu;
        this.saveMenu = builder.saveMenu;
    }
    
    public static App getInstance() {
        if (INSTANCE == null)
            throw new IllegalStateException("App instance has to be built first");
        return INSTANCE;
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
    public boolean moviesIsEmpty() {
        return movies.isEmpty();
    }
    public String askFilepath(String prompt) {
        Optional<String> validatedFilepath;
        do {
            System.out.print(prompt);
            validatedFilepath = InputValidation.validateInput(scanner.nextLine());
        } while (validatedFilepath.isEmpty());
        return validatedFilepath.get();
    }
    public int askSize(String prompt) {
        Optional<Integer> validatedSize;
        do {
            System.out.print(prompt);
            validatedSize = InputValidation.validateIntegerInput(scanner.nextLine());
        } while (validatedSize.isEmpty());
        return validatedSize.get();
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
    
    public static interface MainMenuBuilder {
        public FillMenuBuilder withMainMenu(Menu menu);
    }
    public static interface FillMenuBuilder {
        public SortMenuBuilder withFillMenu(Menu menu);
    }
    public static interface SortMenuBuilder {
        public CompMenuBuilder withSortMenu(Menu menu);
    }
    public static interface CompMenuBuilder {
        public SaveMenuBuilder withCompMenu(Menu menu);
    }
    public static interface SaveMenuBuilder {
        public StepBuilder withSaveMenu(Menu menu);
    }
    public static class StepBuilder implements MainMenuBuilder, FillMenuBuilder, 
            SortMenuBuilder, CompMenuBuilder, SaveMenuBuilder{
        private Menu mainMenu, fillMenu, sortMenu, compMenu, saveMenu;
        
        private StepBuilder() {}
        
        public static MainMenuBuilder newBuilder() {
            return new StepBuilder();
        }
        @Override
        public FillMenuBuilder withMainMenu(Menu menu) {
            validateMenu(menu);
            this.mainMenu = menu;
            return this;
        }
        @Override
        public SortMenuBuilder withFillMenu(Menu menu) {
            validateMenu(menu);
            this.fillMenu = menu;
            return this;
        }
        @Override
        public CompMenuBuilder withSortMenu(Menu menu) {
            validateMenu(menu);
            this.sortMenu = menu;
            return this;
        }
        @Override
        public SaveMenuBuilder withCompMenu(Menu menu) {
            validateMenu(menu);
            this.compMenu = menu;
            return this;
        }
        @Override
        public StepBuilder withSaveMenu(Menu menu) {
            validateMenu(menu);
            this.saveMenu = menu;
            return this;
        }
        public App build() {
            if (INSTANCE != null)
                throw new IllegalStateException("App instance can be built only once");
            INSTANCE = new App(this);
            return INSTANCE;
        }
        private void validateMenu(Menu menu) {
            if (menu == null)
                throw new IllegalArgumentException("Menu cannot be null");
        }
    }
}
