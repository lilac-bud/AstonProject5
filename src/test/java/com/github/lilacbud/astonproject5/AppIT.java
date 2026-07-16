package com.github.lilacbud.astonproject5;

import com.github.lilacbud.astonproject5.movie.FromFileFiller;
import com.github.lilacbud.astonproject5.movie.ManualFiller;
import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.movie.RandomFiller;
import com.github.lilacbud.astonproject5.movie.save.DefaultSaver;
import com.github.lilacbud.astonproject5.movie.save.MoviesSaver;
import com.github.lilacbud.astonproject5.movie.sort.EvenNumbersSortDecorator;
import com.github.lilacbud.astonproject5.movie.sort.MergeSort;
import com.github.lilacbud.astonproject5.user.Menu;
import com.github.lilacbud.astonproject5.util.InputRequest;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.io.TempDir;

public class AppIT {
    @TempDir
    Path tempDir;

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    
    private final List<String> expectedFileLines = List.of(
            "Криминальное чтиво;1994;2.5",
            "Интерстеллар;2014;3.0",
            "Начало;2010;2.5",
            "Криминальное чтиво;1994;2.5",
            "Начало;2010;2.5",
            "Интерстеллар;2014;3.0");
    private final String expectedOutContent = String.join(System.lineSeparator(), expectedFileLines).replace(".", ",");

    private final String printFormat = "%s;%d;%.1f";
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testRun() throws Exception {
        System.out.println("run");
        System.setOut(new PrintStream(outContent));
        
        final Path filepath = tempDir.resolve("movies.txt");
        final String filepathString = filepath.toString();
        final String input = """
                             qwerty
                             -2
                             0
                             6
                             1
                             3
                             3
                             Криминальное чтиво
                             1500
                             1994
                             2.5
                             Интерстеллар
                             2014
                             3
                             Начало
                             2010
                             2.5
                             4
                             %s
                             3
                             1
                             2
                             4
                             %s
                             2
                             1
                             1
                             %s
                             2
                             5""".formatted(filepathString, filepathString, filepathString);
        
        Scanner scanner = new Scanner(input);

        Menu<App> setFillerMenu = Menu.StepBuilder.<App>newBuilder()
                .withOption(new Menu.MenuOption<>((client) -> 
                        client.setFiller(new FromFileFiller(InputRequest.askString(scanner)))))
                .withOption(new Menu.MenuOption<>((client) -> 
                        client.setFiller(new RandomFiller(InputRequest.askInteger(scanner)))))
                .withOption(new Menu.MenuOption<>((client) -> 
                        client.setFiller(new ManualFiller(InputRequest.askInteger(scanner), scanner, null))))
                .build();
        Menu<App> setSortMenu = Menu.StepBuilder.<App>newBuilder()
                .withOption(new Menu.MenuOption<>((client)
                        -> client.setSortingStrategy(new MergeSort())))
                .withOption(new Menu.MenuOption<>((client) -> {
                    var sortStrategy = new EvenNumbersSortDecorator(new MergeSort(), Movie::getYearOfRelease);
                    client.setSortingStrategy(sortStrategy);
                }))
                .build();
        Menu<App> setCompMenu = Menu.StepBuilder.<App>newBuilder()
                .withOption(new Menu.MenuOption<>((client) -> client.setComparator(Movie.compareByName)))
                .withOption(new Menu.MenuOption<>((client) -> client.setComparator(Movie.compareByYearOfRelease)))
                .withOption(new Menu.MenuOption<>((client) -> client.setComparator(Movie.compareByHourLength)))
                .build();
        Menu<MoviesSaver> setSaveOptionMenu = Menu.StepBuilder.<MoviesSaver>newBuilder()
                .withOption(new Menu.MenuOption<>((client)
                        -> client.setSaveOption(StandardOpenOption.TRUNCATE_EXISTING)))
                .withOption(new Menu.MenuOption<>((client)
                        -> client.setSaveOption(StandardOpenOption.APPEND)))
                .build();
        Menu<App> setSaverMenu = Menu.StepBuilder.<App>newBuilder()
                .withOption(new Menu.MenuOption<>((client) -> 
                        client.setSaver(new DefaultSaver(InputRequest.askString(scanner), scanner, setSaveOptionMenu))))
                .build();
        Menu<App> mainMenu = Menu.StepBuilder.<App>newBuilder()
                .withOption(new Menu.MenuOption<>((client) -> client.tryCommandTillSuccess((_client) -> {
                    setFillerMenu.chooseOption(scanner).execute(_client);
                    _client.fillMovies();
                })))
                .withOption(new Menu.MenuOption<>((client) -> client.printMovies(null, printFormat)))
                .withOption(new Menu.MenuOption<>((client) -> client.tryCommandTillSuccess((_client) -> {
                    setSortMenu.chooseOption(scanner).execute(_client);
                    setCompMenu.chooseOption(scanner).execute(_client);
                    _client.sortMovies();
                })))
                .withOption(new Menu.MenuOption<>((client) -> client.tryCommandTillSuccess((_client) -> {
                    setSaverMenu.chooseOption(scanner).execute(_client);
                    _client.saveMovies();
                })))
                .withOption(new Menu.MenuOption<>((client) -> client.exit()))
                .build();
        
        new App().run((client) -> mainMenu.chooseOption(scanner).execute(client));
        
        assertEquals(expectedFileLines, Files.readAllLines(filepath));
        assertEquals(expectedOutContent, outContent.toString().trim());
    }
}
