package com.github.lilacbud.astonproject5.user.ui;

import java.util.*;

import static com.github.lilacbud.astonproject5.user.ui.PromptHelpers.promptUserSelect;

public class SelectMenu implements UIMenu {
    final String title;
    final List<UIMenuItemOption> items = new ArrayList<>();

    public SelectMenu(String title, UIMenuItemOption... options) {
        this(title, Arrays.asList(options));
    }

    public SelectMenu(String title, List<? extends UIMenuItemOption> options) {
        this.title = title;
        items.clear();
        items.addAll(options);
    }

    @Override
    public UIScreen prompt(Scanner scanner) {
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
