package com.github.lilacbud.astonproject5;

import com.github.lilacbud.astonproject5.movie.FromFileFiller;
import com.github.lilacbud.astonproject5.movie.ManualFiller;
import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.movie.MoviesFiller;
import com.github.lilacbud.astonproject5.movie.RandomFiller;
import com.github.lilacbud.astonproject5.user.Menu;
import java.util.List;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Menu.SubMenu mainMenu = new Menu.SubMenu("Главное меню:",
            List.of(
                    new Menu.MenuOption("Заполнить список фильмов", () -> Menu.getInstance().fillMovies()),
                    new Menu.MenuOption("Вывести список фильмов на экран", () -> {
                        Menu.getInstance().checkIfMoviesEmpty();
                        Menu.getInstance().printMovies();
                    }),
                    new Menu.MenuOption("Отсортировать список фильмов", () -> {
                        Menu.getInstance().checkIfMoviesEmpty();
                        Menu.getInstance().sortMovies();
                    }),
                    new Menu.MenuOption("Сохранить фильмы", () -> {
                        Menu.getInstance().checkIfMoviesEmpty();
                        Menu.getInstance().saveMovies();
                    }),
                    new Menu.MenuOption("Закончить работу", () -> Menu.getInstance().exit())  
            )
        );
        Menu.SubMenu fillMenu = new Menu.SubMenu("Как заполнить список:",
                List.of(
                        new Menu.MenuOption("Из файла", () -> {
                            MoviesFiller filler = new FromFileFiller(Menu.getInstance().getFilepath());
                            Menu.getInstance().setFiller(filler);
                        }),
                        new Menu.MenuOption("Случайно", () -> {
                            MoviesFiller filler = new RandomFiller(Menu.getInstance().getSize());
                            Menu.getInstance().setFiller(filler);
                        }),
                        new Menu.MenuOption("Вручную", () -> {
                            MoviesFiller filler = new ManualFiller(Menu.getInstance().getSize());
                            Menu.getInstance().setFiller(filler);
                        })
                )
        );
        Menu.SubMenu compMenu = new Menu.SubMenu("Отсортировать список фильмов:",
                List.of(
                        new Menu.MenuOption("По названию", () -> Menu.getInstance().setComparator(Movie.compareByName)),
                        new Menu.MenuOption("По году выпуска", ()
                                -> Menu.getInstance().setComparator(Movie.compareByYearOfRelease)),
                        new Menu.MenuOption("По длительности", () -> 
                                Menu.getInstance().setComparator(Movie.compareByHourLength))
                )
        );
        new Menu.Builder()
                .withMainMenu(mainMenu)
                .withFillMenu(fillMenu)
                .withCompMenu(compMenu)
                .build()
                .run();
    }
    
}
