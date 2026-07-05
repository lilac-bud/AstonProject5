package com.github.lilacbud.astonproject5.user.ui;

import com.github.lilacbud.astonproject5.user.UserExitException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static com.github.lilacbud.astonproject5.user.ui.PromptHelpers.promptUserSelect;

public class SelectMenu<T, R> implements UIMenu<R> {
    final String title;
    final List<UIMenuItemOption<T, R>> items;

    public SelectMenu(String title, UIMenuItemOption<T, R>... options) {
        this(title, Arrays.asList(options));
    }

    public SelectMenu(String title, List<UIMenuItemOption<T, R>> options) {
        this.title = title;
        this.items = List.copyOf(options);
    }

    @Override
    public R prompt(Scanner scanner) throws UserExitException {
        if (items.isEmpty()) {
            System.out.println("Нет элементов для выбора");
            return null;
        }

        var selected = promptUserSelect(scanner, title, items);

        if (Objects.isNull(selected)) {
            throw new UserExitException(true);
        }

        var value = selected.getValue();
        return selected.executeAction(value);
    }
}
