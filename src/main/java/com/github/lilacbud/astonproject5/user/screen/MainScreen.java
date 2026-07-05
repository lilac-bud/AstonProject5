package com.github.lilacbud.astonproject5.user.screen;

import com.github.lilacbud.astonproject5.user.Menu;
import com.github.lilacbud.astonproject5.user.UserExitException;
import com.github.lilacbud.astonproject5.user.ui.SelectMenu;
import com.github.lilacbud.astonproject5.user.ui.SelectMenuItem;
import com.github.lilacbud.astonproject5.user.ui.UIMenu;
import com.github.lilacbud.astonproject5.user.ui.UIScreen;

import java.util.Scanner;

public class MainScreen implements UIScreen {
    final private UIMenu<UIScreen> menu = new SelectMenu<>(
        "Выберите источник данных:",
        new SelectMenuItem<Void, UIScreen>('1', "Из файла", (e) -> onFileInputStrategy()),
        new SelectMenuItem<Void, UIScreen>('2', "Ввести вручную", (e) -> onManualInputStrategy()),
        new SelectMenuItem<Void, UIScreen>('3', "Случайные данные", (e) -> onRandomInputStrategy()),
        new SelectMenuItem<Void, UIScreen>('Q', "Выход", (e) -> onExit())
    );

    @Override
    public UIScreen show(Scanner scanner) throws UserExitException {
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

    private UIScreen onExit() throws UserExitException {
        throw Menu.exitException;
    }
}
