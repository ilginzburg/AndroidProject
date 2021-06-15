package com.skillfactory.android12.ilya;

import java.util.Random;
import java.util.Scanner;

public class Game {

    private Warrior warrior;
    private Monster monster;

    public void createWarrior(String name) {
        warrior = new Warrior(name);
    }

    public void createMonster() {
        Random random = new Random();
        int nameChoice = random.nextInt(2);
        monster = new Monster((nameChoice == 0) ? "Гоблин" : "Скелет");
    }


    public Warrior getWarrior() {
        return warrior;
    }

    public Monster getMonster() {
        return monster;
    }

    public void fightFlow() {
        createMonster();
        printThreat();
        fight();
        if (monster.isDead()) {
            upgradeWarrior();
            printCongrats();
            return;
        }
        if (warrior.isDead())
            endGame();
    }

    private void printThreat() {
        System.out.println(warrior.getName() + ", навстречу тебе несётся ужасный "
                + monster.getName() + ", он готов разорвать тебя на куски!");
    }

    private void fight() {
        Scanner scanner = new Scanner(System.in);
        while (!(monster.isDead() || warrior.isDead())) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (monster.getTurn())
                monster.attack(warrior);
            else {
                System.out.println(warrior.getName()+", нажми 'я' для удара");
                if(scanner.next().equals("я"))
                    warrior.attack(monster);
            }
        }
    }


    private void upgradeWarrior() {
        warrior.setExperience(1);
        warrior.setSmartness(1);
        warrior.setGold(40);
    }

    private void printCongrats() {
        System.out.println(warrior.getName() + ", красава! Ты победил " + monster.getName() + "а!");
    }

    private void printGoodBye() {
        System.out.println("Игра окончена, до новых встреч," + warrior.getName() + "! ");
    }

    public void endGame() {
        printGoodBye();
        System.exit(0);
    }


}
