package com.github.lilacbud.astonproject5.user.screen;

import com.github.lilacbud.astonproject5.user.ui.SelectMenu;
import com.github.lilacbud.astonproject5.user.ui.SelectMenuItem;
import com.github.lilacbud.astonproject5.user.ui.UIMenu;
import com.github.lilacbud.astonproject5.user.ui.UIScreen;

import java.util.Scanner;

public class MainScreen implements UIScreen {
    final private UIMenu menu = new SelectMenu(
        "Выберите источник данных:",
        new SelectMenuItem('1', "Из файла", (e) -> onFileInputStrategy()),
        new SelectMenuItem('2', "Ввести вручную", (e) -> onManualInputStrategy()),
        new SelectMenuItem('3', "Случайные данные", (e) -> onRandomInputStrategy()),
        new SelectMenuItem('Q', "Выход", (e) -> onExit())
    );

    @Override
    public UIScreen show(Scanner scanner) {
        return menu.prompt(scanner);
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
