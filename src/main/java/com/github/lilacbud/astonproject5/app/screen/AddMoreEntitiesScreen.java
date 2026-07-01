package com.github.lilacbud.astonproject5.app.screen;

import com.github.lilacbud.astonproject5.ui.SelectMenu;
import com.github.lilacbud.astonproject5.ui.SelectMenuItem;
import com.github.lilacbud.astonproject5.ui.UIMenu;
import com.github.lilacbud.astonproject5.ui.UIScreen;

public class AddMoreEntitiesScreen implements UIScreen {
    private final UIScreen returnScreen;

    final private UIMenu menu = new SelectMenu(
        "Добавить еще?",
        new SelectMenuItem('Y', "Да", (e) -> onAddMore()),
        new SelectMenuItem('N', "Завершить", (e) -> onComplete()),
        new SelectMenuItem('Q', "Выход", (e) -> onExit())
    );

    public AddMoreEntitiesScreen(UIScreen returnScreen) {
        this.returnScreen = returnScreen;
    }

    @Override
    public UIMenu getMenu() {
        return menu;
    }

    private UIScreen onAddMore() {
        return returnScreen;
    }

    private UIScreen onComplete() {
        return new ActionsScreen();
    }

    private UIScreen onExit() {
        return null;
    }
}
