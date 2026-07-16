package com.github.lilacbud.astonproject5.movie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class FromFileFiller implements MoviesFiller {

    private final Path path;
    private final Movie.Builder builder = new Movie.Builder();

    public FromFileFiller(String filepath ){

        requireNonNull(filepath, "Filepath must not be null");

        Path resultPath = Paths.get(filepath);

        if (!Files.exists(resultPath)) {
            throw new IllegalArgumentException("File does not exist: " + filepath);
        }
        if (!Files.isReadable(resultPath)) {
            throw new IllegalArgumentException("File is not readable: " + filepath);
        }

        this.path = resultPath;
    }

    @Override
    public void fillMovies(Collection<Movie> movies) {

        requireNonNull(movies, "Collection<Movie> movies must be non null to fillMovies");

        movies.clear();
        try (Stream<String> lines = Files.lines(path)) {
                    lines.map(this::parseMovie)
                    .forEach(movies::add);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load movies from file: " + path, e);
        }
    }

    private Movie parseMovie(String line) {
        String[] arrStrings = line.split(";");

        if(arrStrings.length != 3) {
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
