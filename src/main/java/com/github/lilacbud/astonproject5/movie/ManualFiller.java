package com.github.lilacbud.astonproject5.movie;

import com.github.lilacbud.astonproject5.app.MoviesFiller;
import java.util.Collection;
import java.util.Objects;
import static java.util.Objects.requireNonNull;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ManualFiller implements MoviesFiller {
    private final int size;
    private final Scanner scanner;
    private final Prompts prompts;
    private final Movie.Builder builder = new Movie.Builder();

    public ManualFiller(int size, Scanner scanner, Prompts prompts) {
        if (size < 0) {
            throw new IllegalArgumentException("Size cannot be negative");
        }
        this.size = size;
        this.scanner = requireNonNull(scanner, "Scanner cannot be null");
        this.prompts = Objects.requireNonNullElse(prompts, new Prompts(null, null, null));
    }

    @Override
    public void fillMovies(Collection<Movie> movies) {
        requireNonNull(movies, "Movies must not be null").clear();
        Stream.generate(this::createMovie)
                .limit(size)
                .forEach(movies::add);
    }
    
    private Movie createMovie() {
        return builder
                .withName(askMovieName())
                .withYearOfRelease(askMovieYearOfRelease())
                .withHourLength(askMovieHourLength())
                .build();
    }
    
    private <T> T askValue(String prompt, Supplier<Optional<T>> validator) {
        Optional<T> validatedValue;
        do {
            if (prompt != null)
                System.out.print(prompt);
            validatedValue = validator.get();
        }
        while (validatedValue.isEmpty());
        return validatedValue.get();
    }
    
    private String askMovieName() {
        return askValue(prompts.movieNamePrompt, () 
                -> MovieInputValidation.validateName(scanner.nextLine()));
    }
    
    private int askMovieYearOfRelease() {
        return askValue(prompts.movieYearPrompt, () 
                -> MovieInputValidation.validateYearOfRelease(scanner.nextLine()));
    }
    
    private float askMovieHourLength() {
        return askValue(prompts.movieHourLengthPrompt, () 
                -> MovieInputValidation.validateHourLength(scanner.nextLine()));
    }
    
    public static record Prompts(String movieNamePrompt, String movieYearPrompt, String movieHourLengthPrompt) {
    }
}
