package com.github.lilacbud.astonproject5.movie;

import java.util.Collection;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

public class ManualFiller implements MoviesFiller {
    private final int size;
    private final Scanner scanner;

    public ManualFiller(int size, Scanner scanner) {
        this.size = size;
        this.scanner = scanner;
    }

    @Override
    public void fillMovies(Collection<Movie> movies) {
        System.out.println("ЗАПОЛНЕНИЕ СПИСКА ФИЛЬМОВ ВРУЧНУЮ");

        IntStream.range(0, size) //создаем поток чисел от 0 до size-1
                 .mapToObj(i -> this.fill()) //заменяем каждое число на результат вызова метода
                 .forEach(movies::add); //добавляем фильм в коллекцию

        System.out.println("\nСПИСОК ДОБАВЛЕННЫХ ФИЛЬМОВ");
        movies.stream().forEach(System.out::println);
    }

    //получаем данные о фильме
    private Movie fill() {
        //получаем название фильма
        String name;
        do {
            System.out.print("Введите название фильма: ");
            name = scanner.nextLine();
        }
        while (MovieInputValidation.validateName(name).isEmpty()); //пока не будет введено корректное название

        //получаем год выпуска
        int yearOfRelease; //год выпуска
        String yearStr; //год выпуска в строковом представлении
        Optional<Integer> verifiedYear; //результат метода валидации
        do {
            System.out.print("Введите год выпуска: ");
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
            System.out.print("Введите продолжительность фильма: ");
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
}
