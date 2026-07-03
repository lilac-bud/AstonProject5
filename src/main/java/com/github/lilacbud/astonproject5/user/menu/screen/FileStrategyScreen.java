package com.github.lilacbud.astonproject5.user.menu.screen;

import com.github.lilacbud.astonproject5.movie.FromFileFiller;
import com.github.lilacbud.astonproject5.user.menu.Menu;
import com.github.lilacbud.astonproject5.user.ui.InputMenu;
import com.github.lilacbud.astonproject5.user.ui.InputMenuItem;
import com.github.lilacbud.astonproject5.user.ui.UIMenu;
import com.github.lilacbud.astonproject5.user.ui.UIScreen;

import java.util.Scanner;

public class FileStrategyScreen implements UIScreen {
    final private UIMenu menu = new InputMenu(
        "Путь к файлу:",
        new InputMenuItem((str) -> onInput(str))
    );

    @Override
    public UIScreen show(Scanner scanner) {
        return menu.prompt(scanner);
    }

    private UIScreen onInput(String filePath) {
        var menu = Menu.getInstance();
        var filler = new FromFileFiller(filePath);
        menu.setMoviesFiller(filler);

        return new FillerScreen(this);
    }
}
