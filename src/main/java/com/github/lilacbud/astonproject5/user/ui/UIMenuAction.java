package com.github.lilacbud.astonproject5.user.ui;

import com.github.lilacbud.astonproject5.user.UserExitException;

@FunctionalInterface
public interface UIMenuAction<TInput, TReturn> {
    TReturn execute(TInput payload) throws UserExitException;
}
