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
import java.util.IllegalFormatException;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.Optional;
import java.util.Scanner;

public class App {
    private boolean running = true;
    private final List<Movie> movies = new ArrayList<>();
    private final Scanner scanner;
    private MoviesFiller filler;
    private final MoviesSorter sorter;
    private MoviesSaver saver;
    
    private final Menu<App> mainMenu, setFillerMenu, setSortMenu, setCompMenu, setSaverMenu;
    
    private App(StepBuilder builder) {
        this.scanner = builder.scanner;
        this.mainMenu = builder.mainMenu;
        this.setFillerMenu = builder.setFillerMenu;
        this.setSortMenu = builder.setSortMenu;
        this.setCompMenu = builder.setCompMenu;
        this.setSaverMenu = builder.setSaverMenu;
        this.sorter = builder.sorter;
    }
    
    public void run(){
        while (running)
            mainMenu.chooseOption(scanner).execute(this);
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
    public String askFilepath(String prompt) {
        Optional<String> validatedFilepath;
        do {
            if (prompt != null)
                System.out.print(prompt);
            validatedFilepath = InputValidation.validateInput(scanner.nextLine());
        } while (validatedFilepath.isEmpty());
        return validatedFilepath.get();
    }
    public int askSize(String prompt) {
        Optional<Integer> validatedSize;
        do {
            if (prompt != null)
                System.out.print(prompt);
            validatedSize = InputValidation.validateIntegerInput(scanner.nextLine());
        } while (validatedSize.isEmpty());
        return validatedSize.get();
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
    public void saveMovies(String successMessage) {
        tryCommandTillSuccess(successMessage, (client) -> {
            client.setSaverMenu.chooseOption(scanner).execute(client);
            client.saver.save(movies);
        });
    }
    public void fillMovies(String successMessage) {
        tryCommandTillSuccess(successMessage, (client) -> {
            client.setFillerMenu.chooseOption(scanner).execute(client);
            client.filler.fillMovies(movies);
        });
    }
    public void sortMovies(String successMessage) {
        tryCommandTillSuccess(successMessage, (client) -> {
            client.setSortMenu.chooseOption(scanner).execute(client);
            client.setCompMenu.chooseOption(scanner).execute(client);
            client.sorter.performSorting(movies);
        });
    }
    public void exit() {
        running = false;
    }
    
    private void tryCommandTillSuccess(String successMessage, Menu.MenuCommand<App> command) {
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
    
    public static interface ScannerBuilder {
        public MainMenuBuilder withScanner(Scanner scanner);
    }
    public static interface MainMenuBuilder {
        public FillMenuBuilder withMainMenu(Menu<App> menu);
    }
    public static interface FillMenuBuilder {
        public SortMenuBuilder withSetFillerMenu(Menu<App> menu);
    }
    public static interface SortMenuBuilder {
        public CompMenuBuilder withSetSortMenu(Menu<App> menu);
    }
    public static interface CompMenuBuilder {
        public SaveMenuBuilder withSetCompMenu(Menu<App> menu);
    }
    public static interface SaveMenuBuilder {
        public StepBuilder withSetSaverMenu(Menu<App> menu);
    }
    public static class StepBuilder implements ScannerBuilder, MainMenuBuilder, FillMenuBuilder, 
            SortMenuBuilder, CompMenuBuilder, SaveMenuBuilder {
        private Scanner scanner;
        private Menu<App> mainMenu, setFillerMenu, setSortMenu, setCompMenu, setSaverMenu;
        private MoviesSorter sorter = new MoviesSorter(null, null);
        
        private StepBuilder() {}
        
        public static ScannerBuilder newBuilder() {
            return new StepBuilder();
        }
        @Override
        public MainMenuBuilder withScanner(Scanner scanner) {
            this.scanner = requireNonNull(scanner, "Scanner cannot be null");
            return this;
        } 
        @Override
        public FillMenuBuilder withMainMenu(Menu<App> menu) {
            this.mainMenu = validateMenu(menu);
            return this;
        }
        @Override
        public SortMenuBuilder withSetFillerMenu(Menu<App> menu) {
            this.setFillerMenu = validateMenu(menu);
            return this;
        }
        @Override
        public CompMenuBuilder withSetSortMenu(Menu<App> menu) {
            this.setSortMenu = validateMenu(menu);
            return this;
        }
        @Override
        public SaveMenuBuilder withSetCompMenu(Menu<App> menu) {
            this.setCompMenu = validateMenu(menu);
            return this;
        }
        @Override
        public StepBuilder withSetSaverMenu(Menu<App> menu) {
            this.setSaverMenu = validateMenu(menu);
            return this;
        }
        public StepBuilder withMoviesSorter(MoviesSorter sorter) {
            this.sorter = requireNonNull(sorter, "Sorter cannot be null");
            return this;
        }
        public App build() {
            return new App(this);
        }
        private Menu<App> validateMenu(Menu<App> menu) {
            return requireNonNull(menu, "Menu cannot be null");
        }
    }
}
