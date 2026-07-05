package com.github.lilacbud.astonproject5.user.screen;

import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.user.Menu;
import com.github.lilacbud.astonproject5.user.UserExitException;
import com.github.lilacbud.astonproject5.user.ui.SelectMenu;
import com.github.lilacbud.astonproject5.user.ui.SelectMenuItem;
import com.github.lilacbud.astonproject5.user.ui.UIMenu;
import com.github.lilacbud.astonproject5.user.ui.UIScreen;

import java.util.Comparator;
import java.util.Objects;
import java.util.Scanner;

public final class SortingOrderScreen implements UIScreen {
    final private Comparator<Movie> comparator;

    final private UIMenu<UIScreen> menu = new SelectMenu<>(
        "Порядок сортировки:",
        new SelectMenuItem<SortOrder, UIScreen>('1', "По возрастанию", SortOrder.ASC, (e) -> onInput(e)),
        new SelectMenuItem<SortOrder, UIScreen>('2', "По убыванию", SortOrder.ASC, (e) -> onInput(e))
    );

    public SortingOrderScreen(Comparator<Movie> comparator) {
        this.comparator = comparator;
    }

    @Override
    public UIScreen show(Scanner scanner) throws UserExitException {
        return menu.prompt(scanner);
    }

    private UIScreen onInput(SortOrder order) {
        var menu = Menu.getInstance();
        var sorter = menu.getMoviesSorter();

        if (Objects.isNull(sorter) || Objects.isNull(comparator)) {
            return new ActionsScreen();
        }

        if (order == SortOrder.DESC) {
            sorter.setComparator(comparator.reversed());
        }

        sorter.performSorting(menu.getMovies());

        return new ActionsScreen();
    }

    private enum SortOrder {
        ASC,
        DESC;
    }
}
