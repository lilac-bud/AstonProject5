package com.github.lilacbud.astonproject5.user.ui;

import com.github.lilacbud.astonproject5.user.UserExitException;

import java.util.*;

import static com.github.lilacbud.astonproject5.user.ui.PromptHelpers.promptUserSelect;

public class SelectMenu<R> implements UIMenu<R> {
    final String title;
    final List<UIMenuItemOption<Void, R>> items = new ArrayList<>();

    public SelectMenu(String title, UIMenuItemOption<Void, R>... options) {
        this(title, Arrays.asList(options));
    }

    public SelectMenu(String title, List<? extends UIMenuItemOption<Void, R>> options) {
        this.title = title;
        items.clear();
        items.addAll(options);
    }

    @Override
    public R prompt(Scanner scanner) throws UserExitException {
        if (items.isEmpty()) {
            System.out.println("Нет элементов для выбора");
            return null;
        }

        var selected = promptUserSelect(scanner, title, items);

        if (Objects.nonNull(selected)) {
            var action = selected.getAction();

            if (Objects.nonNull(action)) {
                return action.execute();
            }
        }

        return null;
    }
}
