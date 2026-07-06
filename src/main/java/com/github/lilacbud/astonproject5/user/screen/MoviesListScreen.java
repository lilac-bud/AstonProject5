package com.github.lilacbud.astonproject5.user.screen;

import com.github.lilacbud.astonproject5.user.Menu;
import com.github.lilacbud.astonproject5.user.UserExitException;
import com.github.lilacbud.astonproject5.user.ui.SelectMenuItem;
import com.github.lilacbud.astonproject5.user.ui.UIScreen;

import java.time.Duration;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class MoviesListScreen implements UIScreen {
    private final String DIVIDER = "-------------------------------";

    @Override
    public UIScreen show(Scanner scanner) throws UserExitException {
        var menu = Menu.getInstance();
        var movies = menu.getMovies();

        String moviesOutput;

        if (Objects.isNull(movies) || movies.isEmpty()) {
            moviesOutput = "(Список фильмов пуст)\n";
        } else {
            moviesOutput = IntStream
                .range(0, movies.size())
                .mapToObj(index -> {
                    var movie = movies.get(index);
                    var num = index + 1;
                    var name = movie.getName();
                    var year = movie.getYearOfRelease();
                    var duration = formatDuration(movie.getHourLength());
                    return "[%1$d] %2$s (%3$d) - %4$s\n".formatted(num, name, year, duration);
                }).collect(Collectors.joining());
        }

        System.out.println(DIVIDER);
        System.out.print(moviesOutput);
        System.out.println(DIVIDER);

        return new ActionsScreen();
    }

    private static String formatDuration(float hours) {
        var millis = (long) (hours * 3600 * 1000);
        var duration = Duration.ofMillis(millis);

        var h = duration.toHours();
        var m = duration.toMinutesPart();
        var s = duration.toSecondsPart();

        return String.format("%02d:%02d:%02d", h, m, s);
    }
}
