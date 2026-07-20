package com.github.lilacbud.astonproject5.movie;

import com.github.lilacbud.astonproject5.app.MoviesFiller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import static java.util.Objects.requireNonNull;
import java.util.stream.Stream;

public class FromFileFiller implements MoviesFiller {
    public static final String FILEPATH_NULL_MESSAGE = "Filepath must not be null";
    public static final String FILE_EXISTS_NOT_MESSAGE = "File does not exist";
    public static final String FILE_NOT_READABLE_MESSAGE = "File is not readable";
    public static final String COLLECTION_NULL_MESSAGE = "Collection must not be null";
    public static final String INVALID_LINE_FORMAT_MESSAGE_FORMAT = "Invalid line format: %s";
    public static final String INVALID_VALUE_MESSAGE_FORMAT = "Invalid value: %s";
    public static final String FILE_LOAD_FAIL_MESSAGE = "Failed to load collection from file";
    
    private static final String LINE_SEPARATOR = ";";
    private static final int ARGS_NUMBER = 3;
    private final Path path;
    private final Movie.Builder builder = new Movie.Builder();

    public FromFileFiller(String filepath ) {
        path = Paths.get(requireNonNull(filepath, FILEPATH_NULL_MESSAGE));
        if (!Files.exists(path)) {
            throw new IllegalArgumentException(FILE_EXISTS_NOT_MESSAGE);
        }
        if (!Files.isReadable(path)) {
            throw new IllegalArgumentException(FILE_NOT_READABLE_MESSAGE);
        }
    }

    @Override
    public void fillMovies(Collection<Movie> movies) {
        requireNonNull(movies, COLLECTION_NULL_MESSAGE).clear();
        try (Stream<String> lines = Files.lines(path)) {
            lines.map(this::parseMovie).forEach(movies::add);
        } catch (IOException e) {
            throw new RuntimeException(FILE_LOAD_FAIL_MESSAGE, e);
        }
    }

    private Movie parseMovie(String line) {
        final String[] arrStrings = line.split(LINE_SEPARATOR);
        if (arrStrings.length != ARGS_NUMBER) {
            throw new IllegalArgumentException(String.format(INVALID_LINE_FORMAT_MESSAGE_FORMAT, line));
        }
        String name = MovieInputValidation.validateName(arrStrings[0]).orElseThrow(() 
                -> new IllegalArgumentException(String.format(INVALID_VALUE_MESSAGE_FORMAT, arrStrings[0])));
        int yearOfRelease = MovieInputValidation.validateYearOfRelease(arrStrings[1]).orElseThrow(() 
                -> new IllegalArgumentException(String.format(INVALID_VALUE_MESSAGE_FORMAT, arrStrings[1])));
        float hourLength = MovieInputValidation.validateHourLength(arrStrings[2]).orElseThrow(() 
                -> new IllegalArgumentException(String.format(INVALID_VALUE_MESSAGE_FORMAT, arrStrings[2])));
        return builder
                .withName(name)
                .withYearOfRelease(yearOfRelease)
                .withHourLength(hourLength)
                .build();
    }
}
