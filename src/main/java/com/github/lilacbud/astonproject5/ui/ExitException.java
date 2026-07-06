package com.github.lilacbud.astonproject5.ui;

public class ExitException extends Exception {
    private final boolean forceExit;

    public ExitException(boolean forceExit) {
        this.forceExit = forceExit;
    }

    public ExitException() {
        this(false);
    }

    public boolean isForceExit() {
        return forceExit;
    }
}
