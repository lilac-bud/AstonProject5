package com.github.lilacbud.astonproject5.app;

import java.util.Comparator;
import java.util.List;

public interface SortingStrategy<E> {
     void sort(List<E> list, Comparator<E> comp);
}
