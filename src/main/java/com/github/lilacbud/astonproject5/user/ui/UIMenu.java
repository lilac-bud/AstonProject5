package com.github.lilacbud.astonproject5.user.ui;

import com.github.lilacbud.astonproject5.user.UserExitException;

import java.util.Scanner;

public interface UIMenu<TReturn> {
    TReturn prompt(Scanner scanner) throws UserExitException;
}
