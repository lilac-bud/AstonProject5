package com.github.lilacbud.astonproject5.movie;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Stream;

public class RandomFiller implements MoviesFiller {
    private static final Random RANDOM = new Random();
    private static final int MIN_NAME_LENGTH = 5;
    private static final int MAX_NAME_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" 
            + "0123456789" + "abcdefghijklmnopqrstuvxyz"; 
    private static final int MIN_YEAR = 1895;
    private static final int MAX_YEAR = 2030;
    private static final float MIN_HOUR_LENGTH = 0.05F;
    private static final float MAX_HOUR_LENGTH = 3F;
    private static final int HL_PRECISION = 3;
    private static final float HL_EPS = (float)(1F / Math.pow(10, 3));
    
    private final int size;
    
    public RandomFiller(int size) {
        if (size < 0)
            size = 0;
        this.size = size;
    }
    @Override
    public void fillMovies(Collection<Movie> movies) {
        movies.clear();
        Stream.generate(this::generateMovie)
                .limit(size)
                .forEach(movies::add);
    }
    
    private Movie generateMovie(){
        final Movie movie = new Movie.Builder()
                .withName(generateName())
                .withYearOfRelease(generateYear())
                .withHourLength(generateHourLength())
                .build();
        return movie;
    }
    private String generateName() {
        final int length = RANDOM.nextInt(MIN_NAME_LENGTH, MAX_NAME_LENGTH + 1);
        final char[] temp = new char[length];
        for (int i = 0; i < length; i++)
        {
            final int charIndex = RANDOM.nextInt(CHARACTERS.length());
            temp[i] = CHARACTERS.charAt(charIndex);
        }
        return new String(temp);
    }
    private int generateYear() {
        return RANDOM.nextInt(MIN_YEAR, MAX_YEAR + 1);
    }
    private float generateHourLength() {
        final float hourLength = 
                RANDOM.nextFloat(MIN_HOUR_LENGTH, MAX_HOUR_LENGTH + HL_EPS);
        return round(hourLength);
    }
    private float round(float hourLength) {
        if (HL_PRECISION < 0)
            return hourLength;
        BigDecimal temp = new BigDecimal(Float.toString(hourLength));
        temp = temp.setScale(HL_PRECISION, RoundingMode.HALF_UP);
        return temp.floatValue();
    }
}
