package com.github.lilacbud.astonproject5.user.screen;

import com.github.lilacbud.astonproject5.user.Menu;
import com.github.lilacbud.astonproject5.user.UserExitException;
import com.github.lilacbud.astonproject5.user.ui.SelectMenu;
import com.github.lilacbud.astonproject5.user.ui.SelectMenuItem;
import com.github.lilacbud.astonproject5.user.ui.UIMenu;
import com.github.lilacbud.astonproject5.user.ui.UIScreen;

import java.util.Scanner;

public class ActionsScreen implements UIScreen {
    final private UIMenu<UIScreen> menu = new SelectMenu<>(
        "Действие:",
        new SelectMenuItem<>('1', "Показать список", (e) -> onDisplayList()),
        new SelectMenuItem<>('2', "Отсортировать", (e) -> onSortList()),
        new SelectMenuItem<>('3', "Поиск", (e) -> onSearch()),
        new SelectMenuItem<>('4', "Сохранить в файл", (e) -> onSave()),
        new SelectMenuItem<>('5', "На главный экран", (e) -> onGoToMain()),
        new SelectMenuItem<>('Q', "Выход", (e) -> onExit())
    );

    @Override
    public UIScreen show(Scanner scanner) throws UserExitException {
        return menu.prompt(scanner);
    }

    private UIScreen onDisplayList() {
        return new MoviesListScreen();
    }

    private UIScreen onSortList() {
        return new SortingFieldScreen();
    }

    private UIScreen onSearch() {
        return this;
    }

    private UIScreen onSave() {
        return new SaveToFileScreen(this);
    }

    private UIScreen onGoToMain() {
        return new MainScreen();
    }

    private UIScreen onExit() throws UserExitException {
        throw Menu.exitException;
    }
}
