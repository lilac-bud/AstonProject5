package com.github.lilacbud.astonproject5.movie.sort;

import java.util.Comparator;
import java.util.List;

public interface SortingStrategy<E> {
     void sort(List<E> movies, Comparator<E> comp);
}
