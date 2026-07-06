package com.github.lilacbud.astonproject5.ui;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static com.github.lilacbud.astonproject5.ui.PromptHelpers.promptUserSelect;

public class SelectMenu<T, R> implements UIMenu<R> {
    final String title;
    final List<UIMenuItemOption<T, R>> items;

    @SafeVarargs
    public SelectMenu(String title, UIMenuItemOption<T, R>... options) {
        this(title, List.of(options));
    }

    public SelectMenu(String title, List<? extends UIMenuItemOption<T, R>> options) {
        this.title = title;
        this.items = List.copyOf(options);
    }

    @Override
    public R prompt(Scanner scanner) throws ExitException {
        if (items.isEmpty()) {
            System.out.println("Нет элементов для выбора");
            return null;
        }

        var selected = promptUserSelect(scanner, title, items);

        if (Objects.isNull(selected)) {
            throw new ExitException(true);
        }

        var value = selected.getValue();
        return selected.executeAction(value);
    }
}
