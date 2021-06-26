package com.skillfactory.android12.ilya;

import java.util.Scanner;

public class UserInterface {

    private final Game game;
    Scanner scanner = new Scanner(System.in);

    public UserInterface(Game game){
        this.game = game;
    }

    public void printWelcome(){
        System.out.print("Здравствуй, добрый молодец! ");
        askName();
    }

    private void askName(){
        System.out.println("Как имя твоё?");
        game.createWarrior(scanner.nextLine());
    }

    public void userChoice(){
        boolean isPassed;

            System.out.println("Слушай, " + game.getWarrior().getName() + "!");
            System.out.println("Вот идёшь ты, идёшь....");
            System.out.println("Идёшь ты, идёшь....");
            System.out.println("И видишь - камень на пути, рядом клавиатура валяется, а на камне том написано:");
        do {
            System.out.println("D нажмёшь - всю правду о себе узнаешь.");
            System.out.println("A нажмёшь - на базар придёшь.");
            System.out.println("W нажмёшь - в тёмный лес попадёшь.");
            System.out.println("X нажмёшь - домой к себе приползёшь.");
            System.out.println("ЖМИ ДАВАЙ !!!");
            isPassed = decodeChoice();
        }
        while(!isPassed);
    }

    private boolean decodeChoice(){
        switch(scanner.next()){
            case "a","A","ф","Ф","а","А","f","F":
                new Trader().makeService(game.getWarrior(),scanner);
                return false;
            case "d","D","в","В":
                game.getWarrior().printState();
                return false;
            case "x","X","ч","Ч":
                game.endGame();
                return true;
            case "w", "W","ц","Ц":
                Thread t1 = new Thread() {
                    public void run() {
                        game.fightFlow();
                    }
                };
                t1.start();
                try {
                    t1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return false;
            default:
                System.out.println("Не туда жмёшь. Попробуй ещё разок.");
                return false;
        }
    }
}
