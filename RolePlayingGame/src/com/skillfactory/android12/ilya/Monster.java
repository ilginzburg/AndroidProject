package com.skillfactory.android12.ilya;

import java.util.Random;

public class Monster extends Member {

    public Monster(String name) {
        super(name);
        Random random = new Random();
        this.turn = random.nextInt(2) == 0;
    }


}
