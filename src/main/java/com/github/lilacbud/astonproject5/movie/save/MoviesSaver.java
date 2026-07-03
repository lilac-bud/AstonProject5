package com.github.lilacbud.astonproject5.movie.save;

import com.github.lilacbud.astonproject5.movie.Movie;
import java.util.Collection;

public interface MoviesSaver {
    public void save(Collection<Movie> movies);
}
