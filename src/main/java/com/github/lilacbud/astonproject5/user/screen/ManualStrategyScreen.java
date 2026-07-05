package com.github.lilacbud.astonproject5.user.screen;

import com.github.lilacbud.astonproject5.movie.ManualFiller;
import com.github.lilacbud.astonproject5.user.Menu;
import com.github.lilacbud.astonproject5.user.UserExitException;
import com.github.lilacbud.astonproject5.user.ui.InputMenu;
import com.github.lilacbud.astonproject5.user.ui.InputMenuItem;
import com.github.lilacbud.astonproject5.user.ui.UIMenu;
import com.github.lilacbud.astonproject5.user.ui.UIScreen;

import java.util.Scanner;

public final class ManualStrategyScreen implements UIScreen {
    final private UIMenu<UIScreen> menu = new InputMenu<>(
        "Количество элементов:",
        new InputMenuItem<>((str) -> onInput(str))
    );

    @Override
    public UIScreen show(Scanner scanner) throws UserExitException {
        return menu.prompt(scanner);
    }

    private UIScreen onInput(String entityNumberStr) {
        var menu = Menu.getInstance();

        int entityNumber;

        try {
            entityNumber = Integer.parseInt(entityNumberStr);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка. Введите число.");
            return this;
        }

        var filler = new ManualFiller(entityNumber);
        menu.setMoviesFiller(filler);

        return new FillerScreen(this);
    }
}
