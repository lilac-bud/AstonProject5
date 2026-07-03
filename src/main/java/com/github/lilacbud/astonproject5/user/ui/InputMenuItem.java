package com.github.lilacbud.astonproject5.user.ui;

public final class InputMenuItem implements UIMenuItem {
    private final UIMenuAction<String> action;

    public InputMenuItem(UIMenuAction<String> action) {
        this.action = action;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public UIMenuAction getAction() {
        return action;
    }
}
