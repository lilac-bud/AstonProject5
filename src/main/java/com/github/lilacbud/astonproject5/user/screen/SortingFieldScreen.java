package com.github.lilacbud.astonproject5.user.screen;

import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.movie.sort.MoviesSorter;
import com.github.lilacbud.astonproject5.movie.sort.SortingStrategy;
import com.github.lilacbud.astonproject5.user.Menu;
import com.github.lilacbud.astonproject5.user.UserExitException;
import com.github.lilacbud.astonproject5.user.ui.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.IntStream;

public class SortingFieldScreen implements UIScreen {
    private static NamedFieldComparator[] comparators = new NamedFieldComparator[]{
        new NamedFieldComparator("Название", Movie.compareByName),
        new NamedFieldComparator("Год выхода", Movie.compareByYearOfRelease),
        new NamedFieldComparator("Продолжительность", Movie.compareHourLength),
    };

    final private UIMenu<UIScreen> menu = new SelectMenu<>(
        "Поле для сортировки:",
        IntStream
            .range(0, comparators.length)
            .mapToObj(index -> {
                var entry = comparators[index];
                var ch = String.valueOf(index + 1).charAt(0);
                return new SelectMenuItem<>(ch, entry.name, entry.value, (comp) -> onInput(comp));
            }).toList()
    );

    @Override
    public UIScreen show(Scanner scanner) throws UserExitException {
        return menu.prompt(scanner);
    }

    private UIScreen onInput(Comparator<Movie> comparator) {
        var sorter = new MoviesSorter(new SortingStrategy() {
            @Override
            public void sort(Collection<Movie> movies, Comparator<Movie> comp) {
                // TODO: заменить на реальный класс
            }
        }, comparator);

        var menu = Menu.getInstance();
        menu.setMoviesSorter(sorter);

        return new SortingOrderScreen(comparator);
    }

    private static final class NamedFieldComparator {
        private final String name;
        private final Comparator<Movie> value;

        public NamedFieldComparator(String name, Comparator<Movie> value) {
            this.name = name;
            this.value = value;
        }
    }
}
