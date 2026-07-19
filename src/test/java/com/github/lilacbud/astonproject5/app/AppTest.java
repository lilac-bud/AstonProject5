package com.github.lilacbud.astonproject5.app;

import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.util.MovieCounter;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
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
    private MoviesSaver saver;

    private final App app = new App();
    
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    
    private final SortingStrategy<Movie> sortStrategy = (movies, comp) -> movies.sort(comp);
    private final Comparator<Movie> comparator = Comparator.comparing(Movie::getYearOfRelease);

    private final String printSuccessMessage = "Movies printed";
    
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
    
    private void configureSaverMock() {
        doAnswer(i -> {
            Path filepath = tempDir.resolve("movies.txt");
            List<Movie> movies = i.getArgument(0);
            Files.write(filepath, movies.stream().map(Movie::toString).toList());
            return null;
        }).when(saver).save(anyList());
    }
    
    private void appCountMovieWithMockedCounter(App app, String target, String successFormat) {
        try (MockedStatic<MovieCounter> validation = mockStatic(MovieCounter.class)) {
            validation.when(() -> MovieCounter.countInsert(mockMovies, movie1.getName())).thenReturn(1);
            
            app.countMovie(target, successFormat);
        }
    }
    
    @BeforeEach
    public void setUp() {
        mockMovies = List.of(movie1, movie2, movie3);
        sortedMockMovies = List.of(movie1, movie3, movie2);
    }
    
    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        actualMovies = null;
    }

    @Test
    public void testRun() {
        System.out.println("run");
        System.setOut(new PrintStream(outContent));
        
        final String printContent = "Print";
        final String expectedOutContent = printContent.repeat(5);

        app.run((client) -> {
            for (int i = 0; i < 5; ++i) {
                System.out.print(printContent);
            }
            client.exit();
        });
        
        assertEquals(expectedOutContent, outContent.toString().trim());
    }

    @Test
    public void testSetFillerGivenNull() {
        System.out.println("setFiller given null");
        final var thrown = assertThrows(NullPointerException.class, () -> app.setFiller(null));
        assertEquals("Filler must not be null", thrown.getMessage());
    }

    @Test
    public void testSetSaverGivenNull() {
        System.out.println("setSaver given null");
        final var thrown = assertThrows(NullPointerException.class, () -> app.setSaver(null));
        assertEquals("Saver must not be null", thrown.getMessage());
    }

    @Test
    public void testSetSortingStrategyGivenNull() {
        System.out.println("setSortingStrategy given null");
        final var thrown = assertThrows(NullPointerException.class, () -> app.setSortingStrategy(null));
        assertEquals("Sorting strategy must not be null", thrown.getMessage());
    }

    @Test
    public void testSetComparatorGivenNull() {
        System.out.println("setComparator given null");
        final var thrown = assertThrows(NullPointerException.class, () -> app.setComparator(null));
        assertEquals("Comparator must not be null", thrown.getMessage());
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
        app.printMovies(null, successMessage);

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
        app.printMovies(printFormat, successMessage);

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
                .map(movie -> String.format(printFormat, 
                        movie.getName(), 
                        movie.getYearOfRelease(), 
                        movie.getHourLength()))
                .collect(Collectors.joining(newline)) + newline + successMessage;

        app.setFiller(filler);
        app.fillMovies();
        app.printMovies("%s, %d, %f", successMessage);

        assertEquals(expectedOutContent, outContent.toString().trim());
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

        System.out.println("sortMovies");
        
        app.setFiller(filler);
        app.fillMovies();
        app.setSortingStrategy(sortStrategy);
        app.setComparator(comparator);
        app.sortMovies();

        assertEquals(sortedMockMovies, actualMovies);
    }

    @Test
    public void testCountMovieGivenCorrectFormat(){
        when(movie1.getName()).thenReturn("Криминальное чтиво");
        configureFillerMock();
        
        System.out.println("countMovie given correct format");
        System.setOut(new PrintStream(outContent));
        
        final String target = movie1.getName();
        final String successFormat = "Фильм \"%s\" встречается %d раз(а)";
        final int expectedCount = 1;
        
        app.setFiller (filler);
        app.fillMovies();
        appCountMovieWithMockedCounter(app, target, successFormat);
        
        assertEquals(String.format(successFormat, target, expectedCount), outContent.toString().trim());
    }

    @Test
    public void testCountMovieGivenIllegalFormat() {
        when(movie1.getName()).thenReturn("Криминальное чтиво");
        configureFillerMock();
        
        System.out.println("countMovie given illegal format");
        System.setOut(new PrintStream(outContent));
        
        final String target = movie1.getName();
        final String successFormat = "%d, %d";
        final int expectedCount = 1;
        
        app.setFiller(filler);
        app.fillMovies();
        appCountMovieWithMockedCounter(app, target, successFormat);
        
        assertEquals(String.valueOf(expectedCount), outContent.toString().trim());
    }

    @Test
    public void testCountMovieGivenNullFormat() {
        when(movie1.getName()).thenReturn("Криминальное чтиво");
        configureFillerMock();
        
        System.out.println("countMovie given null format");
        System.setOut(new PrintStream(outContent));
        
        final String target = movie1.getName();
        final int expectedCount = 1;

        app.setFiller(filler);
        app.fillMovies();
        appCountMovieWithMockedCounter(app, target, null);
        
        assertEquals(String.valueOf(expectedCount), outContent.toString().trim());
    }
}
