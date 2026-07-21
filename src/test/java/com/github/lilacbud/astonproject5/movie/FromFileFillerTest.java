package com.github.lilacbud.astonproject5.movie;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
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
    
    private FromFileFiller createFiller(String filename) throws URISyntaxException {
        return new FromFileFiller(getPath(filename).toString());
    }
    
    private Path getPath(String filename) throws URISyntaxException {
        URL resource = getClass().getResource(filename);
        return Paths.get(requireNonNull(resource).toURI());
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
    public void givenNullAsFilepath_whenCreatingFromFileFiller_thenThrow() {
        final var thrown = assertThrows(NullPointerException.class, () -> new FromFileFiller(null));
        assertEquals(FromFileFiller.FILEPATH_NULL_MESSAGE, thrown.getMessage());
    }

    @Test
    @SuppressWarnings("ThrowableResultIgnored")
    public void givenInvalidPath_whenCreatingFromFileFiller_thenThrow() {
        final String filename = "Name:InvalidFile.txt";
        assertThrows(InvalidPathException.class, () -> new FromFileFiller(filename));
    }
    
    @Test
    public void givenThatFileExistsNot_whenCreatingFromFileFiller_thenThrow() {
        final String filename = "NotExistFile.txt";
        final var thrown = assertThrows(IllegalArgumentException.class, () -> new FromFileFiller(filename));
        assertEquals(FromFileFiller.FILE_EXISTS_NOT_MESSAGE, thrown.getMessage());
    }

    @Test
    public void givenThatFileIsCorrect_whenFillingCollection_thenCollectionShouldBeFilled() 
            throws IOException, URISyntaxException {
        final String filename = "correctMovies.txt";
        final List<Movie> movies = new ArrayList<>();
        final List<String> expectedNames = new ArrayList<>();
        final List<Integer> expectedYears = new ArrayList<>();
        final List<Float> expectedLengths = new ArrayList<>();
        Files.lines(getPath(filename))
                .map(line -> line.split(";"))
                .forEach(args -> {
                    expectedNames.add(args[0]);
                    expectedYears.add(Integer.valueOf(args[1]));
                    expectedLengths.add(Float.valueOf(args[2]));
                });
        when(movie.getName()).thenAnswer(AdditionalAnswers.returnsElementsOf(expectedNames));
        when(movie.getYearOfRelease()).thenAnswer(AdditionalAnswers.returnsElementsOf(expectedYears));
        when(movie.getHourLength()).thenAnswer(AdditionalAnswers.returnsElementsOf(expectedLengths));
        try (MockedConstruction<Movie.Builder> mockBuilder = mockConstruction(Movie.Builder.class, 
                    withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS))) {
            final FromFileFiller fff = createFiller(filename);
            Movie.Builder builder = mockBuilder.constructed().get(0);
            for (int i = 0; i < 5; ++i)
                when(builder.withName(expectedNames.get(i))
                        .withYearOfRelease(expectedYears.get(i))
                        .withHourLength(expectedLengths.get(i))
                        .build())
                        .thenReturn(movie);
            fillMovies(fff, movies);
        }
        assertEquals(expectedNames.size(), movies.size());
        assertEquals(expectedNames, movies.stream().map(Movie::getName).toList());
        assertEquals(expectedYears, movies.stream().map(Movie::getYearOfRelease).toList());
        assertEquals(expectedLengths, movies.stream().map(Movie::getHourLength).toList());
    }

    @Test
    public void givenThatFileIsEmpty_whenFillingCollection_thenCollectionShouldBeEmpty() throws URISyntaxException {
        final String filename = "emptyFile.txt";
        final FromFileFiller fff = createFiller(filename);
        final Collection<Movie> movies = new ArrayList<>();
        fff.fillMovies(movies);
        assertTrue(movies.isEmpty());
    }

    @Test
    public void givenThatFileHasExtraFields_whenFillingCollection_thenThrow() 
            throws URISyntaxException, IOException {
        final String filename = "extraField.txt";
        final String line = Files.readAllLines(getPath(filename)).get(0);
        final FromFileFiller fff = createFiller(filename);
        final Collection<Movie> movies = new ArrayList<>();
        final var thrown = assertThrows(IllegalArgumentException.class, () -> fillMovies(fff, movies));
        assertEquals(String.format(FromFileFiller.INVALID_LINE_FORMAT_MESSAGE_FORMAT, line), thrown.getMessage());
    }

    @Test
    public void givenThatFileHasMissingFields_whenFillingCollection_thenThrow() 
            throws URISyntaxException, IOException {
        final String filename = "missingField.txt";
        final String line = Files.readAllLines(getPath(filename)).get(0);
        final FromFileFiller fff = createFiller(filename);
        final Collection<Movie> movies = new ArrayList<>();
        final var thrown = assertThrows(IllegalArgumentException.class, () -> fillMovies(fff, movies));
        assertEquals(String.format(FromFileFiller.INVALID_LINE_FORMAT_MESSAGE_FORMAT, line), thrown.getMessage());
    }

    @Test
    public void givenThatFileHasInvalidSecondField_whenFillingCollection_thenThrow() 
            throws URISyntaxException, IOException {
        final String filename = "invalidSecondField.txt";
        final String line = Files.readAllLines(getPath(filename)).get(0);
        final FromFileFiller fff = createFiller(filename);
        final Collection<Movie> movies = new ArrayList<>();
        final var thrown = assertThrows(IllegalArgumentException.class, () -> fillMovies(fff, movies));
        assertEquals(String.format(FromFileFiller.INVALID_VALUE_MESSAGE_FORMAT, line), thrown.getMessage());
    }

    @Test
    public void givenThatFileHasInvalidThirdField_whenFillingCollection_thenThrow() 
            throws URISyntaxException, IOException {
        final String filename = "invalidThirdField.txt";
        final String line = Files.readAllLines(getPath(filename)).get(0);
        final FromFileFiller fff = createFiller(filename);
        final Collection<Movie> movies = new ArrayList<>();
        final var thrown = assertThrows(IllegalArgumentException.class, () -> fillMovies(fff, movies));
        assertEquals(String.format(FromFileFiller.INVALID_VALUE_MESSAGE_FORMAT, line), thrown.getMessage());
    }

    @Test
    public void givenNullAsCollection_whenFillingCollection_thenThrow() throws URISyntaxException {
        final String filename = "correctMovies.txt";
        final FromFileFiller fff = createFiller(filename);
        final var thrown = assertThrows(NullPointerException.class, () -> fillMovies(fff, null));
        assertEquals(FromFileFiller.COLLECTION_NULL_MESSAGE, thrown.getMessage());
    }
}
