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
    private final static String LINE_SEPARATOR = ";";
    private final static int ARGS_NUMBER = 3;
    private final Path path;
    private final Movie.Builder builder = new Movie.Builder();

    public FromFileFiller(String filepath ) {
        path = Paths.get(requireNonNull(filepath, "Filepath must not be null"));
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("File does not exist: " + filepath);
        }
        if (!Files.isReadable(path)) {
            throw new IllegalArgumentException("File is not readable: " + filepath);
        }
    }

    @Override
    public void fillMovies(Collection<Movie> movies) {
        requireNonNull(movies, "Movies must not be null").clear();
        try (Stream<String> lines = Files.lines(path)) {
            lines.map(this::parseMovie).forEach(movies::add);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load movies from file: " + path, e);
        }
    }

    private Movie parseMovie(String line) {
        final String[] arrStrings = line.split(LINE_SEPARATOR);
        if (arrStrings.length != ARGS_NUMBER) {
            throw new IllegalArgumentException("Invalid string format: " + line);
        }
        String name = MovieInputValidation.validateName(arrStrings[0])
                .orElseThrow(() -> new IllegalArgumentException("Invalid name: " + arrStrings[0]));
        int yearOfRelease = MovieInputValidation.validateYearOfRelease(arrStrings[1])
                .orElseThrow(() -> new IllegalArgumentException("Invalid year: " + arrStrings[1]));
        float hourLength = MovieInputValidation.validateHourLength(arrStrings[2])
                .orElseThrow(() -> new IllegalArgumentException("Invalid hour: " + arrStrings[2]));
        return builder
                .withName(name)
                .withYearOfRelease(yearOfRelease)
                .withHourLength(hourLength)
                .build();
    }
}
