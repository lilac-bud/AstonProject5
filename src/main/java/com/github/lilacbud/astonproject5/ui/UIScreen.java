package com.github.lilacbud.astonproject5.ui;

import java.util.Scanner;

public interface UIScreen {
    UIScreen show(Scanner scanner) throws ExitException;
}
