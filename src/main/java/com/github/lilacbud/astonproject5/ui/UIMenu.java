package com.github.lilacbud.astonproject5.ui;

import java.util.Scanner;

public interface UIMenu<TReturn> {
    TReturn prompt(Scanner scanner) throws ExitException;
}
