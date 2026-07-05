package com.github.lilacbud.astonproject5.user.ui;

import com.github.lilacbud.astonproject5.user.UserExitException;

public interface UIMenuItemOption<TInput, TReturn> extends UIMenuItem<TInput, TReturn> {
    boolean matchKey(Character key);
    TInput getValue() throws UserExitException;
}
