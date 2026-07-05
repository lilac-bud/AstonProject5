package com.github.lilacbud.astonproject5.user.screen;

import com.github.lilacbud.astonproject5.movie.save.DefaultSaver;
import com.github.lilacbud.astonproject5.user.Menu;
import com.github.lilacbud.astonproject5.user.UserExitException;
import com.github.lilacbud.astonproject5.user.ui.InputMenu;
import com.github.lilacbud.astonproject5.user.ui.InputMenuItem;
import com.github.lilacbud.astonproject5.user.ui.UIMenu;
import com.github.lilacbud.astonproject5.user.ui.UIScreen;

import java.util.Scanner;

public class SaveToFileScreen implements UIScreen {
    final private UIMenu<UIScreen> menu = new InputMenu<>(
        "Сохранить в файл:",
        new InputMenuItem<>((str) -> onInput(str))
    );

    private final UIScreen backScreen;

    public SaveToFileScreen(UIScreen backScreen) {
        this.backScreen = backScreen;
    }

    @Override
    public UIScreen show(Scanner scanner) throws UserExitException {
        return menu.prompt(scanner);
    }

    private UIScreen onInput(String filePath) {
        var menu = Menu.getInstance();
        var saver = new DefaultSaver(filePath);

        saver.save(menu.getMovies());

        return backScreen;
    }
}
