package com.github.lilacbud.astonproject5.user.menu.screen;

import com.github.lilacbud.astonproject5.user.menu.Menu;
import com.github.lilacbud.astonproject5.user.ui.UIScreen;

import java.util.ArrayList;
import java.util.Scanner;

public class FillerScreen implements UIScreen {
    private final UIScreen backScreen;

    public FillerScreen(UIScreen backScreen) {
        this.backScreen = backScreen;
    }

    @Override
    public UIScreen show(Scanner scanner) {
        var menu = Menu.getInstance();

        try {
            menu.getMoviesFiller().fillMovies(new ArrayList<>());
        } catch (Throwable e) {
            System.out.println(e);
            return backScreen;
        }

        return new ActionsScreen();
    }
}
