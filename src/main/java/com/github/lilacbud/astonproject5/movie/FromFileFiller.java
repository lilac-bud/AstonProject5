package com.github.lilacbud.astonproject5.movie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Stream;

public class FromFileFiller implements MoviesFiller{

    private final Path path;

    public FromFileFiller(String filepath){

        Path path = Paths.get(filepath);

        if (!Files.exists(path)) {
            throw new IllegalArgumentException("File does not exist: " + filepath);
        }
        if (!Files.isReadable(path)) {
            throw new IllegalArgumentException("File is not readable: " + filepath);
        }

        this.path = path;
    }

    @Override
    public void fillMovies(Collection<Movie> movies){
        try (Stream<String> lines = Files.lines(path)) {
            lines.map(FromFileFiller::parseMovie)
                    .forEach(movies::add);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load movies from file: " + path, e);
        }
    }

    private static Movie parseMovie(String line)
    {
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

        return new Movie.Builder()
                .withName(name)
                .withYearOfRelease(yearOfRelease)
                .withHourLength(hourLength)
                .build();
    }
}
