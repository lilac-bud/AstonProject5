package com.github.lilacbud.astonproject5.user.ui;

import com.github.lilacbud.astonproject5.user.UserExitException;

public interface UIMenuItem<TInput, TReturn> {
    String getTitle();

    TReturn executeAction(TInput value) throws UserExitException;
}
