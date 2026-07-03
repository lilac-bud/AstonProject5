package com.github.lilacbud.astonproject5.user.screen;

import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.movie.sort.MoviesSorter;
import com.github.lilacbud.astonproject5.movie.sort.SortingStrategy;
import com.github.lilacbud.astonproject5.user.Menu;
import com.github.lilacbud.astonproject5.user.ui.SelectMenu;
import com.github.lilacbud.astonproject5.user.ui.SelectMenuItem;
import com.github.lilacbud.astonproject5.user.ui.UIMenu;
import com.github.lilacbud.astonproject5.user.ui.UIScreen;

import java.util.*;
import java.util.stream.IntStream;

public class SortingFieldScreen implements UIScreen {
    private static EntityField[] fields = new EntityField[]{
        new EntityField("name", "Название"),
        new EntityField("yearOfRelease", "Год выхода"),
        new EntityField("hourLength", "Продолжительность"),
    };

    final private UIMenu menu = new SelectMenu(
        "Поле для сортировки:",
        IntStream
            .range(0, fields.length)
            .mapToObj(index -> {
                var field = fields[index];
                var ch = String.valueOf(index + 1).charAt(0);
                return new SelectMenuItem(ch, field.fieldTitle, (e) -> onInput(field));
            }).toList()
    );

    @Override
    public UIScreen show(Scanner scanner) {
        return menu.prompt(scanner);
    }

    private UIScreen onInput(EntityField entityField) {
        var comparator = switch (entityField.field) {
            case "name" -> Movie.compareByName;

            case "yearOfRelease" -> Movie.compareByYearOfRelease;

            case "hourLength" -> Movie.compareHourLength;

            default -> null;
        };

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

    private static final class EntityField {
        private final String field;
        private final String fieldTitle;

        public EntityField(String field, String fieldTitle) {
            this.field = field;
            this.fieldTitle = fieldTitle;
        }
    }
}
