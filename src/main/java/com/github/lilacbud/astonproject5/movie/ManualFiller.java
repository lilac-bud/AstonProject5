package com.github.lilacbud.astonproject5.movie;

import java.util.Collection;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

//ЗАПОЛНЕНИЕ СПИСКА ФИЛЬМОВ ВРУЧНУЮ
public class ManualFiller implements MoviesFiller {
    private final int size;
    private final Scanner scanner;
    private final Prompts prompts = 
            new Prompts("Введите название фильма: ", "Введите год выпуска: ", "Введите продолжительность фильма: ");

    public ManualFiller(int size, Scanner scanner) {
        if (size < 0)
            throw new IllegalArgumentException("Size cannot be negative");
        this.size = size;
        this.scanner = requireNonNull(scanner, "Scanner cannot be null");
    }

    @Override
    public void fillMovies(Collection<Movie> movies) {

        requireNonNull(movies, "Collection<Movie> movies must be non null to sort");

        movies.clear();
        IntStream.range(0, size) //создаем поток чисел от 0 до size-1
                 .mapToObj(i -> this.fill()) //заменяем каждое число на результат вызова метода
                 .forEach(movies::add); //добавляем фильм в коллекцию

        //movies.stream().forEach(System.out::println);
    }

    //получаем данные о фильме
    private Movie fill() {
        //получаем название фильма
        String name; //название фильма
        Optional<String> verifiedName; //результат метода валидации
        do {
            System.out.print(prompts.movieNamePrompt);
            name = scanner.nextLine();
            verifiedName = MovieInputValidation.validateName(name);
        }
        while (verifiedName.isEmpty()); //пока не будет введено корректное название
        name = verifiedName.get();

        //получаем год выпуска
        int yearOfRelease; //год выпуска
        String yearStr; //год выпуска в строковом представлении
        Optional<Integer> verifiedYear; //результат метода валидации
        do {
            System.out.print(prompts.movieYearPrompt);
            yearStr = scanner.nextLine();
            verifiedYear = MovieInputValidation.validateYearOfRelease(yearStr);
        }
        while (verifiedYear.isEmpty());
        yearOfRelease = verifiedYear.get();

        //получаем продолжительность
        float hourLength; //продолжительность
        String hourStr; //продолжительность в строковом представлении
        Optional<Float> verifiedHour; //результат метода валидации
        do {
            System.out.print(prompts.movieHourLengthPrompt);
            hourStr = scanner.nextLine();
            verifiedHour = MovieInputValidation.validateHourLength(hourStr);
        }
        while (verifiedHour.isEmpty());
        hourLength = verifiedHour.get();

        //создаем объект
        return new Movie.Builder().withName(name)
                .withYearOfRelease(yearOfRelease)
                .withHourLength(hourLength)
                .build();
    }
    
    public static class Prompts {
        private final String movieNamePrompt;
        private final String movieYearPrompt;
        private final String movieHourLengthPrompt;
        
        public Prompts(String movieNamePrompt, String movieYearPrompt, String movieHourLengthPrompt) {
            this.movieNamePrompt = movieNamePrompt;
            this.movieYearPrompt = movieYearPrompt;
            this.movieHourLengthPrompt = movieHourLengthPrompt;
        }
    }
}
