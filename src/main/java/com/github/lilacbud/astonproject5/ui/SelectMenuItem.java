package com.github.lilacbud.astonproject5.ui;

import java.util.Objects;

public final class SelectMenuItem<T, R> implements UIMenuItemOption<T, R> {
    private final Character key;
    private final String title;
    private final T value;
    private final UIMenuAction<T, R> action;

    public SelectMenuItem(Character key, String title, T value, UIMenuAction<T, R> action) {
        this.key = key;
        this.title = title;
        this.value = value;
        this.action = action;
    }

    public SelectMenuItem(Character key, String title, UIMenuAction<Void, R> action) {
        this(key, title, null, (_) -> action.execute(null));
    }

    @Override
    public R executeAction(T value) throws ExitException {
        return action.execute(value);
    }

    @Override
    public String getTitle() {
        return "[%s] - %s".formatted(key, title);
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public boolean matchKey(Character key) {
        if (Objects.isNull(key)) {
            return false;
        }

        return (Character.toLowerCase(this.key) == Character.toLowerCase(key));
    }
}
