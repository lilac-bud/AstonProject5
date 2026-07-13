package com.github.lilacbud.astonproject5.user;

import com.github.lilacbud.astonproject5.movie.*;
import com.github.lilacbud.astonproject5.movie.save.*;
import com.github.lilacbud.astonproject5.movie.sort.*;
import com.github.lilacbud.astonproject5.util.InputValidation;
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
    private MoviesFiller filler;
    private MoviesSorter sorter;
    private final SortingStrategy sortStrategy = new MergeSort();
    
    private final SubMenu mainMenu = new SubMenu("Главное меню:",
            List.of(
                    new MenuOption("Заполнить список фильмов", () -> Menu.getInstance().fillMovies()),
                    new MenuOption("Вывести список фильмов на экран", () -> {
                        Menu.getInstance().checkIfMoviesEmpty();
                        Menu.getInstance().printMovies();
                    }),
                    new MenuOption("Отсортировать список фильмов", () -> {
                        Menu.getInstance().checkIfMoviesEmpty();
                        Menu.getInstance().sortMovies();
                    }),
                    new MenuOption("Сохранить фильмы", () -> {
                        Menu.getInstance().checkIfMoviesEmpty();
                        Menu.getInstance().saveMovies();
                    }),
                    new MenuOption("Закончить работу", () -> Menu.getInstance().exit())  
            )
    );
    private final SubMenu fillMenu = new SubMenu("Как заполнить список:",
            List.of(
                    new MenuOption("Из файла", () -> {
                        MoviesFiller filler = new FromFileFiller(Menu.getInstance().getFilepath());
                        Menu.getInstance().setFiller(filler);
                    }),
                    new MenuOption("Случайно", () -> {
                        MoviesFiller filler = new RandomFiller(Menu.getInstance().getSize());
                        Menu.getInstance().setFiller(filler);
                    }),
                    new MenuOption("Вручную", () -> {
                        MoviesFiller filler = new ManualFiller(Menu.getInstance().getSize());
                        Menu.getInstance().setFiller(filler);
                    })
            )
    );
    private final SubMenu changeCompMenu = new SubMenu("",
            List.of(
                    new MenuOption("Да", () -> Menu.getInstance().chooseComparator()),
                    new MenuOption("Нет", () -> {})
            )
    );
    private final SubMenu compMenu = new SubMenu("Отсортировать список фильмов:",
            List.of(
                    new MenuOption("По названию", () -> Menu.getInstance().setComparator(Movie.compareByName)),
                    new MenuOption("По году выпуска", () -> 
                            Menu.getInstance().setComparator(Movie.compareByYearOfRelease)),
                    new MenuOption("По длительности", () -> Menu.getInstance().setComparator(Movie.compareByHourLength))
            )
    );
    
    private Menu() {}
    public static Menu getInstance(){
        if (instance == null)
            instance = new Menu();
        return instance;
    }
    public void run(){
        while (running)
            mainMenu.chooseOption(scanner).execute();
    }
    
    private void setFiller(MoviesFiller filler) {
        this.filler = filler;
    }
    private void setComparator(Comparator<Movie> comp) {
        if (sorter == null)
            sorter = new MoviesSorter(sortStrategy, comp);
        else
            sorter.setComparator(comp);
    }
    private String getFilepath() {
        Optional<String> validatedFilepath;
        do {
            System.out.print("Укажите путь к файлу: ");
            validatedFilepath = InputValidation.validateInput(scanner.nextLine());
        } while (validatedFilepath.isEmpty());
        return validatedFilepath.get();
    }
    private int getSize() {
        Optional<Integer> validatedSize;
        do {
            System.out.print("Укажите количество фильмов: ");
            validatedSize = InputValidation.validateIntegerInput(scanner.nextLine());
        } while (validatedSize.isEmpty());
        return validatedSize.get();
    }
    private void printMovies() {
        movies.forEach(System.out::println);
    }
    private void saveMovies() {
        try {
            new DefaultSaver(getFilepath(), scanner).save(movies);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
    private void fillMovies() {
        while (true) {
            try {
                fillMenu.chooseOption(scanner).execute();
                filler.fillMovies(movies);
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                continue;
            }
            System.out.println("Список успешно заполнен");
            break;
        }
    }
    private void checkIfMoviesEmpty() {
        if (movies.isEmpty())
            fillMovies();
    }
    private void sortMovies() {
        if (sorter == null)
            chooseComparator();
        else
            changeCompMenu.chooseOption(scanner).execute();
        sorter.performSorting(movies);
        System.out.println("Список успешно отсортирован");
    }
    private void chooseComparator() {
        MenuOption compOption = compMenu.chooseOption(scanner);
        String title = String.format("Сейчас фильмы сортируются %s. Поменять?", compOption.getTitle().toLowerCase());
        changeCompMenu.setTitle(title);
        compOption.execute();
    }
    private void exit() {
        running = false;
    }
    
    private static interface MenuCommand {
        void execute();
    }
    private static class MenuOption {
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
    private static class SubMenu {
        private String title;
        private final List<MenuOption> options;
        
        private SubMenu(String title, List<MenuOption> options) {
            this.title = title;
            this.options = options;
        }
        private void setTitle(String title) {
            this.title = title;
        }
        private MenuOption chooseOption(Scanner scanner) {
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
    }
}
