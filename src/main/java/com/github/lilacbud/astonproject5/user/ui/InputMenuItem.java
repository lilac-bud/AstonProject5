package com.github.lilacbud.astonproject5.user.ui;

import com.github.lilacbud.astonproject5.user.UserExitException;

public final class InputMenuItem<R> implements UIMenuItem<String, R> {
    private final UIMenuAction<String, R> action;

    public InputMenuItem(UIMenuAction<String, R> action) {
        this.action = action;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public R executeAction(String value) throws UserExitException {
        return action.execute(value);
    }
}
