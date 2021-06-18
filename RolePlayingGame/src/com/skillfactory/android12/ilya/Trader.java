package com.skillfactory.android12.ilya;

import java.util.Scanner;

public class Trader implements Marketeer {

    final int potionPrice = 10;
    final int healthDeltaPlus = 200;
    final String name;

    public Trader() {
        this.name = "Старьёвщик Джо";
    }

    public void makeService(Warrior warrior, Scanner scanner) {
        sayHello(warrior, scanner);
        if (getAnswer(scanner) && sellPotion(warrior))
            sayGoodByeYes();
         else
            sayGoodByeNo();
        sayGoodBye(warrior);
    }

    private void sayHello(Warrior warrior, Scanner scanner) {
        System.out.println("Здравствуй, " + warrior.getName() + "! ");
        System.out.println("Заходи дорогой, гостем будешь! ");
        System.out.println("Я - " + name + ".");
        System.out.println("У меня есть отличное зелье - исцелит от всех ран. ");
        System.out.println("Только для тебя и только сегодня - всего за 10 золотых. ");
        System.out.println("Покупаешь? (у - да, другое - нет)");
    }

    private boolean getAnswer(Scanner scanner) {
        String ans = scanner.next();
        return ans.equals("y") || ans.equals("yes") || ans.equals("н") ||
                ans.equals("да") || ans.equals("Н") || ans.equals("Y");
    }

    private boolean sellPotion(Warrior warrior) {
        if (warrior.setGold(-potionPrice)) {
            warrior.setHealth(healthDeltaPlus);
            return true;
        }
        return false;
    }

    private void sayGoodBye(Warrior warrior) {
        System.out.println("До свидания, " + warrior.getName() + "!");
        System.out.println();
    }
}
