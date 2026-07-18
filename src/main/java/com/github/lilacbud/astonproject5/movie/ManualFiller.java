package com.github.lilacbud.astonproject5.movie;

import java.util.Collection;
import static java.util.Objects.requireNonNull;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

public class ManualFiller implements MoviesFiller {
    private final int size;
    private final Scanner scanner;
    private final Optional<Prompts> prompts;
    private final Movie.Builder builder = new Movie.Builder();

    public ManualFiller(int size, Scanner scanner, Prompts prompts) {
        if (size < 0) {
            throw new IllegalArgumentException("Size cannot be negative");
        }
        this.size = size;
        this.scanner = requireNonNull(scanner, "Scanner cannot be null");
        this.prompts = Optional.ofNullable(prompts);
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
    
    private String askMovieName() {
        Optional<String> validatedName;
        do {
            prompts.ifPresent((p) -> System.out.print(p.movieNamePrompt));
            validatedName = MovieInputValidation.validateName(scanner.nextLine());
        }
        while (validatedName.isEmpty());
        return validatedName.get();
    }
    
    private int askMovieYearOfRelease() {
        Optional<Integer> validatedYear;
        do {
            prompts.ifPresent((p) -> System.out.print(p.movieYearPrompt));
            validatedYear = MovieInputValidation.validateYearOfRelease(scanner.nextLine());
        }
        while (validatedYear.isEmpty());
        return validatedYear.get();
    }
    
    private float askMovieHourLength() {
        Optional<Float> validatedHour;
        do {
            prompts.ifPresent((p) -> System.out.print(p.movieHourLengthPrompt));
            validatedHour = MovieInputValidation.validateHourLength(scanner.nextLine());
        }
        while (validatedHour.isEmpty());
        return validatedHour.get();
    }
    
    public static record Prompts(String movieNamePrompt, String movieYearPrompt, String movieHourLengthPrompt) {
        public Prompts(String movieNamePrompt, String movieYearPrompt, String movieHourLengthPrompt) {
            this.movieNamePrompt = validatePrompt(movieNamePrompt);
            this.movieYearPrompt = validatePrompt(movieYearPrompt);
            this.movieHourLengthPrompt = validatePrompt(movieHourLengthPrompt);
        }
        
        private String validatePrompt(String prompt) {
            return requireNonNull(prompt, "Prompt must not be null");
        }
    }
}
