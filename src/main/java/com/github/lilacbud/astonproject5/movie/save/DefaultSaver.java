package com.github.lilacbud.astonproject5.movie.save;

import com.github.lilacbud.astonproject5.movie.Movie;
import com.github.lilacbud.astonproject5.user.Menu;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.nio.file.Path;
import java.util.Scanner;
import java.nio.file.StandardOpenOption;

import static java.util.Objects.requireNonNull;

public class DefaultSaver implements MoviesSaver {
    private final Path filePath;
    private final Scanner scanner;
    private final Menu<MoviesSaver> setSaveOptionMenu;
    private StandardOpenOption saveOption = StandardOpenOption.TRUNCATE_EXISTING;

    public DefaultSaver(String filepath, Scanner scanner, Menu<MoviesSaver> setSaveOptionMenu) {
        this.filePath = Path.of(requireNonNull(filepath, "Filepath must not be null"));
        this.scanner = requireNonNull(scanner, "Scanner must not be null");
        this.setSaveOptionMenu = setSaveOptionMenu;
    }

    @Override
    public void setSaveOption(StandardOpenOption saveOption) {
        this.saveOption = saveOption;
    }
    @Override
    public void save(Collection<Movie> movies){

        requireNonNull(movies, "Collection<Movie> movies must be non null to save");
        
        if (Files.exists(filePath) && setSaveOptionMenu != null)
            setSaveOptionMenu.chooseOption(scanner).execute(this);

        try (BufferedWriter writer=Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, saveOption)) {
            for (Movie movie:movies) {
                writer.write(movie.getName()+";"+movie.getYearOfRelease()+";"+movie.getHourLength());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("File Save Error", e);
        }
    }
}
