package com.github.lilacbud.astonproject5.movie.save;

import com.github.lilacbud.astonproject5.movie.Movie;
import java.nio.file.StandardOpenOption;
import java.util.Collection;

public interface MoviesSaver {
    void setSaveOption(StandardOpenOption saveOption);
    void save(Collection<Movie> movies);
}
