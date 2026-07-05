package com.github.lilacbud.astonproject5.user.ui;

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
    public UIMenuAction<String, R> getAction() {
        return action;
    }
}
