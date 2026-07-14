package com.github.lilacbud.astonproject5.user;

import com.github.lilacbud.astonproject5.util.InputValidation;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;

public class MenuTest {
    private final PrintStream originalErr = System.err;
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final Menu.MenuOption option1 = new Menu.MenuOption("Set value to 1", () -> setValue(1));
    private final Menu.MenuOption option2 = new Menu.MenuOption("Set value to 2", () -> setValue(2));
    private final Menu menu = Menu.StepBuilder.newBuilder()
            .withTitle("Title")
            .withPrompt("Prompt")
            .withOption(option1)
            .withOption(option2)
            .build();
    private final String expectedMenuOutput = """
                                              Title
                                              1. Set value to 1
                                              2. Set value to 2
                                              Prompt""";
    private static int VALUE = 0;
    
    private static void setValue(int value) {
        VALUE = value;
    }
    
    private Menu.MenuOption chooseOption(Menu menu, Scanner scanner) {
        try (MockedStatic<InputValidation> validation = mockStatic(InputValidation.class)) {
            validation.when(() -> InputValidation.validateIntegerInput(anyString()))
                    .thenAnswer(i -> Optional.of(Integer.valueOf(i.getArgument(0))));
            validation.when(() -> InputValidation.validateIntegerInput("qwerty")).thenReturn(Optional.empty());
            validation.when(() -> InputValidation.validateIntegerInput("-2")).thenReturn(Optional.empty());
            
            return menu.chooseOption(scanner);
        }
    }
    
    public MenuTest() {}

    @AfterEach
    public void tearDown() {
        System.setErr(originalErr);
        System.setOut(originalOut);
        setValue(0);
    }

    @Test
    public void testCreateMenuOptionWithNullTitle() {
        System.out.println("MenuOption given null title");
        final var thrown = assertThrows(IllegalArgumentException.class, () 
                -> new Menu.MenuOption(null, () -> setValue(1)));
        assertEquals("Both title and command cannot be null", thrown.getMessage());
    }
    @Test
    public void testCreateMenuOptionWithNullCommand() {
        System.out.println("MenuOption given null command");
        final var thrown = assertThrows(IllegalArgumentException.class, () -> new Menu.MenuOption("Title", null));
        assertEquals("Both title and command cannot be null", thrown.getMessage());
    }
    @Test
    public void testMenuOptionExecute() {
        System.out.println("MenuOption.execute");
        final Menu.MenuOption option = new Menu.MenuOption("Title", () -> setValue(1));
        option.execute();
        assertEquals(1, VALUE);
    }
    @Test
    public void testStepBuilderWithNullTitle() {
        System.out.println("StepBuilder with null title");
        final var thrown = assertThrows(IllegalArgumentException.class, () 
                -> Menu.StepBuilder.newBuilder().withTitle(null));
        assertEquals("Title cannot be null", thrown.getMessage());
    }
    @Test
    public void testStepBuilderWithNullPrompt() {
        System.out.println("StepBuilder with null prompt");
        final var thrown = assertThrows(IllegalArgumentException.class, () 
                -> Menu.StepBuilder.newBuilder().withTitle("Title").withPrompt(null));
        assertEquals("Prompt cannot be null", thrown.getMessage());
    }
    @Test
    public void testStepBuilderWithNullOption() {
        System.out.println("StepBuilder with null prompt");
        final var thrown = assertThrows(IllegalArgumentException.class, () 
                -> Menu.StepBuilder.newBuilder().withTitle("Title").withPrompt("Prompt").withOption(null));
        assertEquals("Option cannot be null", thrown.getMessage());
    }
    @Test
    public void testChooseOptionWithOneOption() {
        System.out.println("chooseOption with one option");
        final Menu.MenuOption expectedOption = new Menu.MenuOption("Option title", () -> setValue(1));
        final Menu.MenuOption option = Menu.StepBuilder.newBuilder()
                .withTitle("Title")
                .withPrompt("Prompt")
                .withOption(expectedOption)
                .build()
                .chooseOption(null);
        option.execute();
        assertEquals(expectedOption, option);
        assertEquals(1, VALUE);
    }
    @Test
    public void testChooseOptionGivenNullScanner() {
        System.out.println("chooseOption given null scanner");
        final var thrown = assertThrows(IllegalArgumentException.class, () -> menu.chooseOption(null));
        assertEquals("Scanner cannot be null", thrown.getMessage());
    }
    @Test
    public void testChooseOptionGivenValidScanner() {
        System.out.println("chooseOption given correct scanner");
        System.setErr(new PrintStream(errContent));
        System.setOut(new PrintStream(outContent));
        final String newline = System.lineSeparator();
        final String expectedErrContent = ("No such option" + newline).repeat(2);
        final String expectedOutContent = expectedMenuOutput.repeat(5).replace("\n", newline);
        final Menu.MenuOption option = chooseOption(menu, new Scanner("qwerty\n-2\n0\n3\n2\n"));
        option.execute();
        assertEquals(option2, option);
        assertEquals(2, VALUE);
        assertEquals(expectedErrContent, errContent.toString());
        assertEquals(expectedOutContent, outContent.toString());
    }
}
