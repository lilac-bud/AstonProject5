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
    
    private Menu.SubMenu mainMenu, fillMenu, sortMenu, compMenu, saveMenu;
    
    private App() {}
    
    public static App getInstance() {
        if (INSTANCE == null)
            INSTANCE = new App();
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
    
    public static class Builder {
        public Builder withMainMenu(Menu.SubMenu menu) {
            App.getInstance().mainMenu = menu;
            return this;
        }
        public Builder withFillMenu(Menu.SubMenu menu) {
            App.getInstance().fillMenu = menu;
            return this;
        }
        public Builder withSortMenu(Menu.SubMenu menu) {
            App.getInstance().sortMenu = menu;
            return this;
        }
        public Builder withCompMenu(Menu.SubMenu menu) {
            App.getInstance().compMenu = menu;
            return this;
        }
        public Builder withSaveMenu(Menu.SubMenu menu) {
            App.getInstance().saveMenu = menu;
            return this;
        }
        public App build() {
            return App.getInstance();
        }
    }
}
