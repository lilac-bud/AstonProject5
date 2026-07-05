package com.github.lilacbud.astonproject5.user.ui;

public interface UIMenuItemOption<TInput, TReturn> extends UIMenuItem<TInput, TReturn> {
    boolean matchKey(Character key);
}
