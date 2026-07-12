package com.github.lilacbud.astonproject5.movie.save;

import com.github.lilacbud.astonproject5.movie.Movie;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.nio.file.Path;
import java.util.Scanner;
import java.nio.file.StandardOpenOption;

public class DefaultSaver implements MoviesSaver{
    private final Path filePath;
    private final Scanner scanner;

    public DefaultSaver(String filepath){
        this.filePath=Path.of(filepath);
        this.scanner = new Scanner(System.in);
    }
    @Override
    public void save(Collection<Movie> movies){
        StandardOpenOption option = StandardOpenOption.CREATE;

        if (Files.exists(filePath)){
            while(true){
                System.out.println("Файл уже существует. 1 - перезаписать, 2 - добавить");

                String answer = scanner.nextLine();
                if(answer.equals("1")){
                    option = StandardOpenOption.TRUNCATE_EXISTING;
                    break;
                }
                if (answer.equals("2")){
                    option = StandardOpenOption.APPEND;
                    break;
                }
                System.err.println("incorrect input");
            }
        }

        try (BufferedWriter writer=Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, option)){
            for (Movie movie:movies){
                writer.write(movie.getName()+";"+movie.getYearOfRelease()+";"+movie.getHourLength());
                writer.newLine();
            }
        } catch (IOException e){
            throw new RuntimeException("File Save Error", e);
        }
    }
}
