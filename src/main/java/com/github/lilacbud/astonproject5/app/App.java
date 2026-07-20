package com.github.lilacbud.astonproject5.app;

import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.util.MovieCounter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IllegalFormatException;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.function.Consumer;

public class App {
    public static final String FILLER_NULL_MESSAGE = "Filler must not be null";
    public static final String SAVER_NULL_MESSAGE = "Saver must not be null";
    public static final String SORTSTRAT_NULL_MESSAGE = "Sorting strategy must not be null";
    public static final String COMP_NULL_MESSAGE = "Comparator must not be null";
    public static final String COMMAND_NULL_MESSAGE = "Command must not be null";
    
    private final List<Movie> movies = new ArrayList<>();
    
    private final MoviesSorter sorter = new MoviesSorter();
    private MoviesFiller filler;
    private MoviesSaver saver;
    
    private boolean running = true;
    
    public void run(Consumer<App> command) {
        while (running) {
            command.accept(this);
        }
    }
    
    public void setFiller(MoviesFiller filler) {
        this.filler = requireNonNull(filler, FILLER_NULL_MESSAGE);
    }
    
    public void setSaver(MoviesSaver saver) {
        this.saver = requireNonNull(saver, SAVER_NULL_MESSAGE);
    }
    
    public void setSortingStrategy(SortingStrategy<Movie> sortStrategy) {
        sorter.setSortingStrategy(requireNonNull(sortStrategy, SORTSTRAT_NULL_MESSAGE));
    }
    
    public void setComparator(Comparator<Movie> comp) {
        sorter.setComparator(requireNonNull(comp, COMP_NULL_MESSAGE));
    }
    
    public boolean moviesIsEmpty() {
        return movies.isEmpty();
    }
    
    public void printMovies(String printFormat, String successMessage) {
        try {
            movies.forEach(movie -> System.out.println(String.format(printFormat, 
                    movie.getName(), 
                    movie.getYearOfRelease(), 
                    movie.getHourLength())));
        } catch (NullPointerException | IllegalFormatException e) {
            movies.forEach(System.out::println);
        }
        if (successMessage != null) {
            System.out.println(successMessage);
        }
    }
    
    public void printMovies(String printFormat) {
        printMovies(printFormat, null);
    }
    
    public void printMovies() {
        printMovies(null, null);
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
            System.out.println(String.format(successFormat, target, count));
        } catch (NullPointerException | IllegalFormatException e){
            System.out.println(count);
        }
    }
    
    public void countMovie(String target) {
        countMovie(target, null);
    }

    public void tryCommandTillSuccess(String successMessage, Consumer<App> command) {
        requireNonNull(command, COMMAND_NULL_MESSAGE);
        while (true) {
            try {
                command.accept(this);
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                continue;
            }
            if (successMessage != null) {
                System.out.println(successMessage);
            }
            break;
        }
    }
    
    public void tryCommandTillSuccess(Consumer<App> command) {
        tryCommandTillSuccess(null, command);
    }
    
    private static class MoviesSorter {
        public static final String SORTER_SORTSTRAT_NULL_MESSAGE = 
                "Sorting strategy must not be null to perform sorting";
        public static final String SORTER_COMP_NULL_MESSAGE = 
                "Comparator must not be null to perform sorting";
        
        private SortingStrategy<Movie> sortStrategy;
        private Comparator<Movie> comp;

        public void setSortingStrategy(SortingStrategy<Movie> sortStrategy) {
            this.sortStrategy = sortStrategy;
        }

        public void setComparator(Comparator<Movie> comp) {
            this.comp = comp;
        }

        public void performSorting(List<Movie> movies) {
            if (sortStrategy == null) {
                throw new IllegalStateException(SORTER_SORTSTRAT_NULL_MESSAGE);
            }
            if (comp == null) {
                throw new IllegalStateException(SORTER_COMP_NULL_MESSAGE);
            }
            sortStrategy.sort(movies, comp);
        }
    }
}
