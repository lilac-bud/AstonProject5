package com.github.lilacbud.astonproject5.ui;

import java.util.Objects;

public final class SelectMenuItem implements UIMenuItem, UIMenuItemOption {
    private final Character key;
    private final String title;
    private final UIMenuAction<Void> action;

    public SelectMenuItem(Character key, String title, UIMenuAction<Void> action) {
        this.title = title;
        this.key = key;
        this.action = action;
    }

    @Override
    public UIMenuAction getAction() {
        return action;
    }

    @Override
    public String getTitle() {
        return "[%s] - %s".formatted(key, title);
    }

    @Override
    public boolean matchKey(Character key) {
        if (Objects.isNull(key)) {
            return false;
        }

        return (Character.toLowerCase(this.key) == Character.toLowerCase(key));
    }
}
