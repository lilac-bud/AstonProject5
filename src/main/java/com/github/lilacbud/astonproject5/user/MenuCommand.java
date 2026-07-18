package com.github.lilacbud.astonproject5.user;

@FunctionalInterface
public interface MenuCommand<T> {
    void execute(T client);
}
