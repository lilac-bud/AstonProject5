package com.github.lilacbud.astonproject5.movie;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import static java.util.Objects.requireNonNull;
import java.util.Random;
import java.util.stream.Stream;

public class RandomFiller implements MoviesFiller {
    private static final Random RANDOM = new Random();
    private static final int MIN_NAME_LENGTH = 5;
    private static final int MAX_NAME_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz"; 
    private static final int MAX_YEAR = 2030;
    private static final float MAX_HOUR_LENGTH = 3F;
    private static final int HL_PRECISION = 3;
    private static final float HL_EPS = (float)(1F / Math.pow(10, 3));
    
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
                .withYearOfRelease(generateYear())
                .withHourLength(generateHourLength())
                .build();
    }
    
    private String generateName() {
        final int length = RANDOM.nextInt(MIN_NAME_LENGTH, MAX_NAME_LENGTH + 1);
        final char[] temp = new char[length];
        for (int i = 0; i < length; ++i) {
            temp[i] = CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length()));
        }
        return new String(temp);
    }
    
    private int generateYear() {
        return RANDOM.nextInt(MovieInputValidation.MIN_YEAR, MAX_YEAR + 1);
    }
    
    private float generateHourLength() {
        final float hourLength = RANDOM.nextFloat(MovieInputValidation.MIN_HOUR_LENGTH, MAX_HOUR_LENGTH + HL_EPS);
        return round(hourLength);
    }
    
    private float round(float hourLength) {
        if (HL_PRECISION < 0) {
            return hourLength;
        }
        BigDecimal temp = new BigDecimal(Float.toString(hourLength));
        temp = temp.setScale(HL_PRECISION, RoundingMode.HALF_UP);
        return temp.floatValue();
    }
}
