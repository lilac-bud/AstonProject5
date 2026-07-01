package com.github.lilacbud.astonproject5.app;

import com.github.lilacbud.astonproject5.app.screen.MainScreen;
import com.github.lilacbud.astonproject5.ui.UIMenu;
import com.github.lilacbud.astonproject5.ui.UIScreen;

import java.util.Objects;
import java.util.Scanner;

public class App {
    private static App app;
    private UIScreen currentScreen;
    private Scanner scanner = new Scanner(System.in);

    private App() {
    }

    public static App getInstance() {
        if (Objects.isNull(app)) {
            app = new App();
            app.currentScreen = new MainScreen();
        }

        return app;
    }

    public void run() {
        do {
            try {
                UIMenu menu = app.currentScreen.getMenu();
                UIScreen nextScreen = menu.prompt(scanner);
                app.currentScreen = nextScreen;
            } catch (Throwable ex) {
                System.out.println(ex);
            }
        } while (Objects.nonNull(app.currentScreen));

        app.exit();
    }

    public void exit() {
        scanner.close();
    }

    public void useFillStrategy(Object strategy) {
        // TODO: задать стратегию
    }

    public void fillData() throws Throwable {
        // TODO: заполнить список фильмов при помощи выбранной стратегии
        // Выбросить исключение, если неудалось
//        throw new Exception();
    }

    public void addData(Object data) {
        // TODO: добавить в список фильмов при помощи выбранной стратегии
        // Выбросить исключение, если неудалось
//        throw new Exception();
    }
}
