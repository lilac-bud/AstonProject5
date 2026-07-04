package com.github.lilacbud.astonproject5.user.screen;

import com.github.lilacbud.astonproject5.user.Menu;
import com.github.lilacbud.astonproject5.user.ui.UIScreen;

import java.util.Scanner;

public class MoviesListScreen implements UIScreen {
    @Override
    public UIScreen show(Scanner scanner) throws Menu.MenuExitException {
        // TODO: Вывести список на эран

        System.out.println("Список фильмов (TODO!!!)");

        return new ActionsScreen();
    }
}
