package com.github.lilacbud.astonproject5.user;

public class UserExitException extends Exception {
    private final boolean forceExit;

    public UserExitException(boolean forceExit) {
        this.forceExit = forceExit;
    }

    public UserExitException() {
        this(false);
    }

    public boolean isForceExit() {
        return forceExit;
    }
}
