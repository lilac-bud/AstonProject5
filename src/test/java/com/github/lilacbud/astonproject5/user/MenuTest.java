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
    
    private Menu.MenuOption<MenuTest> chooseOption(Menu<MenuTest> menu, Scanner scanner) {
        try (MockedStatic<InputValidation> validation = mockStatic(InputValidation.class)) {
            validation.when(() -> InputValidation.validateIntegerInput(anyString()))
                    .thenAnswer(i -> Optional.of(Integer.valueOf(i.getArgument(0))));
            validation.when(() -> InputValidation.validateIntegerInput("qwerty")).thenReturn(Optional.empty());
            validation.when(() -> InputValidation.validateIntegerInput("-2")).thenReturn(Optional.empty());
            
            return menu.chooseOption(scanner);
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
        System.out.println("StepBuilder with null prompt");
        final var thrown = assertThrows(NullPointerException.class, () 
                -> Menu.StepBuilder.newBuilder().withTitle("Title").withPrompt("Prompt").withOption(null));
        assertEquals("Option cannot be null", thrown.getMessage());
    }
    
    @Test
    public void testChooseOptionWithOneOption() {
        System.out.println("chooseOption with one option");
        final Menu.MenuOption<MenuTest> expectedOption = 
                new Menu.MenuOption<>("Option title", (client) -> client.setValue(1));
        final Menu.MenuOption<MenuTest> option = Menu.StepBuilder.<MenuTest>newBuilder()
                .withOption(expectedOption)
                .build()
                .chooseOption(null);
        option.execute(this);
        assertEquals(expectedOption, option);
        assertEquals(1, value);
    }
    
    @Test
    public void testChooseOptionGivenNullScanner() {
        System.out.println("chooseOption given null scanner");
        final var thrown = assertThrows(NullPointerException.class, () -> menu.chooseOption(null));
        assertEquals("Scanner cannot be null", thrown.getMessage());
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
        
        final Menu.MenuOption<MenuTest> option = chooseOption(menu, new Scanner(validInput));
        option.execute(this);
        
        assertEquals(option2, option);
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
        
        final Menu.MenuOption<MenuTest> tempOption1 = new Menu.MenuOption<>(null, (client) -> client.setValue(1));
        final Menu.MenuOption<MenuTest> tempOption2 = new Menu.MenuOption<>(null, (client) -> client.setValue(2));
        final Menu<MenuTest> tempMenu = Menu.StepBuilder.<MenuTest>newBuilder()
                .withTitle("Title")
                .withPrompt("Prompt")
                .withOption(tempOption1)
                .withOption(tempOption2)
                .build();
        
        final Menu.MenuOption<MenuTest> option = chooseOption(tempMenu, new Scanner(validInput));
        option.execute(this);
        
        assertEquals(tempOption2, option);
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
        
        final Menu.MenuOption<MenuTest> option = chooseOption(tempMenu, new Scanner(validInput));
        option.execute(this);
        
        assertEquals(option2, option);
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
        
        final Menu.MenuOption<MenuTest> tempOption1 = new Menu.MenuOption<>(null, (client) -> client.setValue(1));
        final Menu.MenuOption<MenuTest> tempOption2 = new Menu.MenuOption<>(null, (client) -> client.setValue(2));
        final Menu<MenuTest> tempMenu = Menu.StepBuilder.<MenuTest>newBuilder()
                .withOption(tempOption1)
                .withOption(tempOption2)
                .build();
        
        final Menu.MenuOption<MenuTest> option = chooseOption(tempMenu, new Scanner(validInput));
        option.execute(this);
        
        assertEquals(tempOption2, option);
        assertEquals(2, value);
        assertEquals(expectedErrContent, errContent.toString());
        assertEquals(expectedOutContent, outContent.toString());
    }
}
