package com.github.lilacbud.astonproject5.user;

public class Menu {
    private static Menu instance;
    
    private Menu(){}
    public static Menu getInstance(){
        if (instance == null)
            instance = new Menu();
        return instance;
    }
    public void run(){
        
    }
}
