package com.github.lilacbud.astonproject5.movie;

import java.util.Comparator;

public class Movie {
    static class Builder{
        Builder withName(String name){
            return this;
        }
        Builder withYearOfRelease(int year){
            return this;
        }
        Builder withHourLength(float hourLength){
            return this;
        }
        Movie build(){
            return new Movie(this);
        }
    }

    public static Comparator<Movie> compareByName;
    public static Comparator<Movie> compareByYearOfRelease;
    public static Comparator<Movie> compareHourLength;

    Movie(Builder builder){

    }
    public String getName(){
        return "";
    }
    public int getYearOfRelease(){
        return 0;
    }
    public float getHourLength(){
        return 0F;
    }
}