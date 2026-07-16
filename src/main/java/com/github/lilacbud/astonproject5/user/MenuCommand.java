package com.github.lilacbud.astonproject5.user;

@FunctionalInterface
public interface MenuCommand<T> {
    public void execute(T client);
}
