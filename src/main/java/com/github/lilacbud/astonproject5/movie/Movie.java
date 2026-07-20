package com.github.lilacbud.astonproject5.movie;

import java.util.Comparator;
import static java.util.Objects.requireNonNullElse;

public class Movie {
    public static final Comparator<Movie> compareByName = Comparator.comparing(Movie::getName);
    public static final Comparator<Movie> compareByYearOfRelease = Comparator.comparing(Movie::getYearOfRelease);
    public static final Comparator<Movie> compareByHourLength = Comparator.comparingDouble(Movie::getHourLength);

    private final String name;
    private final int yearOfRelease;
    private final float hourLength;

    private Movie (Builder builder) {
        this.name = builder.name;
        this.yearOfRelease = builder.yearOfRelease;
        this.hourLength = builder.hourLength;
    }

    public String getName() {
        return name;
    }

    public int getYearOfRelease() {
        return yearOfRelease;
    }

    public float getHourLength() {
        return hourLength;
    }

    @Override
    public String toString() {
        return String.format("Movie {Name: %s Year of release: %d Hour length: %f }", name, yearOfRelease, hourLength);
    }

    public static class Builder {
        private String name = "";
        private int yearOfRelease;
        private float hourLength;

        public Builder withName(String name) {
            this.name = requireNonNullElse(name, "");
            return this;
        }

        public Builder withYearOfRelease(int year) {
            this.yearOfRelease = year;
            return this;
        }

        public Builder withHourLength(float hourLength) {
            this.hourLength = hourLength;
            return this;
        }

        public Movie build() {
            return new Movie(this);
        }
    }

}
