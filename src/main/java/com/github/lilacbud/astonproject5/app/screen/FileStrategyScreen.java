package com.github.lilacbud.astonproject5.app.screen;

import com.github.lilacbud.astonproject5.app.App;
import com.github.lilacbud.astonproject5.ui.InputMenu;
import com.github.lilacbud.astonproject5.ui.InputMenuItem;
import com.github.lilacbud.astonproject5.ui.UIMenu;
import com.github.lilacbud.astonproject5.ui.UIScreen;

public class FileStrategyScreen implements UIScreen {
    final private UIMenu menu = new InputMenu(
        "Путь к файлу:",
        new InputMenuItem((str) -> onInput(str))
    );

    @Override
    public UIMenu getMenu() {
        return menu;
    }

    private UIScreen onInput(String filePath) {
        // TODO: Проверить существует ли файл
        // В случае ошибки вернуть тот же экран

        var app = App.getInstance();

        // TODO: задать стратегию
        // var strategy = new FileInputStrategy(filePath);
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
