package com.github.lilacbud.astonproject5.user.ui;

import com.github.lilacbud.astonproject5.user.Menu;

@FunctionalInterface
public interface UIMenuAction<T> {
    UIScreen execute(T payload) throws Menu.MenuExitException;

    default UIScreen execute() throws Menu.MenuExitException {
        return execute(null);
    }
}
