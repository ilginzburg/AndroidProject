package com.skillfactory.android12.ilya;

public class Main {
    public static void main(String[] args) {
        init();
       // start();
    }

    private static void init(){
        Game game = new Game();
        UserInterface ui = new UserInterface(game);
        ui.printWelcome();

        ui.userChoice();

    }

    private static void start(){

    }
}
