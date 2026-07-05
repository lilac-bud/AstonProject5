package com.github.lilacbud.astonproject5.user.ui;

import com.github.lilacbud.astonproject5.user.UserExitException;

@FunctionalInterface
public interface UIMenuAction<T> {
    UIScreen execute(T payload) throws UserExitException;

    default UIScreen execute() throws UserExitException {
        return execute(null);
    }
}
