package com.github.lilacbud.astonproject5;

import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.movie.MoviesFiller;
import com.github.lilacbud.astonproject5.movie.save.MoviesSaver;
import com.github.lilacbud.astonproject5.movie.sort.MoviesSorter;
import com.github.lilacbud.astonproject5.movie.sort.SortingStrategy;
import com.github.lilacbud.astonproject5.user.Menu;
import com.github.lilacbud.astonproject5.user.MenuCommand;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AppTest {
    @TempDir
    Path tempDir;

    @Mock 
    private Movie movie1, movie2, movie3;
    private List<Movie> mockMovies;
    private List<Movie> sortedMockMovies;

    private List<Movie> actualMovies;
    
    @Mock 
    private MoviesFiller filler;
    @Mock
    private MoviesSorter sorter;
    @Captor 
    private ArgumentCaptor<SortingStrategy> sortStrategyCaptor;
    @Captor 
    private ArgumentCaptor<Comparator<Movie>> compCaptor;
    @Mock 
    private MoviesSaver saver;
    
    @Mock
    private Menu.MenuOption<App> fillMoviesOption, printMoviesOption, sortMoviesOption, saveMoviesOption, exitOption;
    
    @Mock
    private Menu<App> mainMenu;
    
    @InjectMocks
    private App app;
    
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    
    private final SortingStrategy sortStrategy = (movies, comp) -> ((List<Movie>)movies).sort(comp);
    private final Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);
    
    private final String fillSuccessMessage = "Movies filled";
    private final String printSuccessMessage = "Movies printed";
    private final String sortSuccessMessage = "Movies sorted";
    private final String saveSuccessMessage = "Movies saved";
    
    private final String newline = System.lineSeparator();

    private void configureMovieMocksGetName() {
        when(movie1.getName()).thenReturn("Криминальное чтиво");
        when(movie2.getName()).thenReturn("Интерстеллар");
        when(movie3.getName()).thenReturn("Начало");
    }
    private void configureMovieMocksGetYearOfRelease() {
        when(movie1.getYearOfRelease()).thenReturn(1994);
        when(movie2.getYearOfRelease()).thenReturn(2014);
        when(movie3.getYearOfRelease()).thenReturn(2010);
    }
    private void configureMovieMocksGetHourLength() {
        when(movie1.getHourLength()).thenReturn(2.5f);
        when(movie2.getHourLength()).thenReturn(3f);
        when(movie3.getHourLength()).thenReturn(2.5f);
    }
    private void configureMovieMocksToString(){
        when(movie1.toString()).thenReturn("movie1");
        when(movie2.toString()).thenReturn("movie2");
        when(movie3.toString()).thenReturn("movie3");
    }
    private void configureMovieMocks() {
        configureMovieMocksGetName();
        configureMovieMocksGetYearOfRelease();
        configureMovieMocksGetHourLength();
    }
    private void configureFillerMock() {
        doAnswer(i -> {
            List<Movie> movies = i.getArgument(0);
            movies.clear();
            movies.add(movie1);
            movies.add(movie2);
            movies.add(movie3);
            actualMovies = movies;
            return null;
        }).when(filler).fillMovies(anyList());
    }
    private void configureSorterMock() {
        doAnswer(i -> {
            List<Movie> movies = i.getArgument(0);
            sortStrategy.sort(movies, comparator);
            actualMovies = movies;
            return null;
        }).when(sorter).performSorting(anyList());
    }
    private void configureSaverMock() {
        doAnswer(i -> {
            Path filepath = tempDir.resolve("movies.txt");
            List<Movie> movies = i.getArgument(0);
            Files.write(filepath, movies.stream().map(Movie::toString).toList());
            return null;
        }).when(saver).save(anyList());
    }
    private void configureMoviesHelpers() {
        configureFillerMock();
        configureSorterMock();
        configureSaverMock();
    }
    private void configureMenuOptionMock(Menu.MenuOption<App> option, MenuCommand<App> command) {
        doAnswer(i -> {
            App client = i.getArgument(0);
            command.execute(client);
            return null;
        }).when(option).execute(any());
    }
    private void configureMainMenuMocks() {
        configureMenuOptionMock(fillMoviesOption, client -> {
            client.setFiller(filler);
            client.fillMovies();
            System.out.println(fillSuccessMessage);
        });
        configureMenuOptionMock(printMoviesOption, client -> {
            if (client.moviesIsEmpty()) {
                client.setFiller(filler);
                client.fillMovies();
                System.out.println(fillSuccessMessage);
            }
            client.printMovies(printSuccessMessage, null);
        });
        configureMenuOptionMock(sortMoviesOption, client -> {
            if (client.moviesIsEmpty()) {
                client.setFiller(filler);
                client.fillMovies();
                System.out.println(fillSuccessMessage);
            }
            client.setSortingStrategy(sortStrategy);
            client.setComparator(comparator);
            client.sortMovies();
            System.out.println(sortSuccessMessage);
        });
        configureMenuOptionMock(saveMoviesOption, client -> {
            if (client.moviesIsEmpty()) {
                client.setFiller(filler);
                client.fillMovies();
            }
            client.setSaver(saver);
            client.saveMovies();
            System.out.println(saveSuccessMessage);
        });
        configureMenuOptionMock(exitOption, client -> client.exit());
        
        when(mainMenu.chooseOption(any())).thenAnswer(i -> {
            Scanner scanner = i.getArgument(0);
            while (true) {
                switch (scanner.nextLine()) {
                    case "1" -> { return fillMoviesOption; }
                    case "2" -> { return printMoviesOption; }
                    case "3" -> { return sortMoviesOption; }
                    case "4" -> { return saveMoviesOption; }
                    case "5" -> { return exitOption; }
                    default -> {}
                }
            }
        });
    }
    
    @BeforeEach
    public void setUp() {
        mockMovies = List.of(movie1, movie2, movie3);
        sortedMockMovies = List.of(movie1, movie3, movie2);
    }
    
    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        app = null;
        actualMovies = null;
    }

    @Test
    public void testRun() {
        configureMovieMocksToString();
        configureMovieMocksGetYearOfRelease();
        configureMoviesHelpers();
        configureMainMenuMocks();
        System.out.println("run");
        System.setOut(new PrintStream(outContent));

        final Scanner scanner = new Scanner("qwerty\n  \n-2\n0\n8\n1\n3\n2\n4\n5\n");
        final String expectedOutContent = fillSuccessMessage + newline + sortSuccessMessage + newline + 
                sortedMockMovies.stream().map(Movie::toString).collect(Collectors.joining(newline)) 
                + newline + printSuccessMessage + newline + saveSuccessMessage + newline;

        app.run((client) -> mainMenu.chooseOption(scanner).execute(client));

        verify(mainMenu, times(5)).chooseOption(scanner);
        InOrder inOrder = inOrder(fillMoviesOption, sortMoviesOption, printMoviesOption, saveMoviesOption, exitOption);
        inOrder.verify(fillMoviesOption).execute(app);
        inOrder.verify(sortMoviesOption).execute(app);
        inOrder.verify(printMoviesOption).execute(app);
        inOrder.verify(saveMoviesOption).execute(app);
        inOrder.verify(exitOption).execute(app);

        assertFalse(app.moviesIsEmpty());
        assertEquals(expectedOutContent, outContent.toString());
    }

    @Test
    public void testSetFillerGivenNull() {
        System.out.println("setFiller given null");
        final var thrown = assertThrows(NullPointerException.class, () -> app.setFiller(null));
        assertEquals("Filler cannot be null", thrown.getMessage());
    }

    @Test
    public void testSetSaverGivenNull() {
        System.out.println("setSaver given null");
        final var thrown = assertThrows(NullPointerException.class, () -> app.setSaver(null));
        assertEquals("Saver cannot be null", thrown.getMessage());
    }

    @Test
    public void testSetSortingStrategyGivenNull() {
        System.out.println("setSortingStrategy given null");
        final var thrown = assertThrows(NullPointerException.class, () -> app.setSortingStrategy(null));
        assertEquals("Sorting strategy cannot be null", thrown.getMessage());
    }

    @Test
    public void testSetComparatorGivenNull() {
        System.out.println("setComparator given null");
        final var thrown = assertThrows(NullPointerException.class, () -> app.setComparator(null));
        assertEquals("Comparator cannot be null", thrown.getMessage());
    }

    @Test
    public void testPrintMoviesGivenNullFormat() {
        configureMovieMocksToString();
        configureFillerMock();
        System.out.println("printMovies given null print format");
        System.setOut(new PrintStream(outContent));

        final String successMessage = printSuccessMessage;
        final String expectedOutContent = mockMovies.stream().map(Movie::toString).collect(Collectors.joining(newline)) 
                + newline + successMessage + newline;

        app.setFiller(filler);
        app.fillMovies();
        app.printMovies(successMessage, null);

        assertEquals(expectedOutContent, outContent.toString());
    }
    
    @Test
    public void testPrintMoviesGivenIllegalFormat() {
        configureMovieMocksToString();
        configureFillerMock();
        System.out.println("printMovies given illegal print format");
        System.setOut(new PrintStream(outContent));

        final String successMessage = printSuccessMessage;
        final String printFormat = "%d, %d, %d";
        final String expectedOutContent = mockMovies.stream().map(Movie::toString).collect(Collectors.joining(newline)) 
                + newline + successMessage + newline;

        app.setFiller(filler);
        app.fillMovies();
        app.printMovies(successMessage, printFormat);

        assertEquals(expectedOutContent, outContent.toString());
    }
    
    @Test
    public void testPrintMoviesGivenCorrectFormat() {
        configureMovieMocks();
        configureFillerMock();
        System.out.println("printMovies given illegal print format");
        System.setOut(new PrintStream(outContent));

        final String successMessage = printSuccessMessage;
        final String printFormat = "%s, %d, %f";
        final String expectedOutContent = mockMovies.stream()
                .map(movie -> 
                        String.format(printFormat, movie.getName(), movie.getYearOfRelease(), movie.getHourLength()))
                .collect(Collectors.joining(newline)) + newline + successMessage + newline;

        app.setFiller(filler);
        app.fillMovies();
        app.printMovies(successMessage, "%s, %d, %f");

        assertEquals(expectedOutContent, outContent.toString());
    }

    @Test
    public void testSaveMovies() throws Exception {
        configureMovieMocksToString();
        configureFillerMock();
        configureSaverMock();
        System.out.println("saveMovies");

        final Path filepath = tempDir.resolve("movies.txt");
        final List<String> expectedLines = mockMovies.stream().map(Movie::toString).toList();

        app.setFiller(filler);
        app.fillMovies();
        app.setSaver(saver);
        app.saveMovies();

        verify(saver).save(anyList());

        List<String> lines = Files.readAllLines(filepath);
        assertEquals(3, lines.size());
        assertEquals(expectedLines, lines);
    }

    @Test
    public void testFillMovies() {
        configureFillerMock();
        System.out.println("fillMovies");

        app.setFiller(filler);
        app.fillMovies();

        assertFalse(app.moviesIsEmpty());
        verify(filler).fillMovies(anyList());

        assertEquals(mockMovies, actualMovies);
    }

    @Test
    public void testSortMovies() {
        configureMovieMocksGetYearOfRelease();
        configureFillerMock();
        configureSorterMock();
        System.out.println("sortMovies");

        app.setFiller(filler);
        app.fillMovies();
        app.setSortingStrategy(sortStrategy);
        app.setComparator(comparator);
        app.sortMovies();

        InOrder inOrder = inOrder(sorter);
        inOrder.verify(sorter).setSortingStrategy(sortStrategyCaptor.capture());
        inOrder.verify(sorter).setComparator(compCaptor.capture());
        inOrder.verify(sorter).performSorting(anyList());

        assertEquals(sortStrategy, sortStrategyCaptor.getValue());
        assertEquals(comparator, compCaptor.getValue());
        assertEquals(sortedMockMovies, actualMovies);
    }

    @Test
    public void testCountMovieGivenCorrectFormat(){
        configureMovieMocksGetName();
        configureFillerMock();
        System.out.println("countMovie given correct format");
        app.setFiller (filler);
        app.fillMovies();
        System.setOut(new PrintStream(outContent));
        final String target = movie1.getName();
        final String successFormat = "Фильм \"%s\" встречается %d раз(а)";
        final int expectedCount = 1;
        app.countMovie(target, successFormat);
        assertEquals(String.format(successFormat, target, expectedCount),
                outContent.toString().trim());
    }

    @Test
    public void testCountMovieGivenIllegalFormat() {
        configureMovieMocksGetName();
        configureFillerMock();
        System.out.println("countMovie given illegal format");
        app.setFiller(filler);
        app.fillMovies();
        System.setOut(new PrintStream(outContent));
        final String target = movie1.getName();
        final String successFormat = "%d, %d";
        final int expectedCount = 1;
        app.countMovie(target, successFormat);
        assertEquals(String.valueOf(expectedCount), outContent.toString().trim());
    }

    @Test
    public void testCountMovieGivenNullFormat() {
        configureMovieMocksGetName();
        configureFillerMock();
        System.out.println("countMovie given null format");
        app.setFiller(filler);
        app.fillMovies();
        System.setOut(new PrintStream(outContent));
        final String target = movie1.getName();
        final int expectedCount = 1;
        app.countMovie(target, null);
        assertEquals(String.valueOf(expectedCount), outContent.toString().trim());
    }
}
