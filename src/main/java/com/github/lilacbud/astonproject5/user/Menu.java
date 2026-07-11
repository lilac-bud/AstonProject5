package com.github.lilacbud.astonproject5.user;

import com.github.lilacbud.astonproject5.movie.FromFileFiller;
import com.github.lilacbud.astonproject5.movie.*;
import com.github.lilacbud.astonproject5.movie.save.*;
import com.github.lilacbud.astonproject5.movie.sort.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Menu {
    private static Menu instance;
    private boolean running = true;
    private final List<Movie> movies = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private FillMenu fillMenu;
    private SortMenu sortMenu;
    
    private final List<MenuOption> mainOptions = List.of(
            new MenuOption("Заполнить список фильмов", () -> {
                getFillMenu().run();
            }),
            new MenuOption("Вывести список фильмов на экран", () -> {
                if (movies.isEmpty())
                    getFillMenu().run();
                printMovies();
            }),
            new MenuOption("Отсортировать список фильмов", () -> {
                if (movies.isEmpty())
                    getFillMenu().run();
                getSortMenu().run();
            }),
            new MenuOption("Сохранить фильмы", () -> {
                new DefaultSaver(getFilepath()).save(movies);
            }),
            new MenuOption("Закончить работу", () -> {
                running = false;
            })
    );
    
    private Menu(){
    }
    private FillMenu getFillMenu() {
        if (fillMenu == null)
            fillMenu = new FillMenu();
        return fillMenu;
    }
    private SortMenu getSortMenu() {
        if (sortMenu == null)
            sortMenu = new SortMenu();
        return sortMenu;
    }
    private void printMovies() {
        movies.forEach(System.out::println);
    }
    
    public static Menu getInstance(){
        if (instance == null)
            instance = new Menu();
        return instance;
    }
    public void run(){
        while (running)
            chooseOption("Главное меню: ", mainOptions).execute();
    }
    
    private static final class InputValidation {
        private InputValidation() {
            throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
        }
        private static Optional<String> validateInput(String input) {
            if (input == null || input.isBlank()) {
                System.err.println("Input cannot be empty");
                return Optional.empty();
            } 
            return Optional.of(input.trim());
        }
        private static Optional<Integer> validateIntegerInput(String input) {
            final Optional<String> validatedInput = validateInput(input);
            if (validatedInput.isEmpty())
                return Optional.empty();
            try {
                final Integer convertedInput = Integer.valueOf(validatedInput.get());
                if (convertedInput < 0)
                    throw new IllegalArgumentException();
                return Optional.of(convertedInput);
            } catch (NumberFormatException e) {
                System.err.println("Input must be a whole number");
            } catch (IllegalArgumentException e) {
                System.err.println("Input cannot be negative");
            }
            return Optional.empty();
        }
    }
    private String getFilepath() {
            Optional<String> validatedFilepath;
            do {
                System.out.print("Укажите путь к файлу: ");
                validatedFilepath = InputValidation.validateInput(scanner.nextLine());
            } while (validatedFilepath.isEmpty());
            return validatedFilepath.get();
        }
    
    private interface MenuCommand {
        void execute();
    }
    private class MenuOption {
        private final String title;
        private final MenuCommand command;
        
        private MenuOption(String title, MenuCommand command) {
            this.title = title;
            this.command = command;
        }
        private String getTitle() {
            return title;
        }
        private void execute() {
            command.execute();
        }
    }
    private MenuOption chooseOption(String title, List<MenuOption> options) {
        Optional<Integer> validatedInput;
        while (true) {
            do {
                System.out.println(title);
                IntStream.range(0, options.size())
                         .mapToObj(i -> String.format("%d. %s", i + 1, options.get(i).getTitle()))
                         .forEach(System.out::println);
                System.out.print("Выберите одну из опций: ");
                validatedInput = InputValidation.validateIntegerInput(scanner.nextLine());
            } while (validatedInput.isEmpty());
            try {
                return options.get(validatedInput.get() - 1);
            } catch (IndexOutOfBoundsException e) {
                System.err.println("No such option");
            }
        }
    }
    
    private class FillMenu {
        private MoviesFiller filler;
        
        private final List<MenuOption> fillOptions = List.of(
                new MenuOption("Из файла", () -> {
                    filler = new FromFileFiller(getFilepath());
                }),
                new MenuOption("Случайно", () -> {
                    filler = new RandomFiller(getSize());
                }),
                new MenuOption("Вручную", () -> {
                    filler = new ManualFiller(getSize());
                })
        );
        
        private int getSize() {
            Optional<Integer> validatedSize;
            do {
                System.out.print("Укажите количество фильмов: ");
                validatedSize = InputValidation.validateIntegerInput(scanner.nextLine());
            } while (validatedSize.isEmpty());
            return validatedSize.get();
        }
        private void chooseFiller() {
            while (filler == null) {
                try {
                    chooseOption("Как заполнить список:", fillOptions).execute();
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                    filler = null;
                }
            }
        }
        private void run() {
            while (true) {
                chooseFiller();
                try {
                    filler.fillMovies(movies);
                } catch (RuntimeException e) {
                    System.err.println(e.getMessage());
                    continue;
                }
                System.out.println("Список успешно заполнен");
                break;
            }
        }
    }
    
    private class SortMenu {
        private MoviesSorter sorter;
        private final SortingStrategy sortStrategy = (movieList, comp) -> {
                    ((List<Movie>)movieList).sort(comp);
                };
        private String curComp;
        
        private final List<MenuOption> compOptions = List.of(
                new MenuOption("По названию", () -> {
                    setComparator(Movie.compareByName);
                }),
                new MenuOption("По году выпуска", () -> {
                    setComparator(Movie.compareByYearOfRelease);
                }),
                new MenuOption("По длительности", () -> {
                    setComparator(Movie.compareByHourLength);
                })
        );
        private final List<MenuOption> changeCompOptions = List.of(
                new MenuOption("Да", () -> chooseComparator()),
                new MenuOption("Нет", () -> {})
        );
        
        private void setComparator(Comparator<Movie> comp) {
            if (sorter == null)
                sorter = new MoviesSorter(sortStrategy, comp);
            else
                sorter.setComparator(comp);
        }
        private void chooseComparator() {
            MenuOption compOption = chooseOption("Отсортировать список фильмов: ", compOptions);
            curComp = compOption.getTitle().toLowerCase();
            compOption.execute();
        }
        private void chooseChangingComparator() {
            String title = String.format("Сейчас фильмы сортируются %s. Поменять?", curComp);
            chooseOption(title, changeCompOptions).execute();
        }
        private void run() {
            if (sorter == null)
                chooseComparator();
            else
                chooseChangingComparator();
            sorter.performSorting(movies);
            System.out.println("Список успешно отсортирован");
        }
    }
}
