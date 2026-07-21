package com.github.lilacbud.astonproject5.movie.save;

import com.github.lilacbud.astonproject5.app.MoviesSaver;
import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.user.Menu;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import static java.util.Objects.requireNonNull;
import java.util.Scanner;

public class DefaultSaver implements MoviesSaver {
    public static final String FILEPATH_NULL_MESSAGE = "Filepath must not be null";
    public static final String SCANNER_NULL_MESSAGE = "If menu is not null scanner also must not be null";
    public static final String COLLECTION_NULL_MESSAGE = "Collection must not be null";
    public static final String FILE_SAVE_FAIL_MESSAGE = "File save failed";
    
    private final Path filePath;
    private final Menu<? super DefaultSaver> setSaveOptionMenu;
    private final Scanner scanner;
    private StandardOpenOption saveOption = StandardOpenOption.TRUNCATE_EXISTING;
    
    public DefaultSaver(String filepath, Menu<? super DefaultSaver> setSaveOptionMenu, Scanner scanner) {
        this.filePath = Path.of(requireNonNull(filepath, FILEPATH_NULL_MESSAGE));
        if (setSaveOptionMenu != null) {
            requireNonNull(scanner, SCANNER_NULL_MESSAGE);
        }
        this.setSaveOptionMenu = setSaveOptionMenu;
        this.scanner = scanner;
    }
    
    public DefaultSaver(String filepath) {
        this(filepath, null, null);
    }

    @Override
    public void setSaveOption(StandardOpenOption saveOption) {
        this.saveOption = saveOption;
    }
    
    @Override
    public void save(Collection<Movie> movies){
        requireNonNull(movies, COLLECTION_NULL_MESSAGE);
        if (Files.exists(filePath) && setSaveOptionMenu != null) {
            setSaveOptionMenu.chooseOptionAndExecute(scanner, this);
        }
        try (BufferedWriter writer=Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, saveOption)) {
            for (Movie movie : movies) {
                writer.write(movie.getName() + ";" + movie.getYearOfRelease() + ";" + movie.getHourLength());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(FILE_SAVE_FAIL_MESSAGE, e);
        }
    }
}
