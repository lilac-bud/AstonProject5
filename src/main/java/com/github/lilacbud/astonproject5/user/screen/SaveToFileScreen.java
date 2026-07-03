package com.github.lilacbud.astonproject5.user.screen;

import com.github.lilacbud.astonproject5.movie.save.DefaultSaver;
import com.github.lilacbud.astonproject5.user.Menu;
import com.github.lilacbud.astonproject5.user.ui.InputMenu;
import com.github.lilacbud.astonproject5.user.ui.InputMenuItem;
import com.github.lilacbud.astonproject5.user.ui.UIMenu;
import com.github.lilacbud.astonproject5.user.ui.UIScreen;

import java.util.Scanner;

// TODO: Поделить на сохранение из меню
// и сохранение с подтверждение при выходе
public class SaveToFileScreen implements UIScreen {
    final private UIMenu menu = new InputMenu(
        "Путь к файлу:",
        new InputMenuItem((str) -> onInput(str))
    );

    private final UIScreen backScreen;

    public SaveToFileScreen(UIScreen backScreen) {
        this.backScreen = backScreen;
    }

    @Override
    public UIScreen show(Scanner scanner) {
        return menu.prompt(scanner);
    }

    private UIScreen onInput(String filePath) {
        var menu = Menu.getInstance();
        var saver = new DefaultSaver(filePath);

        saver.save(menu.getMovies());

        return backScreen;
    }
}
