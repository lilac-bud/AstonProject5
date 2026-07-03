package com.github.lilacbud.astonproject5.user.screen;

import com.github.lilacbud.astonproject5.movie.RandomFiller;
import com.github.lilacbud.astonproject5.user.Menu;
import com.github.lilacbud.astonproject5.user.ui.InputMenu;
import com.github.lilacbud.astonproject5.user.ui.InputMenuItem;
import com.github.lilacbud.astonproject5.user.ui.UIMenu;
import com.github.lilacbud.astonproject5.user.ui.UIScreen;

import java.util.Scanner;

public class RandomStrategyScreen implements UIScreen {
    final private UIMenu menu = new InputMenu(
        "Количество элементов:",
        new InputMenuItem((str) -> onInput(str))
    );

    @Override
    public UIScreen show(Scanner scanner) {
        return menu.prompt(scanner);
    }

    private UIScreen onInput(String entityNumberStr) {
        int entityNumber;

        try {
            entityNumber = Integer.parseInt(entityNumberStr);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка. Введите число.");
            return this;
        }

        var menu = Menu.getInstance();
        var filler = new RandomFiller(entityNumber);
        menu.setMoviesFiller(filler);

        return new FillerScreen(this);
    }
}
