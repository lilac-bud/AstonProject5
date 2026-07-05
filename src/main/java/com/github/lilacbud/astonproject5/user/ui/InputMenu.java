package com.github.lilacbud.astonproject5.user.ui;

import com.github.lilacbud.astonproject5.user.UserExitException;

import java.util.Objects;
import java.util.Scanner;

import static com.github.lilacbud.astonproject5.user.ui.PromptHelpers.promptUserInput;

public class InputMenu implements UIMenu {
    final String title;
    final UIMenuItem inputItem;

    public InputMenu(String title, UIMenuItem inputItem) {
        this.title = title;
        this.inputItem = inputItem;
    }

    @Override
    public UIScreen prompt(Scanner scanner) throws UserExitException {
        var displayTitle = "%s\n> ".formatted(title);

        var text = promptUserInput(
            scanner,
            displayTitle,
            (val) -> val.strip(),
            null
        );

        if (Objects.nonNull(text)) {
            var action = inputItem.getAction();
            return action.execute(text);
        }

        return null;
    }
}
