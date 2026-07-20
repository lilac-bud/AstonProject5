package com.github.lilacbud.astonproject5.movie;

import com.github.lilacbud.astonproject5.app.MoviesFiller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.Optional;
import java.util.function.Supplier;
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
        final List<String> args = Arrays.asList(line.split(LINE_SEPARATOR));
        if (args.size() != ARGS_NUMBER) {
            throw new IllegalArgumentException(String.format(INVALID_LINE_FORMAT_MESSAGE_FORMAT, line));
        }
        String name = parseValue(String.format(INVALID_VALUE_MESSAGE_FORMAT, args.get(0)), 
                () -> MovieInputValidation.validateName(args.get(0)));
        int yearOfRelease = parseValue(String.format(INVALID_VALUE_MESSAGE_FORMAT, args.get(1)), 
                () -> MovieInputValidation.validateYearOfRelease(args.get(1)));
        float hourLength = parseValue(String.format(INVALID_VALUE_MESSAGE_FORMAT, args.get(2)), 
                () -> MovieInputValidation.validateHourLength(args.get(2)));
        return builder
                .withName(name)
                .withYearOfRelease(yearOfRelease)
                .withHourLength(hourLength)
                .build();
    }
    
    private <T> T parseValue(String exceptionMesssage, Supplier<Optional<T>> validator) {
        return validator.get().orElseThrow(() -> new IllegalArgumentException(exceptionMesssage));
    }
}
