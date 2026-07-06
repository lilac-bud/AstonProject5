package com.github.lilacbud.astonproject5.ui;

public interface UIMenuItem<TInput, TReturn> {
    String getTitle();

    TReturn executeAction(TInput value) throws ExitException;
}
