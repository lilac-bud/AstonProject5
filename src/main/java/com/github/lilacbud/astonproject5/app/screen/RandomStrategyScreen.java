package com.github.lilacbud.astonproject5.app.screen;

import com.github.lilacbud.astonproject5.app.App;
import com.github.lilacbud.astonproject5.ui.InputMenu;
import com.github.lilacbud.astonproject5.ui.InputMenuItem;
import com.github.lilacbud.astonproject5.ui.UIMenu;
import com.github.lilacbud.astonproject5.ui.UIScreen;

public class RandomStrategyScreen implements UIScreen {
    final private UIMenu menu = new InputMenu(
        "Количество элементов:",
        new InputMenuItem((str) -> onInput(str))
    );

    @Override
    public UIMenu getMenu() {
        return menu;
    }

    private UIScreen onInput(String entityNumberStr) {
        // TODO: Распаристь число, проверить
        // В случае ошибки вернуть тот же экран

        var entityNumber = Integer.parseInt(entityNumberStr);

        var app = App.getInstance();

        // TODO: задать стратегию
        // var strategy = new RandomInputStrategy(entityNumber);
        app.useFillStrategy(null);

        try {
            app.fillData();
        } catch (Throwable e) {
            // TODO: В случае ошибки вернуть тот же экран
            System.out.println(e);
            return this;
        }

        return new ActionsScreen();
    }
}
