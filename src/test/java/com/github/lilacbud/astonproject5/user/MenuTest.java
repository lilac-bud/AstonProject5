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
    private final String validInput = "qwerty\n-2\n0\n3\n2\n";
    private final Menu.MenuOption<MenuTest> option1 = 
            new Menu.MenuOption<>((client) -> client.setValue(1));
    private final Menu.MenuOption<MenuTest> option2 = 
            new Menu.MenuOption<>((client) -> client.setValue(2));
    private final Menu<MenuTest> menu = Menu.StepBuilder.<MenuTest>newBuilder()
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
        setValue(0);
    }

    @Test
    public void givenNullAsCommand_whenCreatingMenuOption_thenThrow() {
        final var thrown = assertThrows(NullPointerException.class, () -> new Menu.MenuOption<MenuTest>(null));
        assertEquals(Menu.MenuOption.COMMAND_NULL_MESSAGE, thrown.getMessage());
    }
    
    @Test
    public void givenSetValueAsCommand_whenExecutingOption_thenValueShouldBeSetToExpected() {
        final int expectedValue = 1;
        final Menu.MenuOption<MenuTest> option = new Menu.MenuOption<>((client) -> client.setValue(expectedValue));
        option.execute(this);
        assertEquals(expectedValue, value);
    }
    
    @Test
    public void givenNullAsOption_whenBuildingMenu_thenThrow() {
        final var thrown = assertThrows(NullPointerException.class, () 
                -> Menu.StepBuilder.newBuilder().withOption(null));
        assertEquals(Menu.StepBuilder.OPTION_NULL_MESSAGE, thrown.getMessage());
    }
    
    @Test
    public void givenThatMenuWasBuiltWithOneOption_whenChoosingOption_thenThatOptionShouldBeExecuted() {
        final int expectedValue = 1;
        Menu.StepBuilder.<MenuTest>newBuilder()
                .withOption(option1)
                .build()
                .chooseOptionAndExecute(null, this);
        assertEquals(expectedValue, value);
    }
    
    @Test
    public void givenNullAsScanner_whenChoosingOption_thenThrow() {
        final Scanner scanner = null;
        final var thrown = assertThrows(NullPointerException.class, () -> menu.chooseOptionAndExecute(scanner, this));
        assertEquals(Menu.SCANNER_NULL_MESSAGE, thrown.getMessage());
    }
    
    @Test
    public void givenScanner_whenChoosingOption_thenPrintAndExecuteAccordingToScanner() {
        System.setErr(new PrintStream(errContent));
        final String expectedErrContent = (Menu.NO_OPTION_MESSAGE + System.lineSeparator()).repeat(2);
        chooseOptionAndExecute(menu, new Scanner(validInput));
        assertEquals(2, value);
        assertEquals(expectedErrContent, errContent.toString());
    }
}
