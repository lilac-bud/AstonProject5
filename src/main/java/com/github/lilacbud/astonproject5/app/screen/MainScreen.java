package com.github.lilacbud.astonproject5.app.screen;

import com.github.lilacbud.astonproject5.ui.SelectMenu;
import com.github.lilacbud.astonproject5.ui.SelectMenuItem;
import com.github.lilacbud.astonproject5.ui.UIMenu;
import com.github.lilacbud.astonproject5.ui.UIScreen;

public class MainScreen implements UIScreen {
    final private UIMenu menu = new SelectMenu(
        "Выберите источник данных:",
        new SelectMenuItem('1', "Из файла", (e) -> onFileInputStrategy()),
        new SelectMenuItem('2', "Ввести вручную", (e) -> onManualInputStrategy()),
        new SelectMenuItem('3', "Случайные данные", (e) -> onRandomInputStrategy()),
        new SelectMenuItem('Q', "Выход", (e) -> onExit())
    );

    @Override
    public UIMenu getMenu() {
        return menu;
    }

    private UIScreen onFileInputStrategy() {
        return new FileStrategyScreen();
    }

    private UIScreen onManualInputStrategy() {
        return new ManualStrategyScreen();
    }

    private UIScreen onRandomInputStrategy() {
        return new RandomStrategyScreen();
    }

    private UIScreen onExit() {
        return null;
    }
}
