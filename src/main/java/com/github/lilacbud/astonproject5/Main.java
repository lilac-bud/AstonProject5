package com.github.lilacbud.astonproject5;

import com.github.lilacbud.astonproject5.movie.FromFileFiller;
import com.github.lilacbud.astonproject5.movie.ManualFiller;
import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.movie.RandomFiller;
import com.github.lilacbud.astonproject5.movie.save.DefaultSaver;
import com.github.lilacbud.astonproject5.movie.sort.EvenNumbersSortDecorator;
import com.github.lilacbud.astonproject5.movie.sort.MergeSort;
import com.github.lilacbud.astonproject5.movie.sort.SortingStrategy;
import com.github.lilacbud.astonproject5.user.Menu;
import java.util.Scanner;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Menu mainMenu = Menu.StepBuilder.newBuilder()
                .withTitle("Главное меню:")
                .withPrompt("Выберите одну из опций: ")
                .withOption(new Menu.MenuOption("Заполнить список фильмов", () 
                        -> App.getInstance().fillMovies("Список успешно заполнен")))
                .withOption(new Menu.MenuOption("Вывести список фильмов на экран", () -> {
                    if (App.getInstance().moviesIsEmpty())
                        App.getInstance().fillMovies("Список успешно заполнен");
                    App.getInstance().printMovies("Список успешно выведен на экран");
                }))
                .withOption(new Menu.MenuOption("Отсортировать список фильмов", () -> {
                    if (App.getInstance().moviesIsEmpty())
                        App.getInstance().fillMovies("Список успешно заполнен");
                    App.getInstance().sortMovies("Список успешно отсортирован");
                }))
                .withOption(new Menu.MenuOption("Сохранить фильмы", () -> {
                    if (App.getInstance().moviesIsEmpty())
                        App.getInstance().fillMovies("Список успешно заполнен");
                    App.getInstance().saveMovies("Список успешно сохранён");
                }))
                .withOption(new Menu.MenuOption("Закончить работу", () -> App.getInstance().exit()))
                .build();
        Menu fillMenu = Menu.StepBuilder.newBuilder()
                .withTitle("Как заполнить список:")
                .withPrompt("Выберите одну из опций: ")
                .withOption(new Menu.MenuOption("Из файла", () -> {
                    String filepath = App.getInstance().getFilepath("Укажите путь к файлу: ");
                    App.getInstance().setFiller(new FromFileFiller(filepath));
                }))
                .withOption(new Menu.MenuOption("Случайно", () -> {
                    int size = App.getInstance().getSize("Укажите количество фильмов: ");
                    App.getInstance().setFiller(new RandomFiller(size));
                }))
                .withOption(new Menu.MenuOption("Вручную", () -> {
                    int size = App.getInstance().getSize("Укажите количество фильмов: ");
                    App.getInstance().setFiller(new ManualFiller(size));
                }))
                .build();
        Menu sortMenu = Menu.StepBuilder.newBuilder()
                .withTitle("Нужно отсортировать:")
                .withPrompt("Выберите одну из опций: ")
                .withOption(new Menu.MenuOption("Все фильмы", ()
                        -> App.getInstance().setSortingStrategy(new MergeSort())))
                .withOption(new Menu.MenuOption("Фильмы с чётным годом выпуска", () -> {
                    SortingStrategy sortStrategy = 
                            new EvenNumbersSortDecorator(new MergeSort(), Movie::getYearOfRelease);
                    App.getInstance().setSortingStrategy(sortStrategy);
                }))
                .build();
        Menu compMenu = Menu.StepBuilder.newBuilder()
                .withTitle("Отсортировать список фильмов:")
                .withPrompt("Выберите одну из опций: ")
                .withOption(new Menu.MenuOption("По названию", () 
                        -> App.getInstance().setComparator(Movie.compareByName)))
                .withOption(new Menu.MenuOption("По году выпуска", ()
                        -> App.getInstance().setComparator(Movie.compareByYearOfRelease)))
                .withOption(new Menu.MenuOption("По длительности", ()
                        -> App.getInstance().setComparator(Movie.compareByHourLength)))
                .build();
        Menu saveMenu = Menu.StepBuilder.newBuilder()
                .withTitle("Сохранить список фильмов:")
                .withPrompt("Выберите одну из опций: ")
                .withOption(new Menu.MenuOption("В текстовый файл", () -> {
                    String filepath = App.getInstance().getFilepath("Укажите путь к файлу: ");
                    Scanner scanner = App.getInstance().getScanner();
                    App.getInstance().setSaver(new DefaultSaver(filepath, scanner));
                }))
                .build();
        
        App.StepBuilder.newBuilder()
               .withMainMenu(mainMenu)
               .withFillMenu(fillMenu)
               .withSortMenu(sortMenu)
               .withCompMenu(compMenu)
               .withSaveMenu(saveMenu)
               .build()
               .run();
    }
    
}
