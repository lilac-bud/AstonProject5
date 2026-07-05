package com.github.lilacbud.astonproject5.user;

import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.movie.MoviesFiller;
import com.github.lilacbud.astonproject5.movie.save.MoviesSaver;
import com.github.lilacbud.astonproject5.movie.sort.MoviesSorter;
import com.github.lilacbud.astonproject5.user.screen.MainScreen;
import com.github.lilacbud.astonproject5.user.screen.SaveBeforeExitScreen;
import com.github.lilacbud.astonproject5.user.ui.UIScreen;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Menu {
    public static final UserExitException exitException = new UserExitException();
    private static Menu menu;
    private UIScreen currentScreen;
    private Scanner scanner = new Scanner(System.in);
    private List<Movie> movies;
    private MoviesFiller moviesFiller;
    private MoviesSorter moviesSorter;
    private MoviesSaver moviesSaver;

    private Menu() {
    }

    public static Menu getInstance() {
        if (Objects.isNull(menu)) {
            menu = new Menu();
            menu.currentScreen = new MainScreen();
        }

        return menu;
    }

    public void run() {
        while (Objects.nonNull(menu.currentScreen)) {
            try {
                UIScreen nextScreen = menu.currentScreen.show(scanner);
                menu.currentScreen = nextScreen;
            } catch (UserExitException ex) {
                if (ex.isForceExit()) {
                    return;
                }

                menu.currentScreen = new SaveBeforeExitScreen();
            } catch (Throwable ex) {
                System.out.println(ex);
            }
        }

        exit();
    }

    public void exit() {
        scanner.close();
    }

    public MoviesFiller getMoviesFiller() {
        return moviesFiller;
    }

    public void setMoviesFiller(MoviesFiller filler) {
        moviesFiller = filler;
    }

    public MoviesSorter getMoviesSorter() {
        return moviesSorter;
    }

    public void setMoviesSorter(MoviesSorter moviesSorter) {
        this.moviesSorter = moviesSorter;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public MoviesSaver getMoviesSaver() {
        return moviesSaver;
    }

    public void setMoviesSaver(MoviesSaver moviesSaver) {
        this.moviesSaver = moviesSaver;
    }
}
