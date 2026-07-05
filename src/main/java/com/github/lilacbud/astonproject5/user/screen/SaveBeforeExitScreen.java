package com.github.lilacbud.astonproject5.user.screen;

import com.github.lilacbud.astonproject5.user.UserExitException;
import com.github.lilacbud.astonproject5.user.ui.SelectMenu;
import com.github.lilacbud.astonproject5.user.ui.SelectMenuItem;
import com.github.lilacbud.astonproject5.user.ui.UIMenu;
import com.github.lilacbud.astonproject5.user.ui.UIScreen;

import java.util.Scanner;

public class SaveBeforeExitScreen implements UIScreen {
    final private UIMenu<UIScreen> menu = new SelectMenu<>(
        "Сохранить изменения в файл?",
        new SelectMenuItem<>('Y', "Да (Выбрать файл)", (e) -> onSaveChanges()),
        // TODO: Сохранение в последний открытый файл
        // new SelectMenuItem('S', "Да (В {{LastFileName}})", (e) -> onSaveIntoLastChanges()),
        new SelectMenuItem<>('N', "Нет", (e) -> onDropChanges())
    );

    private UIScreen onDropChanges() {
        return null;
    }

    private UIScreen onSaveChanges() {
        return new SaveToFileScreen(null);
    }

    @Override
    public UIScreen show(Scanner scanner) throws UserExitException {
        return menu.prompt(scanner);
    }
}
