package com.skillfactory.android12.ilya;

public class Warrior extends Member {

    public Warrior(String name) {
        super(name);
        this.gold = 20;
    }

    public boolean setGold(int amount) {
        if (!isEnough(amount))
            return false;
        this.gold += amount;
        return true;
    }

    private boolean isEnough(int amount) {
        if ((amount + this.gold) < 0) {
            System.out.println("Это мне пока не по карману!");
            return false;
        }
        return true;
    }






}
