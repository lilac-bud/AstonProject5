package com.github.lilacbud.astonproject5.sort;

import com.github.lilacbud.astonproject5.app.SortingStrategy;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EvenNumbersSortDecorator<E> extends SortDecorator<E> {
    public static final String EXTRACTOR_NULL_MESSAGE = "Extractor must not be null";
    public static final String LIST_NULL_MESSAGE = "List must not be null";
    
    private final ToIntFunction<E> extractor;

    public EvenNumbersSortDecorator(SortingStrategy<E> sortingStrategy, ToIntFunction<E> extractor) {
        super(sortingStrategy);
        this.extractor = requireNonNull(extractor, EXTRACTOR_NULL_MESSAGE);
    }

    @Override
    public void sort(List<E> list, Comparator<E> comp) {
        if (requireNonNull(list, LIST_NULL_MESSAGE).isEmpty()) {
            return;
        }
        final List<Integer> evenIndices = IntStream.range(0, list.size()).boxed()
                .filter(i -> numberIsEven(list.get(i)))
                .toList();
        final List<E> evenResult = evenIndices.stream()
                .map(list::get)
                .collect(Collectors.toList());
        
        sortingStrategy.sort(evenResult, comp);
        Iterator<E> evenResultIt = evenResult.iterator();
        evenIndices.forEach(i -> list.set(i, evenResultIt.next()));
    }

    private boolean numberIsEven(E elem) {
        return extractor.applyAsInt(elem) % 2 == 0;
    }
}
