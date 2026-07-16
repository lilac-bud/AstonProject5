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
        Scanner scanner = new Scanner(System.in);
        
        Menu<App> mainMenu = Menu.StepBuilder.<App>newBuilder()
                .withTitle("Главное меню:")
                .withPrompt("Выберите одну из опций: ")
                .withOption(new Menu.MenuOption<>("Заполнить список фильмов", (client) 
                        -> client.fillMovies("Список успешно заполнен")))
                .withOption(new Menu.MenuOption<>("Вывести список фильмов на экран", (client) -> {
                    if (client.moviesIsEmpty())
                        client.fillMovies("Список успешно заполнен");
                    client.printMovies("Список успешно выведен на экран", null);
                }))
                .withOption(new Menu.MenuOption<>("Отсортировать список фильмов", (client) -> {
                    if (client.moviesIsEmpty())
                        client.fillMovies("Список успешно заполнен");
                    client.sortMovies("Список успешно отсортирован");
                }))
                .withOption(new Menu.MenuOption<>("Сохранить фильмы", (client) -> {
                    if (client.moviesIsEmpty())
                        client.fillMovies("Список успешно заполнен");
                    client.saveMovies("Список успешно сохранён");
                }))
                .withOption(new Menu.MenuOption<>("Подсчитать вхождения фильма", (client)->{
                    if (client.moviesIsEmpty()) client.fillMovies("Список успешно заполнен");
                    client.countMovie("Введите название фильма для поиска:", "Фильм \"%s\" встречается %d раз(а)");
                }))
                .withOption(new Menu.MenuOption<>("Закончить работу", (client) -> client.exit()))
                .build();
        Menu<App> setFillerMenu = Menu.StepBuilder.<App>newBuilder()
                .withTitle("Как заполнить список:")
                .withPrompt("Выберите одну из опций: ")
                .withOption(new Menu.MenuOption<>("Из файла", (client) -> {
                    String filepath = client.askFilepath("Укажите путь к файлу: ");
                    client.setFiller(new FromFileFiller(filepath));
                }))
                .withOption(new Menu.MenuOption<>("Случайно", (client) -> {
                    int size = client.askSize("Укажите количество фильмов: ");
                    client.setFiller(new RandomFiller(size));
                }))
                .withOption(new Menu.MenuOption<>("Вручную", (client) -> {
                    int size = client.askSize("Укажите количество фильмов: ");
                    client.setFiller(new ManualFiller(size));
                }))
                .build();
        Menu<App> setSortMenu = Menu.StepBuilder.<App>newBuilder()
                .withTitle("Нужно отсортировать:")
                .withPrompt("Выберите одну из опций: ")
                .withOption(new Menu.MenuOption<>("Все фильмы", (client)
                        -> client.setSortingStrategy(new MergeSort())))
                .withOption(new Menu.MenuOption<>("Фильмы с чётным годом выпуска", (client) -> {
                    SortingStrategy sortStrategy = 
                            new EvenNumbersSortDecorator(new MergeSort(), Movie::getYearOfRelease);
                    client.setSortingStrategy(sortStrategy);
                }))
                .build();
        Menu<App> setCompMenu = Menu.StepBuilder.<App>newBuilder()
                .withTitle("Отсортировать список фильмов:")
                .withPrompt("Выберите одну из опций: ")
                .withOption(new Menu.MenuOption<>("По названию", (client) 
                        -> client.setComparator(Movie.compareByName)))
                .withOption(new Menu.MenuOption<>("По году выпуска", (client)
                        -> client.setComparator(Movie.compareByYearOfRelease)))
                .withOption(new Menu.MenuOption<>("По длительности", (client)
                        -> client.setComparator(Movie.compareByHourLength)))
                .build();
        Menu<App> setSaverMenu = Menu.StepBuilder.<App>newBuilder()
                .withTitle("Сохранить список фильмов:")
                .withPrompt("Выберите одну из опций: ")
                .withOption(new Menu.MenuOption<>("В текстовый файл", (client) -> {
                    String filepath = client.askFilepath("Укажите путь к файлу: ");
                    client.setSaver(new DefaultSaver(filepath, scanner));
                }))
                .build();
        
        App.StepBuilder.newBuilder()
                .withScanner(scanner)
                .withMainMenu(mainMenu)
                .withSetFillerMenu(setFillerMenu)
                .withSetSortMenu(setSortMenu)
                .withSetCompMenu(setCompMenu)
                .withSetSaverMenu(setSaverMenu)
                .build()
                .run();
    }
    
}
