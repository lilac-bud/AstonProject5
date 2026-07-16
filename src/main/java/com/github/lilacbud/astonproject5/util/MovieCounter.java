package com.github.lilacbud.astonproject5.util;

import java.util.List;
import com.github.lilacbud.astonproject5.movie.Movie;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class MovieCounter {
    private MovieCounter() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static int countInsert(List<Movie> movies, String target) {
        if (movies == null || movies.isEmpty() || target == null) return 0;

        int threadCount = Runtime.getRuntime().availableProcessors();
        AtomicInteger totalCount = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        int chunkSize = (int) Math.ceil((double) movies.size() / threadCount);

        for (int i = 0; i < threadCount; i++) {
            int from = i * chunkSize;
            int to = Math.min(from + chunkSize, movies.size());
            if (from >= movies.size()) break;

            executor.submit(() -> {
                int localCount = 0;
                for (int j = from; j < to; j++) {
                    if (target.equals(movies.get(j).getName())) localCount++;
                }
                totalCount.addAndGet(localCount);
            });
        }
        executor.shutdown();
        try{
            executor.awaitTermination(1,TimeUnit.MINUTES);
        } catch (InterruptedException e) {Thread.currentThread().interrupt();}
        return totalCount.get();
    }
}