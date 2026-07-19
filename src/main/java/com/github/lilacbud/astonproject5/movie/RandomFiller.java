package com.github.lilacbud.astonproject5.movie;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Base64;
import java.util.Collection;
import static java.util.Objects.requireNonNull;
import java.util.Random;
import java.util.stream.Stream;

public class RandomFiller implements MoviesFiller {
    private static final Random RANDOM = new Random();
    private static final int MIN_NAME_LENGTH = 5;
    private static final int MAX_NAME_LENGTH = 10;
    private static final int MAX_YEAR = 2030;
    private static final float MAX_HOUR_LENGTH = 3F;
    private static final int HL_PRECISION = 3;
    private static final float HL_EPS = 1F / (float)Math.pow(10, 3);
    
    private final int size;
    private final Movie.Builder builder = new Movie.Builder();
    
    public RandomFiller(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size cannot be negative");
        }
        this.size = size;
    }
    
    @Override
    public void fillMovies(Collection<Movie> movies) {
        requireNonNull(movies, "Movies must not be null").clear();
        Stream.generate(this::generateMovie)
                .limit(size)
                .forEach(movies::add);
    }
    
    private Movie generateMovie(){
        return builder
                .withName(generateName())
                .withYearOfRelease(generateYearOfRelease())
                .withHourLength(generateHourLength())
                .build();
    }
    
    private String generateName() { 
        return generateString(MIN_NAME_LENGTH, MAX_NAME_LENGTH + 1);
    }
    
    private int generateYearOfRelease() {
        return generateInt(MovieInputValidation.MIN_YEAR, MAX_YEAR +1);
    }
    
    private float generateHourLength() {
        return generateFloatWithPrecision(MovieInputValidation.MIN_HOUR_LENGTH, MAX_HOUR_LENGTH + HL_EPS, HL_PRECISION);
    }
    
    private String generateString(int length) {
        final byte[] temp = new byte[length];
        RANDOM.nextBytes(temp);
        return Base64.getUrlEncoder().encodeToString(temp);
    }
    
    private String generateString(int minLengthIncl, int maxLengthExcl) {
        return generateString(generateInt(minLengthIncl, maxLengthExcl));
    }
    
    private int generateInt(int minIncl, int maxExcl) {
        return RANDOM.nextInt(minIncl, maxExcl);
    }
    
    private float generateFloat(float minIncl, float maxExcl) {
        return RANDOM.nextFloat(minIncl, maxExcl);
    }
    
    private float generateFloatWithPrecision(float minIncl, float maxExcl, int precision) {
        if (precision < 0) {
            throw new IllegalArgumentException("Precision must not be negative");
        }
        BigDecimal temp = new BigDecimal(Float.toString(generateFloat(minIncl, maxExcl)));
        temp = temp.setScale(precision, RoundingMode.HALF_UP);
        return temp.floatValue();
    }
}
