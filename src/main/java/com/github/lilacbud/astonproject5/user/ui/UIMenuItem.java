package com.github.lilacbud.astonproject5.user.ui;

public interface UIMenuItem<TInput, TReturn> {
    String getTitle();

    UIMenuAction<TInput, TReturn> getAction();
}
