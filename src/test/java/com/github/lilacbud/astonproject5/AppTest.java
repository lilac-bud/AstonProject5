package com.github.lilacbud.astonproject5;

import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.movie.MoviesFiller;
import com.github.lilacbud.astonproject5.movie.save.MoviesSaver;
import com.github.lilacbud.astonproject5.movie.sort.MoviesSorter;
import com.github.lilacbud.astonproject5.movie.sort.SortingStrategy;
import com.github.lilacbud.astonproject5.user.Menu;
import com.github.lilacbud.astonproject5.util.InputValidation;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import static java.util.Objects.requireNonNullElse;
import java.util.Optional;
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
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
    private Menu.MenuOption<App> setFillerOption, setSortStrategyOption, setCompOption, setSaverOption,
            fillMoviesOption, printMoviesOption, sortMoviesOption, saveMoviesOption, exitOption;
    
    @Mock
    private Menu<App> mainMenu, setFillerMenu, setSortMenu, setCompMenu, setSaverMenu;
    
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
    
    private void setUpApp(Scanner scanner) {
        app = App.StepBuilder.newBuilder()
                .withScanner(requireNonNullElse(scanner, new Scanner("")))
                .withMainMenu(mainMenu)
                .withSetFillerMenu(setFillerMenu)
                .withSetSortMenu(setSortMenu)
                .withSetCompMenu(setCompMenu)
                .withSetSaverMenu(setSaverMenu)
                .withMoviesSorter(sorter)
                .build();
    }
    
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
    private void configureMenuOptionMock(Menu.MenuOption<App> option, Menu.MenuCommand<App> command) {
        doAnswer(i -> {
            App client = i.getArgument(0);
            command.execute(client);
            return null;
        }).when(option).execute(any());
    }
    private void configureSetFillerMenuMocks() {
        configureMenuOptionMock(setFillerOption, client -> client.setFiller(filler));
        when(setFillerMenu.chooseOption(any())).thenReturn(setFillerOption);
    }
    private void configureSetSortMenuMocks() {
        configureMenuOptionMock(setSortStrategyOption, client -> client.setSortingStrategy(sortStrategy));
        when(setSortMenu.chooseOption(any())).thenReturn(setSortStrategyOption);
    }
    private void configureSetCompMenuMocks() {
        configureMenuOptionMock(setCompOption, client -> client.setComparator(comparator));
        when(setCompMenu.chooseOption(any())).thenReturn(setCompOption);
    }
    private void configureSetSaverMenuMocks() {
        configureMenuOptionMock(setSaverOption, client -> client.setSaver(saver));
        when(setSaverMenu.chooseOption(any())).thenReturn(setSaverOption);
    }
    private void configureMainMenuMocks() {
        configureSetFillerMenuMocks();
        configureSetSortMenuMocks();
        configureSetCompMenuMocks();
        configureSetSaverMenuMocks();
        configureMenuOptionMock(fillMoviesOption, client -> client.fillMovies(fillSuccessMessage));
        configureMenuOptionMock(printMoviesOption, client -> {
            if (client.moviesIsEmpty())
                client.fillMovies(fillSuccessMessage);
            client.printMovies(printSuccessMessage, null);
        });
        configureMenuOptionMock(sortMoviesOption, client -> {
            if (client.moviesIsEmpty())
                client.fillMovies(fillSuccessMessage);
            client.sortMovies(sortSuccessMessage);
        });
        configureMenuOptionMock(saveMoviesOption, client -> {
            if (client.moviesIsEmpty())
                client.fillMovies(fillSuccessMessage);
            client.saveMovies(saveSuccessMessage);
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
        setUpApp(scanner);
        
        app.run();
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
        setUpApp(null);
        final var thrown = assertThrows(NullPointerException.class, () -> app.setFiller(null));
        assertEquals("Filler cannot be null", thrown.getMessage());
    }

    @Test
    public void testSetSaverGivenNull() {
        System.out.println("setSaver given null");
        setUpApp(null);
        final var thrown = assertThrows(NullPointerException.class, () -> app.setSaver(null));
        assertEquals("Saver cannot be null", thrown.getMessage());
    }

    @Test
    public void testSetSortingStrategyGivenNull() {
        System.out.println("setSortingStrategy given null");
        setUpApp(null);
        final var thrown = assertThrows(NullPointerException.class, () -> app.setSortingStrategy(null));
        assertEquals("Sorting strategy cannot be null", thrown.getMessage());
    }

    @Test
    public void testSetComparatorGivenNull() {
        System.out.println("setComparator given null");
        setUpApp(null);
        final var thrown = assertThrows(NullPointerException.class, () -> app.setComparator(null));
        assertEquals("Comparator cannot be null", thrown.getMessage());
    }

    @Test
    public void testAskFilepath() {
        System.out.println("askFilepath");
        System.setOut(new PrintStream(outContent));
        final String prompt = "Filepath:";
        String result;
        try (MockedStatic<InputValidation> validation = mockStatic(InputValidation.class)) {
            validation.when(() -> InputValidation.validateInput(anyString()))
                    .thenAnswer(i -> Optional.of(i.getArgument(0)));
            validation.when(() -> InputValidation.validateInput("")).thenReturn(Optional.empty());
            validation.when(() -> InputValidation.validateInput(" ")).thenReturn(Optional.empty());
            
            setUpApp(new Scanner("\n \nfilepath\n"));
            result = app.askFilepath(prompt);
        }
        assertEquals("filepath", result);
        assertEquals(prompt.repeat(3), outContent.toString().trim());
    }

    @Test
    public void testAskSize() {
        System.out.println("askSize");
        System.setOut(new PrintStream(outContent));
        final String prompt = "Size:";
        int result;
        try (MockedStatic<InputValidation> validation = mockStatic(InputValidation.class)) {
            validation.when(() -> InputValidation.validateIntegerInput(anyString()))
                    .thenAnswer(i -> Optional.of(Integer.valueOf(i.getArgument(0))));
            validation.when(() -> InputValidation.validateIntegerInput("")).thenReturn(Optional.empty());
            validation.when(() -> InputValidation.validateIntegerInput(" ")).thenReturn(Optional.empty());
            validation.when(() -> InputValidation.validateIntegerInput("qwerty")).thenReturn(Optional.empty());
            validation.when(() -> InputValidation.validateIntegerInput("-2")).thenReturn(Optional.empty());
            
            setUpApp(new Scanner("\n \nqwerty\n-2\n2\n"));
            result = app.askSize(prompt);
        }
        assertEquals(2, result);
        assertEquals(prompt.repeat(5), outContent.toString().trim());
    }

    @Test
    public void testPrintMoviesGivenNullFormat() {
        configureMovieMocksToString();
        configureFillerMock();
        configureSetFillerMenuMocks();
        System.out.println("printMovies given null print format");
        setUpApp(null);
        app.fillMovies(null);
        System.setOut(new PrintStream(outContent));
        final String successMessage = printSuccessMessage;
        final String expectedOutContent = mockMovies.stream().map(Movie::toString).collect(Collectors.joining(newline)) 
                + newline + successMessage + newline;
        app.printMovies(successMessage, null);
        assertEquals(expectedOutContent, outContent.toString());
    }
    
    @Test
    public void testPrintMoviesGivenIllegalFormat() {
        configureMovieMocksToString();
        configureFillerMock();
        configureSetFillerMenuMocks();
        System.out.println("printMovies given illegal print format");
        setUpApp(null);
        app.fillMovies(null);
        System.setOut(new PrintStream(outContent));
        final String successMessage = printSuccessMessage;
        final String printFormat = "%d, %d, %d";
        final String expectedOutContent = mockMovies.stream().map(Movie::toString).collect(Collectors.joining(newline)) 
                + newline + successMessage + newline;
        app.printMovies(successMessage, printFormat);
        assertEquals(expectedOutContent, outContent.toString());
    }
    
    @Test
    public void testPrintMoviesGivenCorrectFormat() {
        configureMovieMocks();
        configureFillerMock();
        configureSetFillerMenuMocks();
        System.out.println("printMovies given illegal print format");
        setUpApp(null);
        app.fillMovies(null);
        System.setOut(new PrintStream(outContent));
        final String successMessage = printSuccessMessage;
        final String printFormat = "%s, %d, %f";
        final String expectedOutContent = mockMovies.stream()
                .map(movie -> 
                        String.format(printFormat, movie.getName(), movie.getYearOfRelease(), movie.getHourLength()))
                .collect(Collectors.joining(newline)) + newline + successMessage + newline;
        app.printMovies(successMessage, "%s, %d, %f");
        assertEquals(expectedOutContent, outContent.toString());
    }

    @Test
    public void testSaveMovies() throws Exception {
        configureMovieMocksToString();
        configureFillerMock();
        configureSaverMock();
        configureSetFillerMenuMocks();
        configureSetSaverMenuMocks();
        System.out.println("saveMovies");
        setUpApp(null);
        app.fillMovies(null);
        System.setOut(new PrintStream(outContent));
        final String successMessage = saveSuccessMessage;
        final Path filepath = tempDir.resolve("movies.txt");
        final List<String> expectedLines = mockMovies.stream().map(Movie::toString).toList();
        app.saveMovies(successMessage);
        InOrder inOrder = inOrder(setSaverMenu, setSaverOption, saver);
        inOrder.verify(setSaverMenu).chooseOption(any());
        inOrder.verify(setSaverOption).execute(app);
        inOrder.verify(saver).save(anyList());
        List<String> lines = Files.readAllLines(filepath);
        assertEquals(3, lines.size());
        assertEquals(expectedLines, lines);
        assertEquals(successMessage, outContent.toString().trim());
    }

    @Test
    public void testFillMovies() {
        configureFillerMock();
        configureSetFillerMenuMocks();
        System.out.println("fillMovies");
        System.setOut(new PrintStream(outContent));
        setUpApp(null);
        final String successMessage = fillSuccessMessage;
        app.fillMovies(successMessage);
        assertFalse(app.moviesIsEmpty());
        InOrder inOrder = inOrder(setFillerMenu, setFillerOption, filler);
        inOrder.verify(setFillerMenu).chooseOption(any());
        inOrder.verify(setFillerOption).execute(app);
        inOrder.verify(filler).fillMovies(anyList());
        assertEquals(mockMovies, actualMovies);
        assertEquals(successMessage, outContent.toString().trim());
    }

    @Test
    public void testSortMovies() {
        configureMovieMocksGetYearOfRelease();
        configureFillerMock();
        configureSorterMock();
        configureSetFillerMenuMocks();
        configureSetSortMenuMocks();
        configureSetCompMenuMocks();
        System.out.println("sortMovies");
        setUpApp(null);
        app.fillMovies(null);
        System.setOut(new PrintStream(outContent));
        final String successMessage = sortSuccessMessage;
        app.sortMovies(successMessage);
        InOrder inOrder = inOrder(setSortMenu, setSortStrategyOption, setCompMenu, setCompOption, sorter);
        inOrder.verify(setSortMenu).chooseOption(any());
        inOrder.verify(setSortStrategyOption).execute(app);
        inOrder.verify(sorter).setSortingStrategy(sortStrategyCaptor.capture());
        inOrder.verify(setCompMenu).chooseOption(any());
        inOrder.verify(setCompOption).execute(app);
        inOrder.verify(sorter).setComparator(compCaptor.capture());
        inOrder.verify(sorter).performSorting(anyList());
        assertEquals(sortStrategy, sortStrategyCaptor.getValue());
        assertEquals(comparator, compCaptor.getValue());
        assertEquals(sortedMockMovies, actualMovies);
        assertEquals(successMessage, outContent.toString().trim());
    }
    
    @Test
    public void testCreateStepBuilderWithNullScanner() {
        System.out.println("StepBuilder with null scanner");
        final var thrown = assertThrows(NullPointerException.class, () -> App.StepBuilder.newBuilder()
                .withScanner(null));
        assertEquals("Scanner cannot be null", thrown.getMessage());
    }
    @Test
    public void testCreateBuilderWithAnyNullMenu() {
        System.out.println("StepBuilder with any null menu");
        final var thrown = assertThrows(NullPointerException.class, () -> App.StepBuilder.newBuilder()
                .withScanner(new Scanner(""))
                .withMainMenu(null));
        assertEquals("Menu cannot be null", thrown.getMessage());
    }
    @Test
    public void testCreateBuilderWithNullSorter() {
        System.out.println("StepBuilder with null sorter");
        final var thrown = assertThrows(NullPointerException.class, () -> App.StepBuilder.newBuilder()
                .withScanner(new Scanner(""))
                .withMainMenu(mainMenu)
                .withSetFillerMenu(setFillerMenu)
                .withSetSortMenu(setSortMenu)
                .withSetCompMenu(setCompMenu)
                .withSetSaverMenu(setSaverMenu)
                .withMoviesSorter(null));
        assertEquals("Sorter cannot be null", thrown.getMessage());
    }
}
