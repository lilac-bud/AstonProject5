package com.github.lilacbud.astonproject5.movie;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FromFileFillerTest {
    @Mock
    private Movie movie;
    
    private final List<String> expectedNames = List.of("The Shawshank Redemption", "The Godfather", 
            "The Matrix", "Inception", "Interstellar");
    private final List<Integer> expectedYears = List.of(1994, 1972, 1999, 2010, 2014);
    private final List<Float> expectedLengths = List.of(2.4F, 2.9F, 2.3F, 2.5F, 2.8F);
    
    private void configureMovieMock() {
        when(movie.getName()).thenReturn("The Shawshank Redemption", "The Godfather", 
                "The Matrix", "Inception", "Interstellar");
        when(movie.getYearOfRelease()).thenReturn(1994, 1972, 1999, 2010, 2014);
        when(movie.getHourLength()).thenReturn(2.4F, 2.9F, 2.3F, 2.5F, 2.8F);
    }
    
    private FromFileFiller createFiller(String filepath) {
        URL resource = getClass().getClassLoader().getResource(filepath);
        requireNonNull(resource, "resource must be non null to save");
        try {
            return new FromFileFiller(Paths.get(resource.toURI()).toString());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Cannot load test resource: " + filepath, e);
        }
    }
    
    private FromFileFiller createFillerWithMockedBuilder(String filepath) {
        try (MockedConstruction<Movie.Builder> mockBuilder = mockConstruction(Movie.Builder.class, 
                    withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS))) {
            FromFileFiller fff = createFiller(filepath);
            
            Movie.Builder builder = mockBuilder.constructed().get(0);
            for (int i = 0; i < 5; ++i)
                when(builder.withName(expectedNames.get(i))
                        .withYearOfRelease(expectedYears.get(i))
                        .withHourLength(expectedLengths.get(i))
                        .build())
                        .thenReturn(movie);
            return fff;
        }
    }

    private void fillMovies(FromFileFiller fff, Collection<Movie> movies) {
        try (MockedStatic<MovieInputValidation> validation = mockStatic(MovieInputValidation.class)) {
            validation.when(() -> MovieInputValidation.validateName(anyString()))
                    .thenAnswer(i -> Optional.of(i.getArgument(0)));
            validation.when(() -> MovieInputValidation.validateYearOfRelease(anyString()))
                    .thenAnswer(i -> Optional.of(Integer.valueOf(i.getArgument(0))));
            validation.when(() -> MovieInputValidation.validateYearOfRelease("qwerty")).thenReturn(Optional.empty());
            validation.when(() -> MovieInputValidation.validateHourLength(anyString()))
                    .thenAnswer(i -> Optional.of(Float.valueOf(i.getArgument(0))));
            validation.when(() -> MovieInputValidation.validateHourLength("qwerty")).thenReturn(Optional.empty());
            
            fff.fillMovies(movies);
        }
    }

    @Test
    void testCreateFromFileFillerWithNullFilepath() {
        System.out.println("FromFileFiller with null filepath");
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new FromFileFiller(null));
        assertEquals(FromFileFiller.FILEPATH_NULL_MESSAGE, exception.getMessage());
    }

    @Test
    @SuppressWarnings("ThrowableResultIgnored")
    public void testCreateFromFileFillerWithInvalidPath() {
        System.out.println("FromFileFiller with invalid path");
        assertThrows(InvalidPathException.class, () -> new FromFileFiller("Name:InvalidFile.txt"));
    }
    
    @Test
    void testCreateFromFileFillerWhenFileExistsNot() {
        System.out.println("FromFileFiller when file exists not");
        String filepath = "NotExistFile.txt";
        var thrown = assertThrows(IllegalArgumentException.class, () -> new FromFileFiller(filepath));
        assertEquals(FromFileFiller.FILE_EXISTS_NOT_MESSAGE, thrown.getMessage());
    }

    @Test
    void testFillMoviesCorrect() {
        configureMovieMock();
        System.out.println("fillMovies correct");
        
        FromFileFiller fff = createFillerWithMockedBuilder("correctMovies.txt");
        List<Movie> movies = new ArrayList<>();
        fillMovies(fff, movies);

        assertEquals(5, movies.size());
        assertEquals(expectedNames, movies.stream().map(Movie::getName).toList());
        assertEquals(expectedYears, movies.stream().map(Movie::getYearOfRelease).toList());
        assertEquals(expectedLengths, movies.stream().map(Movie::getHourLength).toList());
    }

    @Test
    void testFillMoviesWhenEmptyFile() {
        System.out.println("fillMovies when empty file");
        FromFileFiller fff = createFiller("emptyFile.txt");
        Collection<Movie> movies = new ArrayList<>();
        fff.fillMovies(movies);
        assertTrue(movies.isEmpty());
    }

    @Test
    void testFillMoviesWhenFileHasExtraFields() {
        System.out.println("fillMovies when file has extra fields");
        String line = "The Shawshank Redemption;1994;2.4;1972";
        FromFileFiller fff = createFiller("extraField.txt");
        Collection<Movie> movies = new ArrayList<>();
        var thrown = assertThrows(IllegalArgumentException.class, () -> fillMovies(fff, movies));
        assertEquals(String.format(FromFileFiller.INVALID_LINE_FORMAT_MESSAGE_FORMAT, line), thrown.getMessage());
    }

    @Test
    void testFillMoviesWhenFileMissingFields() {
        System.out.println("fillMovies when file missing fields");
        String line = "The Shawshank Redemption;1994";
        FromFileFiller fff = createFiller("missingField.txt");
        Collection<Movie> movies = new ArrayList<>();
        var thrown = assertThrows(IllegalArgumentException.class, () -> fillMovies(fff, movies));
        assertEquals(String.format(FromFileFiller.INVALID_LINE_FORMAT_MESSAGE_FORMAT, line), thrown.getMessage());
    }

    @Test
    void testFillMoviesWhenFileHasInvalidSecondField() {
        System.out.println("fillMovies when file has invalid second field");
        String line = "qwerty";
        FromFileFiller fff = createFiller("invalidSecondField.txt");
        Collection<Movie> movies = new ArrayList<>();
        var thrown = assertThrows(IllegalArgumentException.class, () -> fillMovies(fff, movies));
        assertEquals(String.format(FromFileFiller.INVALID_VALUE_MESSAGE_FORMAT, line), thrown.getMessage());
    }

    @Test
    void testFillMoviesWhenFileHasInvalidThirdField() {
        System.out.println("fillMovies when file has invalid third field");
        String line = "qwerty";
        FromFileFiller fff = createFiller("invalidThirdField.txt");
        Collection<Movie> movies = new ArrayList<>();
        var thrown = assertThrows(IllegalArgumentException.class, () -> fillMovies(fff, movies));
        assertEquals(String.format(FromFileFiller.INVALID_VALUE_MESSAGE_FORMAT, line), thrown.getMessage());
    }

    @Test
    void testFillMoviesGivenNullMovies() {
        System.out.println("fillMovies given null movies");
        FromFileFiller fff = createFiller("correctMovies.txt");
        var thrown = assertThrows(NullPointerException.class, () -> fillMovies(fff, null));
        assertEquals(FromFileFiller.COLLECTION_NULL_MESSAGE, thrown.getMessage());
    }
}
