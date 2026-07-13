package com.github.lilacbud.astonproject5.movie.sort;

import com.github.lilacbud.astonproject5.movie.Movie;

@FunctionalInterface
public interface IntegerFieldExtractor {

    int getValue(Movie movie);

}
