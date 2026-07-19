package com.github.lilacbud.astonproject5;

import com.github.lilacbud.astonproject5.app.App;
import com.github.lilacbud.astonproject5.movie.FromFileFiller;
import com.github.lilacbud.astonproject5.movie.ManualFiller;
import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.movie.RandomFiller;
import com.github.lilacbud.astonproject5.movie.save.DefaultSaver;
import com.github.lilacbud.astonproject5.movie.save.MoviesSaver;
import com.github.lilacbud.astonproject5.sort.EvenNumbersSortDecorator;
import com.github.lilacbud.astonproject5.sort.MergeSort;
import com.github.lilacbud.astonproject5.sort.SortingStrategy;
import com.github.lilacbud.astonproject5.user.Menu;
import com.github.lilacbud.astonproject5.util.InputRequest;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.function.Consumer;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        SortingStrategy<Movie> mergeSort = new MergeSort<>();
        SortingStrategy<Movie> mergeSortEven = new EvenNumbersSortDecorator<>(mergeSort, Movie::getYearOfRelease);

        Menu<App> setFillerMenu = Menu.StepBuilder.<App>newBuilder()
                .withTitle("Как заполнить список:")
                .withPrompt("Выберите одну из опций: ")
                .withOption(new Menu.MenuOption<>("Из файла", (client) -> {
                    String filepath = InputRequest.askString(scanner, "Укажите путь к файлу: ");
                    client.setFiller(new FromFileFiller(filepath));
                }))
                .withOption(new Menu.MenuOption<>("Случайно", (client) -> {
                    int size = InputRequest.askInteger(scanner, "Укажите количество фильмов: ");
                    client.setFiller(new RandomFiller(size));
                }))
                .withOption(new Menu.MenuOption<>("Вручную", (client) -> {
                    int size = InputRequest.askInteger(scanner, "Укажите количество фильмов: ");
                    var prompts = new ManualFiller.Prompts(
                            "Введите название фильма: ", 
                            "Введите год выпуска: ", 
                            "Введите продолжительность фильма: ");
                    client.setFiller(new ManualFiller(size, scanner, prompts));
                }))
                .build();
        
        Menu<App> setSortMenu = Menu.StepBuilder.<App>newBuilder()
                .withTitle("Нужно отсортировать:")
                .withPrompt("Выберите одну из опций: ")
                .withOption(new Menu.MenuOption<>("Все фильмы", (client) -> client.setSortingStrategy(mergeSort)))
                .withOption(new Menu.MenuOption<>("Фильмы с чётным годом выпуска", 
                        (client) -> client.setSortingStrategy(mergeSortEven)))
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
        
        Menu<MoviesSaver> setSaveOptionMenu = Menu.StepBuilder.<MoviesSaver>newBuilder()
                .withTitle("Файл уже существует.")
                .withPrompt("Выберите одну из опций: ")
                .withOption(new Menu.MenuOption<>("Перезаписать", (client)
                        -> client.setSaveOption(StandardOpenOption.TRUNCATE_EXISTING)))
                .withOption(new Menu.MenuOption<>("Добавить", (client)
                        -> client.setSaveOption(StandardOpenOption.APPEND)))
                .build();
        
        Menu<App> setSaverMenu = Menu.StepBuilder.<App>newBuilder()
                .withTitle("Сохранить список фильмов:")
                .withPrompt("Выберите одну из опций: ")
                .withOption(new Menu.MenuOption<>("В текстовый файл", (client) -> {
                    String filepath = InputRequest.askString(scanner, "Укажите путь к файлу: ");
                    client.setSaver(new DefaultSaver(filepath, scanner, setSaveOptionMenu));
                }))
                .build();
        
        Consumer<App> fillCommand = (client) -> {
            setFillerMenu.chooseOptionAndExecute(scanner, client);
            client.fillMovies();
        };
        
        Consumer<App> sortCommand = (client) -> {
            setSortMenu.chooseOptionAndExecute(scanner, client);
            setCompMenu.chooseOptionAndExecute(scanner, client);
            client.sortMovies();
        };
        
        Consumer<App> saveCommand = (client) -> {
            setSaverMenu.chooseOptionAndExecute(scanner, client);
            client.saveMovies();
        };
        
        Menu<App> mainMenu = Menu.StepBuilder.<App>newBuilder()
                .withTitle("Главное меню:")
                .withPrompt("Выберите одну из опций: ")
                .withOption(new Menu.MenuOption<>("Заполнить список фильмов", (client) ->
                        client.tryCommandTillSuccess("Список успешно заполнен", fillCommand)))
                .withOption(new Menu.MenuOption<>("Вывести список фильмов на экран", (client) -> {
                    if (client.moviesIsEmpty())
                        client.tryCommandTillSuccess("Список успешно заполнен", fillCommand);
                    client.printMovies("Название: %-30s, год выпуска: %d, продолжительность: %.1f ч.", 
                            "Список успешно выведен на экран");
                }))
                .withOption(new Menu.MenuOption<>("Отсортировать список фильмов", (client) -> {
                    if (client.moviesIsEmpty())
                        client.tryCommandTillSuccess("Список успешно заполнен", fillCommand);
                    client.tryCommandTillSuccess("Список успешно отсортирован", sortCommand);
                }))
                .withOption(new Menu.MenuOption<>("Сохранить фильмы", (client) -> {
                    if (client.moviesIsEmpty())
                        client.tryCommandTillSuccess("Список успешно заполнен", fillCommand);
                    client.tryCommandTillSuccess("Список успешно сохранён", saveCommand);
                }))
                .withOption(new Menu.MenuOption<>("Подсчитать вхождения фильма", (client) -> {
                    if (client.moviesIsEmpty())
                        client.tryCommandTillSuccess("Список успешно заполнен", fillCommand);
                    String target = InputRequest.askString(scanner,"Введите название фильма: ");
                    client.countMovie(target, "Фильм \"%s\" найден %d раз(а)");
                }))
                .withOption(new Menu.MenuOption<>("Закончить работу", (client) -> client.exit()))
                .build();

        new App().run((client) -> mainMenu.chooseOptionAndExecute(scanner, client));
    }
    
}
