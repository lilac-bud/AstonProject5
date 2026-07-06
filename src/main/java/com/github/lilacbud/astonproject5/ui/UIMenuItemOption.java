package com.github.lilacbud.astonproject5.ui;

public interface UIMenuItemOption<TInput, TReturn> extends UIMenuItem<TInput, TReturn> {
    boolean matchKey(Character key);
    TInput getValue();
}
