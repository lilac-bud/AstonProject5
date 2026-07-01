package com.github.lilacbud.astonproject5.app.screen;

import com.github.lilacbud.astonproject5.ui.SelectMenu;
import com.github.lilacbud.astonproject5.ui.SelectMenuItem;
import com.github.lilacbud.astonproject5.ui.UIMenu;
import com.github.lilacbud.astonproject5.ui.UIScreen;

public class ActionsScreen implements UIScreen {
    final private UIMenu menu = new SelectMenu(
        "Действие:",
        new SelectMenuItem('1', "Показать список", (e) -> onDisplayList()),
        new SelectMenuItem('2', "Отсортировать", (e) -> onSortList()),
        new SelectMenuItem('3', "Поиск", (e) -> onSearch()),
        new SelectMenuItem('4', "Сохранить в файл", (e) -> onSave()),
        new SelectMenuItem('5', "Начать заново", (e) -> onGoToMain()),
        new SelectMenuItem('Q', "Выход", (e) -> onExit())
    );

    @Override
    public UIMenu getMenu() {
        return menu;
    }

    private UIScreen onDisplayList() {
        return this;
    }

    private UIScreen onSortList() {
        return this;
    }

    private UIScreen onSearch() {
        return this;
    }

    private UIScreen onSave() {
        return this;
    }

    private UIScreen onGoToMain() {
        return new MainScreen();
    }

    private UIScreen onExit() {
        return null;
    }
}
