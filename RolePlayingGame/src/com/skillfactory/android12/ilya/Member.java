package com.skillfactory.android12.ilya;

import java.util.Random;

abstract public class Member {

    protected String name;
    protected int health;
    protected int gold;
    protected int smartness;
    protected int experience;
    protected int force;
    protected boolean turn;

    public Member(String name) {
        this.name = name;
        this.health = 200;
        this.force = 100;
        this.turn = false;
        this.smartness = 10;
        this.experience = 0;
    }


    public String getName() {
        return name;
    }

    public int getGold() {
        return gold;
    }

    public void attack(Member member) {
        if (isHit(member)) {
            hit(member);
        } else
            miss(member);
        turn = !turn;
        member.setTurn(!member.getTurn());
    }

    private void hit(Member member){
        member.setHealth(-force);
        System.out.println(name + " больно ударил " + member.getName() + "а");
    }

    public void miss(Member member){
        System.out.println(name + " промахнулся! ");
    }

    private boolean isHit(Member member) {
        Random random = new Random();
        int randomInt = random.nextInt(101);
        return randomInt < (3 * this.smartness);
    }

    public boolean isDead() {
        return this.health <= 0;
    }


    public void printState() {
        System.out.println("Имя: " + name + ", Золото: " + gold + ", Здоровье: " + health);
        System.out.println("Ловкость: " + smartness + ", Опыт: " + experience + ", Сила: " + force);
        System.out.println();
    }


    public void setHealth(int amount) {
        this.health += amount;
    }


    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public boolean getTurn() {
        return turn;
    }

    public int getSmartness() {
        return smartness;
    }

    public void setSmartness(int smartness) {
        this.smartness += smartness;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience += experience;
    }
}
