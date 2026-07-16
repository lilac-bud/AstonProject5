package com.github.lilacbud.astonproject5;

import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.movie.MoviesFiller;
import com.github.lilacbud.astonproject5.movie.save.MoviesSaver;
import com.github.lilacbud.astonproject5.movie.sort.MoviesSorter;
import com.github.lilacbud.astonproject5.movie.sort.SortingStrategy;
import com.github.lilacbud.astonproject5.user.MenuCommand;
import com.github.lilacbud.astonproject5.util.InputRequest;
import com.github.lilacbud.astonproject5.util.MovieCounter;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class App {
    private boolean running = true;
    private final List<Movie> movies = new ArrayList<>();
    private MoviesFiller filler;
    private final MoviesSorter sorter;
    private MoviesSaver saver;
    
    public App(MoviesSorter sorter) {
        this.sorter = sorter;
    }
    public App() {
        this(new MoviesSorter(null, null));
    }
    
    public void run(MenuCommand<App> command) {
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
    
    public void countMovie(String target, String successFormat) {
        int count = MovieCounter.countInsert(movies, target);
        try {
            System.out.println(String.format(successFormat,target,count));
        } catch (NullPointerException | IllegalFormatException e){
            System.out.println(count);
        }
    }

    public void tryCommandTillSuccess(String successMessage, MenuCommand<App> command) {
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
    public void tryCommandTillSuccess(MenuCommand<App> command) {
        tryCommandTillSuccess(null, command);
    }
}
