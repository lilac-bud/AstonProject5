package com.github.lilacbud.astonproject5.user;

import com.github.lilacbud.astonproject5.util.InputValidation;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mockStatic;

public class MenuTest {
    private final PrintStream originalErr = System.err;
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final String validInput = "qwerty\n-2\n0\n3\n2\n";
    private final Menu.MenuOption<MenuTest> option1 = 
            new Menu.MenuOption<>("Set value to 1", (client) -> client.setValue(1));
    private final Menu.MenuOption<MenuTest> option2 = 
            new Menu.MenuOption<>("Set value to 2", (client) -> client.setValue(2));
    private final Menu<MenuTest> menu = Menu.StepBuilder.<MenuTest>newBuilder()
            .withTitle("Title")
            .withPrompt("Prompt")
            .withOption(option1)
            .withOption(option2)
            .build();
    private int value = 0;
    
    private void setValue(int value) {
        this.value = value;
    }
    
    private void chooseOptionAndExecute(Menu<MenuTest> menu, Scanner scanner) {
        try (MockedStatic<InputValidation> validation = mockStatic(InputValidation.class)) {
            validation.when(() -> InputValidation.validateIntegerInput(anyString()))
                    .thenAnswer(i -> Optional.of(Integer.valueOf(i.getArgument(0))));
            validation.when(() -> InputValidation.validateIntegerInput("qwerty")).thenReturn(Optional.empty());
            validation.when(() -> InputValidation.validateIntegerInput("-2")).thenReturn(Optional.empty());
            
            menu.chooseOptionAndExecute(scanner, this);
        }
    }
    
    @AfterEach
    public void tearDown() {
        System.setErr(originalErr);
        System.setOut(originalOut);
        setValue(0);
    }

    @Test
    public void testCreateMenuOptionWithNullCommand() {
        System.out.println("MenuOption given null command");
        final var thrown = assertThrows(NullPointerException.class, () -> new Menu.MenuOption<MenuTest>("Title", null));
        assertEquals("Command must not be null", thrown.getMessage());
    }
    
    @Test
    public void testMenuOptionExecute() {
        System.out.println("MenuOption.execute");
        final Menu.MenuOption<MenuTest> option = new Menu.MenuOption<>("Title", (client) -> client.setValue(1));
        option.execute(this);
        assertEquals(1, value);
    }
    
    @Test
    public void testStepBuilderWithNullOption() {
        System.out.println("StepBuilder with null option");
        final var thrown = assertThrows(NullPointerException.class, () 
                -> Menu.StepBuilder.newBuilder().withTitle("Title").withPrompt("Prompt").withOption(null));
        assertEquals("Option must not be null", thrown.getMessage());
    }
    
    @Test
    public void testChooseOptionWithOneOption() {
        System.out.println("chooseOption with one option");
        Menu.StepBuilder.<MenuTest>newBuilder()
                .withOption(new Menu.MenuOption<>("Option title", (client) -> client.setValue(1)))
                .build()
                .chooseOptionAndExecute(null, this);
        assertEquals(1, value);
    }
    
    @Test
    public void testChooseOptionGivenNullScanner() {
        System.out.println("chooseOption given null scanner");
        final var thrown = assertThrows(NullPointerException.class, () -> menu.chooseOptionAndExecute(null, this));
        assertEquals("Scanner must not be null", thrown.getMessage());
    }
    
    @Test
    public void testChooseOptionGivenValidScanner() {
        System.out.println("chooseOption given correct scanner");
        System.setErr(new PrintStream(errContent));
        System.setOut(new PrintStream(outContent));
        
        final String newline = System.lineSeparator();
        final String expectedErrContent = ("No such option" + newline).repeat(2);
        final String expectedMenuOutput = """
                                          Title
                                          1. Set value to 1
                                          2. Set value to 2
                                          Prompt""";
        final String expectedOutContent = expectedMenuOutput.repeat(5).replace("\n", newline);
        
        chooseOptionAndExecute(menu, new Scanner(validInput));

        assertEquals(2, value);
        assertEquals(expectedErrContent, errContent.toString());
        assertEquals(expectedOutContent, outContent.toString());
    }
    
    @Test
    public void testChooseOptionWithOptionsWithNullTitleGivenValidScanner() {
        System.out.println("chooseOption with options with null title given correct scanner");
        System.setErr(new PrintStream(errContent));
        System.setOut(new PrintStream(outContent));
        
        final String newline = System.lineSeparator();
        final String expectedErrContent = ("No such option" + newline).repeat(2);
        final String expectedMenuOutput = """
                                          Title
                                          Prompt""";
        final String expectedOutContent = expectedMenuOutput.repeat(5).replace("\n", newline);

        final Menu<MenuTest> tempMenu = Menu.StepBuilder.<MenuTest>newBuilder()
                .withTitle("Title")
                .withPrompt("Prompt")
                .withOption(new Menu.MenuOption<>(null, (client) -> client.setValue(1)))
                .withOption(new Menu.MenuOption<>(null, (client) -> client.setValue(2)))
                .build();
        
        chooseOptionAndExecute(tempMenu, new Scanner(validInput));

        assertEquals(2, value);
        assertEquals(expectedErrContent, errContent.toString());
        assertEquals(expectedOutContent, outContent.toString());
    }
    
    @Test
    public void testChooseOptionWithNullTitleAndPromptGivenValidScanner() {
        System.out.println("chooseOption with null title and prompt given correct scanner");
        System.setErr(new PrintStream(errContent));
        System.setOut(new PrintStream(outContent));
        
        final String newline = System.lineSeparator();
        final String expectedErrContent = ("No such option" + newline).repeat(2);
        final String expectedMenuOutput = """
                                          1. Set value to 1
                                          2. Set value to 2\n""";
        final String expectedOutContent = expectedMenuOutput.repeat(5).replace("\n", newline);
        final Menu<MenuTest> tempMenu = Menu.StepBuilder.<MenuTest>newBuilder()
                .withOption(option1)
                .withOption(option2)
                .build();
        
        chooseOptionAndExecute(tempMenu, new Scanner(validInput));
        
        assertEquals(2, value);
        assertEquals(expectedErrContent, errContent.toString());
        assertEquals(expectedOutContent, outContent.toString());
    }
    
    @Test
    public void testChooseOptionWithNullTitlesAndPromptGivenValidScanner() {
        System.out.println("chooseOption with null titles and prompt given correct scanner");
        System.setErr(new PrintStream(errContent));
        System.setOut(new PrintStream(outContent));
        
        final String newline = System.lineSeparator();
        final String expectedErrContent = ("No such option" + newline).repeat(2);
        final String expectedOutContent = "";

        final Menu<MenuTest> tempMenu = Menu.StepBuilder.<MenuTest>newBuilder()
                .withOption(new Menu.MenuOption<>(null, (client) -> client.setValue(1)))
                .withOption(new Menu.MenuOption<>(null, (client) -> client.setValue(2)))
                .build();
        
        chooseOptionAndExecute(tempMenu, new Scanner(validInput));
        
        assertEquals(2, value);
        assertEquals(expectedErrContent, errContent.toString());
        assertEquals(expectedOutContent, outContent.toString());
    }
}
