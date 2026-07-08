package com.github.lilacbud.astonproject5.movie;

import java.util.Collection;
import java.util.Optional;
import java.util.Scanner;

public class ManualFiller implements MoviesFiller{
    private final int size;

    public ManualFiller(int size){
        this.size = size;
    }

    @Override
    public void fillMovies(Collection<Movie> movies){
        System.out.println("ЗАПОЛНЕНИЕ СПИСКА ФИЛЬМОВ ВРУЧНУЮ");
        Scanner scanner = new Scanner(System.in);

        int i = 1;
        while (i <= size) {
            System.out.print(String.format("Введите название фильма №%d: ", i));
            String name = scanner.nextLine();
            if (MovieInputValidation.validateName(name).isEmpty()) //если не прошли валидацию, пропускаем ввод этого фильма
                continue;

            System.out.print("Введите год выпуска: ");
            int yearOfRelease;
            String yearStr = scanner.nextLine();
            Optional<Integer> verifiedYear = MovieInputValidation.validateYearOfRelease(yearStr);
            if (verifiedYear.isEmpty())
                continue;
            else
                yearOfRelease = verifiedYear.get();

            System.out.print("Введите продолжительность фильма: ");
            float hourLength;
            String hourStr = scanner.nextLine();
            Optional<Float> verifiedHour = MovieInputValidation.validateHourLength(hourStr);
            if (verifiedHour.isEmpty())
                continue;
            else
                hourLength = verifiedHour.get();

            Movie.Builder builder = new Movie.Builder();
            Movie movie = builder.withName(name)
                    .withYearOfRelease(yearOfRelease)
                    .withHourLength(hourLength)
                    .build();

            movies.add(movie);

            System.out.println("Фильм успешно добавлен");
            i++;
        }

        scanner.close();

        System.out.println("\nСПИСОК ДОБАВЛЕННЫХ ФИЛЬМОВ");

        movies.stream().forEach(System.out::println);
    }
}
