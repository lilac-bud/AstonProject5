package com.github.lilacbud.astonproject5.ui;

@FunctionalInterface
public interface UIMenuAction<TInput, TReturn> {
    TReturn execute(TInput payload) throws ExitException;
}
