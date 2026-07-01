package com.github.lilacbud.astonproject5.ui;

@FunctionalInterface
public interface UIMenuAction<T> {
    UIScreen execute(T payload);

    default UIScreen execute() {
        return execute(null);
    }
}
