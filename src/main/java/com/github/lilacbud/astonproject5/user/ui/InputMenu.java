package com.github.lilacbud.astonproject5.user.ui;

import com.github.lilacbud.astonproject5.user.UserExitException;

import java.util.Objects;
import java.util.Scanner;

import static com.github.lilacbud.astonproject5.user.ui.PromptHelpers.promptUserInput;

public class InputMenu<R> implements UIMenu<R> {
    final String title;
    final UIMenuItem<String, R> inputItem;

    public InputMenu(String title, UIMenuItem<String, R> inputItem) {
        this.title = title;
        this.inputItem = inputItem;
    }

    @Override
    public R prompt(Scanner scanner) throws UserExitException {
        var displayTitle = "%s\n> ".formatted(title);

        var text = promptUserInput(
            scanner,
            displayTitle,
            (val) -> val.strip(),
            null
        );

        if (Objects.isNull(text)) {
            throw new UserExitException(true);
        }

        return inputItem.executeAction(text);
    }
}
