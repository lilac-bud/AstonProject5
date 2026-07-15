package com.github.lilacbud.astonproject5.movie.save;

import com.github.lilacbud.astonproject5.movie.Movie;
import java.nio.file.StandardOpenOption;
import java.util.Collection;

public interface MoviesSaver {
    public void setSaveOption(StandardOpenOption saveOption);
    public void save(Collection<Movie> movies);
}
