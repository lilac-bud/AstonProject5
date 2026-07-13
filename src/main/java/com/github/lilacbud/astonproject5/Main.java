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
                            -> App.getInstance().fillMovies("Список успешно заполнен")),
                    new Menu.MenuOption("Вывести список фильмов на экран", () -> {
                        if (App.getInstance().moviesIsEmpty())
                            App.getInstance().fillMovies("Список успешно заполнен");
                        App.getInstance().printMovies("Список успешно выведен на экран");
                    }),
                    new Menu.MenuOption("Отсортировать список фильмов", () -> {
                        if (App.getInstance().moviesIsEmpty())
                            App.getInstance().fillMovies("Список успешно заполнен");
                        App.getInstance().sortMovies("Список успешно отсортирован");
                    }),
                    new Menu.MenuOption("Сохранить фильмы", () -> {
                        if (App.getInstance().moviesIsEmpty())
                            App.getInstance().fillMovies("Список успешно заполнен");
                        App.getInstance().saveMovies("Список успешно сохранён");
                    }),
                    new Menu.MenuOption("Закончить работу", () -> App.getInstance().exit())  
            )
        );
        Menu.SubMenu fillMenu = new Menu.SubMenu("Как заполнить список:", "Выберите одну из опций: ",
                List.of(
                        new Menu.MenuOption("Из файла", () -> {
                            String filepath = App.getInstance().getFilepath("Укажите путь к файлу: ");
                            App.getInstance().setFiller(new FromFileFiller(filepath));
                        }),
                        new Menu.MenuOption("Случайно", () -> {
                            int size = App.getInstance().getSize("Укажите количество фильмов: ");
                            App.getInstance().setFiller(new RandomFiller(size));
                        }),
                        new Menu.MenuOption("Вручную", () -> {
                            int size = App.getInstance().getSize("Укажите количество фильмов: ");
                            App.getInstance().setFiller(new ManualFiller(size));
                        })
                )
        );
        Menu.SubMenu sortMenu = new Menu.SubMenu("Нужно отсортировать:", "Выберите одну из опций: ", 
                List.of(
                        new Menu.MenuOption("Все фильмы", () 
                                -> App.getInstance().setSortingStrategy(new MergeSort())),
                        new Menu.MenuOption("Фильмы с чётным годом выпуска", () 
                                -> App.getInstance().setSortingStrategy(new EvenNumbersSortDecorator(new MergeSort(), 
                                        Movie::getYearOfRelease)))
                )
        );
        Menu.SubMenu compMenu = new Menu.SubMenu("Отсортировать список фильмов:", "Выберите одну из опций: ",
                List.of(
                        new Menu.MenuOption("По названию", () -> App.getInstance().setComparator(Movie.compareByName)),
                        new Menu.MenuOption("По году выпуска", ()
                                -> App.getInstance().setComparator(Movie.compareByYearOfRelease)),
                        new Menu.MenuOption("По длительности", () -> 
                                App.getInstance().setComparator(Movie.compareByHourLength))
                )
        );
        Menu.SubMenu saveMenu = new Menu.SubMenu("Сохранить список фильмов:", "Выберите одну из опций: ",
                List.of(
                        new Menu.MenuOption("В текстовый файл", () -> {
                            String filepath = App.getInstance().getFilepath("Укажите путь к файлу: ");
                            Scanner scanner = App.getInstance().getScanner();
                            App.getInstance().setSaver(new DefaultSaver(filepath, scanner));
                        })
                )
        );
        new App.Builder()
               .withMainMenu(mainMenu)
               .withFillMenu(fillMenu)
               .withSortMenu(sortMenu)
               .withCompMenu(compMenu)
               .withSaveMenu(saveMenu)
               .build()
               .run();
    }
    
}
