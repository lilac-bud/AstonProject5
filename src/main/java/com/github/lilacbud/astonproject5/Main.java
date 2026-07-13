package com.github.lilacbud.astonproject5;

import com.github.lilacbud.astonproject5.movie.FromFileFiller;
import com.github.lilacbud.astonproject5.movie.ManualFiller;
import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.movie.RandomFiller;
import com.github.lilacbud.astonproject5.movie.save.DefaultSaver;
import com.github.lilacbud.astonproject5.movie.sort.EvenNumbersSortDecorator;
import com.github.lilacbud.astonproject5.movie.sort.MergeSort;
import com.github.lilacbud.astonproject5.user.Menu;
import java.util.List;
import java.util.Scanner;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Menu.SubMenu mainMenu = new Menu.SubMenu("Главное меню:", "Выберите одну из опций: ",
            List.of(
                    new Menu.MenuOption("Заполнить список фильмов", () 
                            -> Menu.getInstance().fillMovies("Список успешно заполнен")),
                    new Menu.MenuOption("Вывести список фильмов на экран", () -> {
                        if (Menu.getInstance().moviesIsEmpty())
                            Menu.getInstance().fillMovies("Список успешно заполнен");
                        Menu.getInstance().printMovies("Список успешно выведен на экран");
                    }),
                    new Menu.MenuOption("Отсортировать список фильмов", () -> {
                        if (Menu.getInstance().moviesIsEmpty())
                            Menu.getInstance().fillMovies("Список успешно заполнен");
                        Menu.getInstance().sortMovies("Список успешно отсортирован");
                    }),
                    new Menu.MenuOption("Сохранить фильмы", () -> {
                        if (Menu.getInstance().moviesIsEmpty())
                            Menu.getInstance().fillMovies("Список успешно заполнен");
                        Menu.getInstance().saveMovies("Список успешно сохранён");
                    }),
                    new Menu.MenuOption("Закончить работу", () -> Menu.getInstance().exit())  
            )
        );
        Menu.SubMenu fillMenu = new Menu.SubMenu("Как заполнить список:", "Выберите одну из опций: ",
                List.of(
                        new Menu.MenuOption("Из файла", () -> {
                            String filepath = Menu.getInstance().getFilepath("Укажите путь к файлу: ");
                            Menu.getInstance().setFiller(new FromFileFiller(filepath));
                        }),
                        new Menu.MenuOption("Случайно", () -> {
                            int size = Menu.getInstance().getSize("Укажите количество фильмов: ");
                            Menu.getInstance().setFiller(new RandomFiller(size));
                        }),
                        new Menu.MenuOption("Вручную", () -> {
                            int size = Menu.getInstance().getSize("Укажите количество фильмов: ");
                            Menu.getInstance().setFiller(new ManualFiller(size));
                        })
                )
        );
        Menu.SubMenu sortMenu = new Menu.SubMenu("Нужно отсортировать:", "Выберите одну из опций: ", 
                List.of(
                        new Menu.MenuOption("Все фильмы", () 
                                -> Menu.getInstance().setSortingStrategy(new MergeSort())),
                        new Menu.MenuOption("Фильмы с чётным годом выпуска", () 
                                -> Menu.getInstance().setSortingStrategy(new EvenNumbersSortDecorator(new MergeSort(), 
                                        Movie::getYearOfRelease)))
                )
        );
        Menu.SubMenu compMenu = new Menu.SubMenu("Отсортировать список фильмов:", "Выберите одну из опций: ",
                List.of(
                        new Menu.MenuOption("По названию", () -> Menu.getInstance().setComparator(Movie.compareByName)),
                        new Menu.MenuOption("По году выпуска", ()
                                -> Menu.getInstance().setComparator(Movie.compareByYearOfRelease)),
                        new Menu.MenuOption("По длительности", () -> 
                                Menu.getInstance().setComparator(Movie.compareByHourLength))
                )
        );
        Menu.SubMenu saveMenu = new Menu.SubMenu("Сохранить список фильмов:", "Выберите одну из опций: ",
                List.of(
                        new Menu.MenuOption("В текстовый файл", () -> {
                            String filepath = Menu.getInstance().getFilepath("Укажите путь к файлу: ");
                            Scanner scanner = Menu.getInstance().getScanner();
                            Menu.getInstance().setSaver(new DefaultSaver(filepath, scanner));
                        })
                )
        );
        new Menu.Builder()
                .withMainMenu(mainMenu)
                .withFillMenu(fillMenu)
                .withSortMenu(sortMenu)
                .withCompMenu(compMenu)
                .withSaveMenu(saveMenu)
                .build()
                .run();
    }
    
}
