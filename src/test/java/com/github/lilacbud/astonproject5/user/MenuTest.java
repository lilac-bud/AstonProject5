package com.github.lilacbud.astonproject5.user;

import com.github.lilacbud.astonproject5.user.screen.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class MenuTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalSystemOut = System.out;
    private final InputStream originalSystemIn = System.in;

    static Scanner EOFScanner() {
        return new Scanner(new ByteArrayInputStream(new byte[]{}));
    }

    static Scanner charScanner(String str) {
        return new Scanner(new ByteArrayInputStream(str.getBytes()));
    }

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalSystemOut);
        System.setIn(originalSystemIn);
    }

    @Test
    void menuIsSingleton() {
        var m1 = Menu.getInstance();
        var m2 = Menu.getInstance();

        assertNotNull(m1);
        assertNotNull(m2);
        assertEquals(m1, m2);
    }

    @Test
    void appRun() {
        var menu = Menu.getInstance();
        var scanner = EOFScanner();
        menu.run(scanner);

        var expected = """
            Выберите источник данных:
            [1] - Из файла
            [2] - Ввести вручную
            [3] - Случайные данные
            [Q] - Выход
            >
            """.strip();

        assertEquals(expected, outContent.toString().strip());
    }

    @Test
    void showMainScreen() throws UserExitException {
        var screen = new MainScreen();
        var scanner = EOFScanner();

        assertThrows(
            UserExitException.class,
            () -> screen.show(scanner)
        );

        var expected = """
            Выберите источник данных:
            [1] - Из файла
            [2] - Ввести вручную
            [3] - Случайные данные
            [Q] - Выход
            >
            """.strip();
        ;

        assertEquals(expected, outContent.toString().strip());
    }

    @Test
    void catchMainScreenExit() throws UserExitException {
        var screen = new MainScreen();
        var scanner = charScanner("Q");

        assertThrows(UserExitException.class, () -> screen.show(scanner));
    }

    @Test
    void catchActionsScreenExit() throws UserExitException {
        var screen = new ActionsScreen();
        var scanner = charScanner("Q");

        assertThrows(UserExitException.class, () -> screen.show(scanner));
    }

    @Test
    void goToFileStrategyScreen() throws UserExitException {
        var screen = new MainScreen();
        var scanner = charScanner("1");
        var nextScreen = screen.show(scanner);

        assertInstanceOf(FileStrategyScreen.class, nextScreen);
    }

    @Test
    void showFileStrategyScreen() throws UserExitException {
        var screen = new FileStrategyScreen();
        var scanner = EOFScanner();

        assertThrows(
            UserExitException.class,
            () -> screen.show(scanner)
        );

        var expected = """
            Загрузить из файла:
            >
            """.strip();

        assertEquals(expected, outContent.toString().strip());
    }

    @Test
    void goToManualStrategyScreen() throws UserExitException {
        var screen = new MainScreen();
        var scanner = charScanner("2");
        var nextScreen = screen.show(scanner);

        assertInstanceOf(ManualStrategyScreen.class, nextScreen);
    }

    @Test
    void showManualStrategyScreen() throws UserExitException {
        var screen = new ManualStrategyScreen();
        var scanner = EOFScanner();

        assertThrows(
            UserExitException.class,
            () -> screen.show(scanner)
        );

        var expected = """
            Количество элементов:
            >
            """.strip();

        assertEquals(expected, outContent.toString().strip());
    }

    @Test
    void showActionsScreen() throws UserExitException {
        var screen = new ActionsScreen();
        var scanner = EOFScanner();

        assertThrows(
            UserExitException.class,
            () -> screen.show(scanner)
        );

        var expected = """
            Действие:
            [1] - Показать список
            [2] - Отсортировать
            [3] - Поиск
            [4] - Сохранить в файл
            [5] - На главный экран
            [Q] - Выход
            >
            """.strip();

        assertEquals(expected, outContent.toString().strip());
    }

    @Test
    void goToSortingFieldScreen() throws UserExitException {
        var screen = new ActionsScreen();
        var scanner = charScanner("2");
        var nextScreen = screen.show(scanner);

        assertInstanceOf(SortingFieldScreen.class, nextScreen);
    }

    @Test
    void showSortingFieldScreen() throws UserExitException {
        var screen = new SortingFieldScreen();
        var scanner = EOFScanner();

        assertThrows(
            UserExitException.class,
            () -> screen.show(scanner)
        );

        var expected = """
            Поле для сортировки:
            [1] - Название
            [2] - Год выхода
            [3] - Продолжительность
            >
            """.strip();

        assertEquals(expected, outContent.toString().strip());
    }

    @Test
    void goToSortingOrderScreen() throws UserExitException {
        var screen = new SortingFieldScreen();
        var scanner = charScanner("2");
        var nextScreen = screen.show(scanner);

        assertInstanceOf(SortingOrderScreen.class, nextScreen);
    }

    @Test
    void showSortingOrderScreen() throws UserExitException {
        var screen = new SortingOrderScreen(null);
        var scanner = EOFScanner();

        assertThrows(
            UserExitException.class,
            () -> screen.show(scanner)
        );

        var expected = """
            Порядок сортировки:
            [1] - По возрастанию
            [2] - По убыванию
            >
            """.strip();

        assertEquals(expected, outContent.toString().strip());
    }

    @Test
    void goToSaveToFileScreen() throws UserExitException {
        var screen = new ActionsScreen();
        var scanner = charScanner("4");
        var nextScreen = screen.show(scanner);

        assertInstanceOf(SaveToFileScreen.class, nextScreen);
    }

    @Test
    void showSaveToFileScreen() throws UserExitException {
        var screen = new SaveToFileScreen(null);
        var scanner = EOFScanner();

        assertThrows(
            UserExitException.class,
            () -> screen.show(scanner)
        );

        var expected = """
            Сохранить в файл:
            >
            """.strip();

        assertEquals(expected, outContent.toString().strip());
    }
}
